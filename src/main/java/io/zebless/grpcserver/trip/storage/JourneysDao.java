package io.zebless.grpcserver.trip.storage;

import io.reactivex.Flowable;
import io.zebless.grpcserver.trip.domain.Journey;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class JourneysDao {
    private static final Logger logger = Logger.getLogger("JourneysDao");
    final private Map<Integer, History<Journey>> journeys = new HashMap<>();

    public void saveJourney(Journey journey) {
        if (journeys.containsKey(journey.journeyId)) {
            journeys.remove(journey.journeyId);
        }
        journeys.put(journey.journeyId, new History<>(journey));
        logger.info("Journey " + journey.journeyId + " with driver " + journey.driverId + " created");
    }

    public void updateJourney(Journey journey) {
        journeys.get(journey.journeyId).update(journey);
    }

    public Journey getJourney(int journeyId) {
        return journeys.get(journeyId).getValue();
    }

    public Flowable<Journey> getJourneyUpdates(int journeyId) {
        if (journeys.get(journeyId) == null)
            return Flowable.error(new NullPointerException("No such journey! " + journeyId));
        return journeys.get(journeyId).getUpdates();
    }
}
