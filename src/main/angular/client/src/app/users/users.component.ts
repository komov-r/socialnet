import {Component, OnInit} from '@angular/core';
import {ProfileService, UserProfile, UserProfilesList} from "../profile/profile.service";
import {FriendList, FriendService} from "../profile/friend.service";
import {BehaviorSubject, Observable} from "rxjs";
import {filter} from "rxjs/operators";
import {LoginService} from "../login/login.service";

@Component({
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  profiles: Observable<UserProfilesList>
  friendSet: Set<number> = new Set<number>();

  constructor(private profileService: ProfileService, private friendService: FriendService, private loginService: LoginService) {
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


}
