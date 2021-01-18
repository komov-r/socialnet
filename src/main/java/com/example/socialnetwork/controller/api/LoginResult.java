package com.example.socialnetwork.controller.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author RKomov
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResult {

    Long id;
    String token;
}
