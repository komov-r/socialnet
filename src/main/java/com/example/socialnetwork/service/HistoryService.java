package com.example.socialnetwork.service;

import com.example.socialnetwork.config.HistoryCacheConfig;
import com.example.socialnetwork.model.HistoryItem;
import com.example.socialnetwork.repository.HistoryItemRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author RKomov
 */
@Service
public class HistoryService {

    private Cache cache;
    private HistoryItemRepository historyItemRepository;

    public HistoryService(CacheManager cache, HistoryItemRepository historyItemRepository) {
        this.cache = cache.getCache(HistoryCacheConfig.HISTORY_CACHE);
        this.historyItemRepository = historyItemRepository;
    }

    public Collection<HistoryItem> getHistoryItems(Long userId) {
        return historyItemRepository.getHistoryItems(userId);
    }
}
