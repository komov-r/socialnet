package com.example.socialnetwork.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author RKomov
 */
@Getter
@Setter
public class UserProfile extends BaseEntity {

    private String username;

    private String password;

    private String firstName;

    private String surname;

    private String city;

    private LocalDate birthDate;

    private Gender gender;

    private String interests;

    public String getFullName() {
        return firstName + " " + surname;
    }

    public UserProfile() {
    }

    public UserProfile(String username, String password, String firstName, String surname, String city, LocalDate birthDate, Gender gender, String interests) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.city = city;
        this.birthDate = birthDate;
        this.gender = gender;
        this.interests = interests;
    }

    public UserProfile(Long id,
                       String username,
                       String password,
                       String firstName,
                       String surname,
                       String city,
                       LocalDate birthDate,
                       Gender gender,
                       String interests) {
        super(id);
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.city = city;
        this.birthDate = birthDate;
        this.gender = gender;
        this.interests = interests;
    }

    public UserProfile(Long id, UserProfile userProfile) {
        this(id,
                userProfile.getUsername(),
                userProfile.getPassword(),
                userProfile.getFirstName(),
                userProfile.getSurname(),
                userProfile.getCity(),
                userProfile.getBirthDate(),
                userProfile.getGender(),
                userProfile.getInterests()
        );
    }
}
