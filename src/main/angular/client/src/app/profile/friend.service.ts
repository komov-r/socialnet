import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable({providedIn: 'root'})
export class FriendService {
  private baseUrl = environment.apiUrl;


  constructor(private http: HttpClient) {
  }

  getFriends() {
    return this.http.get<FriendList>(`${this.baseUrl}/api/friend`);
  }

  addFriend(friendId: number) {
    return this.http.post<Friend>(`${this.baseUrl}/api/friend`, {friendId: friendId});
  }

  removeFriend(friend: Friend) {
    return this.http.delete(`${this.baseUrl}/api/friend/${friend.friendId}`,)
  }

}

export class FriendList {
  friends: Friend[];
}

export class Friend {
  friendId: number;
  firstName: string;
  lastName: string;
}
