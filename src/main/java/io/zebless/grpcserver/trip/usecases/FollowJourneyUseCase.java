package io.zebless.grpcserver.trip.usecases;

import io.reactivex.Flowable;
import io.zebless.grpcserver.trip.domain.Journey;
import io.zebless.grpcserver.trip.domain.events.EventType;
import io.zebless.grpcserver.trip.domain.events.JourneyUpdateEvent;
import io.zebless.grpcserver.trip.storage.JourneysDao;
import io.zebless.grpcserver.trip.storage.UserDao;

import java.util.logging.Logger;

public class FollowJourneyUseCase {
    private static final Logger logger = Logger.getLogger("FollowJourneyUseCase");
    private UserDao userDao;
    private JourneysDao journeysDao;

    public FollowJourneyUseCase(UserDao userDao, JourneysDao journeysDao) {
        this.userDao = userDao;
        this.journeysDao = journeysDao;
    }

    public Flowable<JourneyUpdateEvent> followJourney(int journeyId) {
        Journey journey = journeysDao.getJourney(journeyId);

        return userDao.getUserUpdates(journey.driverId)
                .map(user -> {
                    int eta = (int) user.lat;
                    logger.info("journey update:" + journeyId + " " + user.userId);
                    return new JourneyUpdateEvent(EventType.DRIVER_LOCATION, journeyId, eta);
                });
    }
}
