import {Component, OnInit} from '@angular/core';
import {
  ChatInfo,
  ChatMessageInfo,
  ChatMessageRequest,
  ChatParticipant,
  ChatService,
  NewChatService
} from "./chat.service";
import {BehaviorSubject} from "rxjs";
import {LoginService} from "../login/login.service";
import {MatSnackBar} from '@angular/material/snack-bar';
import {UserProfile} from "../profile/profile.service";
import {WsClient} from "../wsClient";

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  currentChat: ChatInfo;
  chats: BehaviorSubject<ChatInfo[]> = new BehaviorSubject<ChatInfo[]>([]);
  currentChatMessages: BehaviorSubject<ChatMessageInfo[]> = new BehaviorSubject<ChatMessageInfo[]>([]);

  currentMessage: string;

  constructor(private chatService: ChatService,
              private loginService: LoginService,
              private snackBar: MatSnackBar,
              private newChatService: NewChatService,
              private wsClient:WsClient) {
    console.log("Init  ChatComponent");
  }

  ngOnInit(): void {


    this.wsClient.subscriberMsg(this.appendMessage.bind(this))

    this.chatService.getChats().subscribe(this.chats);

    this.newChatService.newChatEvent.subscribe((v) => this.newChat(v));
  }

  getChatLabel(chat: ChatInfo): string {

    return chat.participants
      .filter(v => v.userId != this.loginService.currentUserValue.id)
      .map(v => v.name + "")
      .reduce((a, b) => a + ", " + b);
  }

  notMeParticipant(chat: ChatInfo): ChatParticipant {
    return chat.participants
      .find(v => v.userId != this.loginService.currentUserValue.id);
  }

  newChat(user: UserProfile) {

    let chatInfo = this.chats.value.find(v => v.participants.length <= 2 && this.notMeParticipant(v).userId == user.id);

    if (chatInfo == null) {
      chatInfo = new ChatInfo(null, [new ChatParticipant(user.id, user.firstName + " " + user.surname)], null);
      this.chats.value.push(chatInfo);
    }
    this.selectChat(chatInfo);
  }

  selectChat(chat: ChatInfo) {
    this.currentChat = chat;
    this.currentMessage = "";

    if (chat == null || chat.id == null) {
      this.currentChatMessages = new BehaviorSubject<ChatMessageInfo[]>([]);
      this.chats.next(this.chats.value);

    } else {
      this.chatService.getMessages(chat.id)
        .subscribe(this.currentChatMessages);
    }
  }

  isMyMessage(msg: ChatMessageInfo) {
    return msg.author.userId == this.loginService.currentUserValue.id;
  }

  addMessage() {

    if (!this.currentMessage) {
      return;
    }

    this.chatService.addMessage(new ChatMessageRequest(this.currentChat.id, this.notMeParticipant(this.currentChat).userId, this.currentMessage))
      .subscribe(msg => {

          this.currentChat.id = msg.chatId;
          this.appendMessage(msg);
          this.currentMessage = "";
        }
      );
  }

  appendMessage(msg: ChatMessageInfo) {
    if (this.currentChat != null && msg.chatId == this.currentChat.id) {
      this.currentChatMessages.value.push(msg);
      this.currentChatMessages.next(this.currentChatMessages.value);
      this.currentChat.lastMessage = msg;
    } else {

      let chatInfo = this.chats.value.find(v => v.id == msg.chatId);

      if (chatInfo != null) {
        this.showPopup(msg, chatInfo);
      } else {
        this.chatService.getChat(msg.chatId).subscribe(v => {
          this.chats.value.push(v);
          this.showPopup(msg, v);
        });
      }
    }

  }

  private showPopup(msg: ChatMessageInfo, v: ChatInfo) {
    this.snackBar
      .open(msg.author.name + ': ' + msg.message, 'View', {
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
      })
      .onAction().subscribe(() => {
      this.selectChat(v);
    });
  }
}
