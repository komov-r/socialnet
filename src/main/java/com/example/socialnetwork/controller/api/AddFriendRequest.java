package com.example.socialnetwork.controller.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author RKomov
 */
@Getter
@Setter
public class AddFriendRequest {
    @JsonIgnore
    private Long userId;
    @NotNull
    private Long friendId;
}
