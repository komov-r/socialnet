import {Component, OnInit} from '@angular/core';
import {ProfileService, ProfilesRequest, UserProfile, UserProfilesList} from "../profile/profile.service";
import {FriendService} from "../profile/friend.service";
import {Observable} from "rxjs";
import {filter} from "rxjs/operators";
import {LoginService} from "../login/login.service";
import {NewChatService} from "../chat/chat.service";

@Component({
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  profiles: Observable<UserProfilesList>
  friendSet: Set<number> = new Set<number>();

  filter: ProfilesRequest = new ProfilesRequest();

  constructor(private profileService: ProfileService,
              private friendService: FriendService,
              private loginService: LoginService,
              private newChatService: NewChatService) {
  }

  ngOnInit() {

    this.friendService.getFriends()
      .pipe(filter(v => v != null))
      .subscribe((v) => v.friends.map(v => v.friendId).forEach((v) => this.friendSet.add(v)));

    this.profiles = this.profileService.getProfiles();
  }


  addFriend(friendId: number) {
    this.friendService.addFriend(friendId).subscribe((f) => {
      this.friendSet.add(f.friendId);
    });
  }

  public isFriend(user: UserProfile): boolean {
    return this.friendSet.has(user.id);
  }

  public isMe(user: UserProfile): boolean {
    return this.loginService.currentUserValue.id == user.id;
  }

  public reload(): void {

    if (this.filter.firstName.length < 3 && this.filter.surname.length < 3) {
      return
    }
    this.profiles = this.profileService.findProfiles(this.filter);
  }

  chat(user: UserProfile) {
    this.newChatService.newChatEvent.emit(user);
  }
}
