import {Component, OnInit} from '@angular/core';
import {ProfileService, UserProfile} from "./profile.service";
import {filter} from "rxjs/operators";
import {Friend, FriendList, FriendService} from "./friend.service";
import {BehaviorSubject, Observable} from "rxjs";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentProfile: BehaviorSubject<UserProfile> = new BehaviorSubject<UserProfile>(null);
  friends: BehaviorSubject<FriendList> = new BehaviorSubject<FriendList>(null);

  constructor(private profileService: ProfileService, private friendService: FriendService) {
  }

  ngOnInit() {
    this.profileService.getProfile()
      .pipe(filter(v => v != null))
      .subscribe(this.currentProfile)

    this.friendService.getFriends()
      .pipe(filter(v => v != null))
      .subscribe(this.friends)

  }

  removeFriend(friend: Friend) {
    this.friendService.removeFriend(friend).subscribe(() => {
      this.friends.value.friends.splice(this.friends.value.friends.indexOf(friend, 0), 1);
      this.friends.next(this.friends.value);
    });
  }

}
