package com.example.socialnetwork.controller.api;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author RKomov
 */
@Getter
@Setter
public class LoginRequest {
    @NotEmpty
    String login;
    @NotEmpty
    String password;
}
