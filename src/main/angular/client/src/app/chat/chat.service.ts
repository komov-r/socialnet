import * as Stomp from 'stompjs';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {EventEmitter, Injectable} from "@angular/core";
import {LoginService} from "../login/login.service";

@Injectable({providedIn: 'root'})
export class ChatService {
  private baseUrl = environment.apiUrl;
  stompClient: any;

  msgCallback: any;

  constructor(private http: HttpClient, private authenticationService: LoginService) {
  }


  getChats() {
    return this.http.get<ChatInfo[]>(`${this.baseUrl}/api/chat`, {});
  }

  getChat(id) {
    return this.http.get<ChatInfo>(`${this.baseUrl}/api/chat/${id}`, {});
  }

  getMessages(chatId: string) {
    return this.http.get<ChatMessageInfo[]>(`${this.baseUrl}/api/message?chatId=${chatId}`, {});
  }

  addMessage(req: ChatMessageRequest) {
    return this.http.post<ChatMessageInfo>(`${this.baseUrl}/api/message`, req);
  }


  public connect() {
    console.log("Connecting to ws");

    let ws = new WebSocket("ws://" + location.hostname + ":8080/notifications");
    this.stompClient = Stomp.over(ws);

    const _this = this;

    console.log("token " + this.authenticationService.currentUserValue.token);
    _this.stompClient.connect({"X-AUTH-TOKEN": this.authenticationService.currentUserValue.token},
      function (frame) {
        _this.stompClient.subscribe("/user/queue/message", frame => {
          _this.msgCallback(JSON.parse(frame.body));
        });

      }, this.errorCallBack);
  }

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log("errorCallBack -> " + error);
    let self = this;
    setTimeout(() => {
      self.connect();
    }, 5000);
  }



}


@Injectable({providedIn: 'root'})
export class NewChatService {
  newChatEvent: EventEmitter<any> = new EventEmitter<any>();



}

export class ChatInfo {

  id: string;
  participants: ChatParticipant[];
  lastMessage: ChatMessageInfo;

  constructor(id: string, participants: ChatParticipant[], lastMessage: ChatMessageInfo) {
    this.id = id;
    this.participants = participants;
    this.lastMessage = lastMessage;
  }
}

export class ChatMessageInfo {
  id: string;
  chatId: string;
  author: ChatParticipant;
  message: string;
  time: string;
}

export class ChatMessageRequest {
  chatId: string;
  participantId: number;
  message: string;

  constructor(chatId: string, participantId: number, message: string) {
    this.chatId = chatId;
    this.participantId = participantId;
    this.message = message;
  }
}

export class ChatParticipant {
  userId: number
  name: string;


  constructor(userId: number, name: string) {
    this.userId = userId;
    this.name = name;
  }
}
