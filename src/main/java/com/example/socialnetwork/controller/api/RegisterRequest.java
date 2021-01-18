package com.example.socialnetwork.controller.api;

import com.example.socialnetwork.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @author RKomov
 */
@Getter
@Setter
public class RegisterRequest {

    @NotEmpty
    String username;
    @NotEmpty
    String firstName;
    @NotEmpty
    String surname;
    @NotEmpty
    String password;


    private String city;

    private LocalDate birthDate;

    private Gender gender;

    private String interests;

}
