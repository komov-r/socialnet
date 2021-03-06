package com.example.socialnetwork.controller.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author RKomov
 */
@Getter
@AllArgsConstructor
public class GetFriendsResponse {
    List<FriendInfo> friends;
}
