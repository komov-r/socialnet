package com.example.socialnetwork.model;

import lombok.Getter;

/**
 * @author RKomov
 */
@Getter
public class Friend extends BaseEntity {

    private final Long userId;
    private final Long friendId;

    public Friend(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public Friend(Long id, Long userId, Long friendId) {
        super(id);
        this.userId = userId;
        this.friendId = friendId;
    }
}
