package com.example.socialnetwork.controller.api;

import lombok.Getter;

/**
 * @author RKomov
 */
@Getter
public class FriendInfo {

    Long userId;
    Long friendId;
    String firstName;
    String lastName;


    public FriendInfo(Long userId, Long friendId, String firstName, String lastName) {
        this.userId = userId;
        this.friendId = friendId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
