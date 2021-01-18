package com.example.socialnetwork.service;

import com.example.socialnetwork.controller.api.AddFriendRequest;
import com.example.socialnetwork.controller.api.FriendInfo;
import com.example.socialnetwork.controller.api.GetFriendsResponse;
import com.example.socialnetwork.model.BaseEntity;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.repository.FriendRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author RKomov
 */
@Service
public class FriendService {

    FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public GetFriendsResponse getFriends(Long userId) {
        return new GetFriendsResponse(friendRepository.getFriendsByUserId(userId));
    }

    @Transactional
    public FriendInfo addFriend(AddFriendRequest request) {
        if (request.getFriendId().equals(request.getUserId())) {
            throw new IllegalArgumentException("FriendId should be different from userId");
        }

        return friendRepository.getFriendInfoByUserIdAndFriendId(request.getUserId(), request.getFriendId())
                .orElseGet(() -> {
                    friendRepository.save(new Friend(request.getFriendId(), request.getUserId()));
                    Friend newFriend = friendRepository.save(new Friend(request.getUserId(), request.getFriendId()));

                    return friendRepository.getFriendInfo(newFriend.getId()).orElseThrow(() -> new IllegalStateException());
                });
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        friendRepository.getFriendByUserIdAndFriendId(userId, friendId)
                .map(BaseEntity::getId)
                .ifPresent(friendRepository::deleteById);

        friendRepository.getFriendByUserIdAndFriendId(friendId, userId)
                .map(BaseEntity::getId)
                .ifPresent(friendRepository::deleteById);
    }

}