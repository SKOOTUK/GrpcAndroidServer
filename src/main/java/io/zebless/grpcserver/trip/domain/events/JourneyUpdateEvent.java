package io.zebless.grpcserver.trip.domain.events;

public class JourneyUpdateEvent {
    public String type;
    public int journeyId;
    public int eta;

    public JourneyUpdateEvent(EventType type, int journeyId, int eta) {
        this.journeyId = journeyId;
        this.type = type.name();
        this.eta = eta;
    }
}


