package com.example.socialnetwork.controller;

import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RKomov
 */
@RestController
public class RefreshController {

    RefreshScope refreshScope;

    public RefreshController(RefreshScope refreshScope) {
        this.refreshScope = refreshScope;
    }


    @PostMapping("/refresh")
    public void refresh(@RequestParam String name) {
        refreshScope.refresh(name);
    }
}
