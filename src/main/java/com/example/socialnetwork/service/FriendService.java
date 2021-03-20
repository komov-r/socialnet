package com.example.socialnetwork.service;

import com.example.socialnetwork.controller.api.AddFriendRequest;
import com.example.socialnetwork.controller.api.FriendInfo;
import com.example.socialnetwork.controller.api.GetFriendsResponse;
import com.example.socialnetwork.event.HistoryEventRequest;
import com.example.socialnetwork.model.BaseEntity;
import com.example.socialnetwork.model.Friend;
import com.example.socialnetwork.model.HistoryEvent;
import com.example.socialnetwork.model.HistoryEventType;
import com.example.socialnetwork.model.ObjectType;
import com.example.socialnetwork.repository.FriendRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author RKomov
 */
@Service
public class FriendService {

    private FriendRepository friendRepository;
    private ApplicationEventPublisher eventPublisher;

    public FriendService(FriendRepository friendRepository, ApplicationEventPublisher eventPublisher) {
        this.friendRepository = friendRepository;
        this.eventPublisher = eventPublisher;
    }

    public GetFriendsResponse getFriends(Long userId) {
        return new GetFriendsResponse(friendRepository.getFriendsByUserId(userId));
    }

    @Transactional
    public FriendInfo addFriend(AddFriendRequest request) {
        if (request.getFriendId().equals(request.getUserId())) {
            throw new IllegalArgumentException("FriendId should be different from userId");
        }

        FriendInfo friendInfo = friendRepository.getFriendInfoByUserIdAndFriendId(request.getUserId(), request.getFriendId())
                .orElseGet(() -> {
                    friendRepository.save(new Friend(request.getFriendId(), request.getUserId()));
                    Friend newFriend = friendRepository.save(new Friend(request.getUserId(), request.getFriendId()));

                    return friendRepository.getFriendInfo(newFriend.getId()).orElseThrow(() -> new IllegalStateException());
                });


        sendHistoryEvents(friendInfo.getUserId(), friendInfo.getFriendId(), HistoryEventType.FRIEND_ADD);

        return friendInfo;
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        friendRepository.getFriendByUserIdAndFriendId(userId, friendId)
                .map(BaseEntity::getId)
                .ifPresent(friendRepository::deleteById);

        friendRepository.getFriendByUserIdAndFriendId(friendId, userId)
                .map(BaseEntity::getId)
                .ifPresent(friendRepository::deleteById);

        sendHistoryEvents(userId, friendId, HistoryEventType.FRIEND_REMOVE);
    }

    private void sendHistoryEvents(Long userId, Long friendId, HistoryEventType friendAdd) {
        eventPublisher.publishEvent(new HistoryEventRequest(
                new HistoryEvent(userId, null, friendId, null, ObjectType.USER, friendAdd, LocalDate.now()),
                1000
        ));
        eventPublisher.publishEvent(new HistoryEventRequest(
                new HistoryEvent(friendId, null, userId, null, ObjectType.USER, friendAdd, LocalDate.now()),
                1000
        ));
    }

}