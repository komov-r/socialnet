package com.example.socialnetwork.controller.api;

import com.example.socialnetwork.model.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @author RKomov
 */
@Getter
@Setter
public class UpdateProfileRequest {

    @JsonIgnore
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String surname;

    private String city;

    private LocalDate birthDate;

    private Gender gender;

    private String interests;

}
