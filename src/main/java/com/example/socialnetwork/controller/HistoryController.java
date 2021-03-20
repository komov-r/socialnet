package com.example.socialnetwork.controller;

import com.example.socialnetwork.model.HistoryItem;
import com.example.socialnetwork.service.HistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author RKomov
 */
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("")
    public Collection<HistoryItem> getHistoryItems() {
        return historyService.getHistoryItems(getCurrentUserProfileId());
    }

    @GetMapping("/{userId}")
    public Collection<HistoryItem> getHistoryItems(@PathVariable("userId") Long userId) {
        return historyService.getHistoryItems(userId);
    }


    private Long getCurrentUserProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
