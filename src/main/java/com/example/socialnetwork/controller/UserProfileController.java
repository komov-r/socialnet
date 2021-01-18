package com.example.socialnetwork.controller;

import com.example.socialnetwork.controller.api.GetUserProfileRequest;
import com.example.socialnetwork.controller.api.GetUserProfileResponse;
import com.example.socialnetwork.controller.api.RegisterRequest;
import com.example.socialnetwork.controller.api.UpdateProfileRequest;
import com.example.socialnetwork.controller.api.UserProfileInfo;
import com.example.socialnetwork.model.UserProfile;
import com.example.socialnetwork.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author RKomov
 */
@RestController
@RequestMapping(path = "/api/")
public class UserProfileController {

    private ProfileService profileService;

    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileInfo> getProfile() {
        return profileService.getById(getCurrentUserProfileId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileInfo> getProfile(@NotNull @PathVariable("id") Long id) {
        return profileService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    public ResponseEntity updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest) {

        updateProfileRequest.setId(getCurrentUserProfileId());

        profileService.updateProfile(updateProfileRequest);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/profiles")
    public GetUserProfileResponse findUsers(GetUserProfileRequest request) {
        return profileService.findUserProfiles(request);
    }

    @PostMapping("/profile")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {

        if (registerRequest.getFirstName().equals("ErrorTest")) {
            throw new IllegalArgumentException("test");
        }

        profileService.addUserProfile(registerRequest);

        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }


}
