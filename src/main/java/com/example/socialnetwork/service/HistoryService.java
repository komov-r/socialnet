package com.example.socialnetwork.service;

import com.example.socialnetwork.model.HistoryItem;
import com.example.socialnetwork.repository.HistoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author RKomov
 */
@Service
public class HistoryService {

        private HistoryItemRepository historyItemRepository;

    public HistoryService(HistoryItemRepository historyItemRepository) {
        this.historyItemRepository = historyItemRepository;
    }

    public Collection<HistoryItem> getHistoryItems(Long userId) {
        return historyItemRepository.getHistoryItems(userId);
    }
}
