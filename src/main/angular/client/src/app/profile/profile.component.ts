import {Component, OnInit} from '@angular/core';
import {ProfileService, UserProfile} from "./profile.service";
import {filter} from "rxjs/operators";
import {Friend, FriendList, FriendService} from "./friend.service";
import {BehaviorSubject} from "rxjs";
import {HistoryItem, HistoryService} from "../history/history.service";

@Component({
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentProfile: BehaviorSubject<UserProfile> = new BehaviorSubject<UserProfile>(null);
  friends: BehaviorSubject<FriendList> = new BehaviorSubject<FriendList>(null);
  historyItems: BehaviorSubject<HistoryItem[]> = new BehaviorSubject<HistoryItem[]>(null);

  constructor(private profileService: ProfileService,
              private friendService: FriendService,
              private historyService: HistoryService) {
  }

  ngOnInit() {
    this.profileService.getProfile()
      .pipe(filter(v => v != null))
      .subscribe(this.currentProfile)

    this.friendService.getFriends()
      .pipe(filter(v => v != null))
      .subscribe(this.friends)

    this.historyService.getHistory()
      .subscribe(this.historyItems)

  }

  removeFriend(friend: Friend) {
    this.friendService.removeFriend(friend).subscribe(() => {
      this.friends.value.friends.splice(this.friends.value.friends.indexOf(friend, 0), 1);
      this.friends.next(this.friends.value);
    });
  }

  translateEventType(eventType: string) {
    switch (eventType) {
      case "FRIEND_ADD":
        return "friended";
      case "FRIEND_REMOVE":
        return "unfriended";
    }
    return eventType;
  }

}
