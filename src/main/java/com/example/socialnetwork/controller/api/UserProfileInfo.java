package com.example.socialnetwork.controller.api;

import com.example.socialnetwork.model.Gender;
import com.example.socialnetwork.model.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

/**
 * @author RKomov
 */
public class UserProfileInfo {
    @JsonIgnore
    UserProfile userProfile;


    public String getSurname() {
        return userProfile.getSurname();
    }

    public String getCity() {
        return userProfile.getCity();
    }

    public LocalDate getBirthDate() {
        return userProfile.getBirthDate();
    }

    public Gender getGender() {
        return userProfile.getGender();
    }

    public String getInterests() {
        return userProfile.getInterests();
    }

    public String getFirstName() {
        return userProfile.getFirstName();
    }

    public Long getId() {
        return userProfile.getId();
    }

    public UserProfileInfo(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
