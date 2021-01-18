package com.example.socialnetwork.controller;

import com.example.socialnetwork.controller.api.AddFriendRequest;
import com.example.socialnetwork.controller.api.FriendInfo;
import com.example.socialnetwork.controller.api.GetFriendsResponse;
import com.example.socialnetwork.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author RKomov
 */
@RestController
@RequestMapping("/api/friend")
public class FriendsController {

    FriendService friendService;

    public FriendsController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping
    public ResponseEntity<FriendInfo> addFriend(@Valid @RequestBody AddFriendRequest request) {

        request.setUserId(getCurrentUserProfileId());
        FriendInfo friendInfo = friendService.addFriend(request);

        return ResponseEntity.ok(friendInfo);
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity removeFriend(@NotNull @PathVariable("friendId") Long friendId) {
        friendService.removeFriend(getCurrentUserProfileId(), friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public GetFriendsResponse getFriends(@PathVariable("id") Long userId) {
        return friendService.getFriends(userId);
    }

    @GetMapping
    public GetFriendsResponse getFriends() {
        return friendService.getFriends(getCurrentUserProfileId());
    }

    private Long getCurrentUserProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
