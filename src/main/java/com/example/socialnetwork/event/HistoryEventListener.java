package com.example.socialnetwork.event;

import com.example.socialnetwork.controller.api.FriendInfo;
import com.example.socialnetwork.model.HistoryEventType;
import com.example.socialnetwork.model.HistoryItem;
import com.example.socialnetwork.repository.FriendRepository;
import com.example.socialnetwork.repository.HistoryItemRepository;
import com.example.socialnetwork.repository.UserProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RKomov
 */
@Component
public class HistoryEventListener {

    private final FriendRepository friendRepository;
    private final HistoryItemRepository historyItemRepository;
    private final UserProfileRepository profileRepository;
    private final ApplicationEventPublisher eventPublisher;

    public HistoryEventListener(FriendRepository friendRepository,
                                HistoryItemRepository historyItemRepository,
                                UserProfileRepository profileRepository,
                                ApplicationEventPublisher eventPublisher) {
        this.friendRepository = friendRepository;
        this.historyItemRepository = historyItemRepository;
        this.profileRepository = profileRepository;
        this.eventPublisher = eventPublisher;
    }


//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @EventListener
//    @Async
    public void handleEvent(HistoryEventRequest historyEventRequest) {

        List<Long> friendsByUserId = produceRecipientsList(historyEventRequest);

        if (friendsByUserId.isEmpty()) {
            return;
        }

        enrichEvent(historyEventRequest);

        List<HistoryItem> historyItems = friendsByUserId.stream()
                .map(v -> new HistoryItem(null, v, historyEventRequest.getEvent()))
                .collect(Collectors.toList());

        historyItemRepository.save(historyItems);
    }

    private List<Long> produceRecipientsList(HistoryEventRequest historyEventRequest) {
        List<Long> recipients = new ArrayList<>();

        if (historyEventRequest.getEvent().getEventType().equals(HistoryEventType.FRIEND_REMOVE)) {
            recipients.add(historyEventRequest.getEvent().getObjectId());
        }

        Long fromId;
        if (historyEventRequest.getMinId() == null) {
            fromId = friendRepository.getMinFriendId(historyEventRequest.event.getUserId());
        } else {
            fromId = historyEventRequest.getMinId();
        }

        if (fromId == null) {
            return recipients;
        }

        List<FriendInfo> friendsByUserId = friendRepository.getFriendsByUserId(historyEventRequest.getEvent().getUserId(), fromId, historyEventRequest.getLimit());

        if (friendsByUserId.isEmpty()) {
            return Collections.emptyList();
        }

        if (friendsByUserId.size() > historyEventRequest.limit) {
            FriendInfo max = friendsByUserId.get(friendsByUserId.size() - 1);
            friendsByUserId.remove(max);

            eventPublisher.publishEvent(new HistoryEventRequest(historyEventRequest.event, historyEventRequest.limit, max.getFriendId()));
        }

        friendsByUserId.stream()
                .map(FriendInfo::getFriendId)
                .forEach(recipients::add);
        return recipients;
    }

    private void enrichEvent(HistoryEventRequest historyEventRequest) {

        profileRepository.getById(historyEventRequest.getEvent().getObjectId()).ifPresent(v -> {
            historyEventRequest.getEvent().setObjectDescription(v.getFullName());
        });

        profileRepository.getById(historyEventRequest.getEvent().getUserId()).ifPresent(v -> {
            historyEventRequest.getEvent().setUserDescription(v.getFullName());
        });
    }

}
