package com.example.socialnetwork.service;

import com.example.socialnetwork.controller.api.GetUserProfileRequest;
import com.example.socialnetwork.controller.api.GetUserProfileResponse;
import com.example.socialnetwork.controller.api.RegisterRequest;
import com.example.socialnetwork.controller.api.UpdateProfileRequest;
import com.example.socialnetwork.controller.api.UserProfileInfo;
import com.example.socialnetwork.model.UserProfile;
import com.example.socialnetwork.repository.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author RKomov
 */
@Service
public class ProfileService {

    UserProfileRepository repository;

    PasswordEncoder passwordEncoder;

    public ProfileService(UserProfileRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfile addUserProfile(RegisterRequest registerRequest) {

        UserProfile userProfile = regRequestToUserProfile(registerRequest);
        return repository.insert(userProfile);
    }

    public Optional<UserProfileInfo> getById(Long id) {
        return repository.getById(id).map(this::userProfileToInfo);
    }

    public GetUserProfileResponse findUserProfiles(GetUserProfileRequest request) {
        return new GetUserProfileResponse(repository.findUserProfiles(request).stream()
                .map(this::userProfileToInfo)
                .collect(Collectors.toList()));
    }

    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        repository.updateProfile(updateProfileRequest);
    }

    private UserProfile regRequestToUserProfile(RegisterRequest registerRequest) {
        UserProfile userProfile = new UserProfile();

        userProfile.setUsername(registerRequest.getUsername());
        userProfile.setFirstName(registerRequest.getFirstName());
        userProfile.setSurname(registerRequest.getSurname());
        userProfile.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userProfile.setBirthDate(registerRequest.getBirthDate());
        userProfile.setCity(registerRequest.getCity());
        userProfile.setInterests(registerRequest.getInterests());
        userProfile.setGender(registerRequest.getGender());
        return userProfile;
    }

    private UserProfileInfo userProfileToInfo(UserProfile userProfile) {
        return new UserProfileInfo(userProfile);
    }
}
