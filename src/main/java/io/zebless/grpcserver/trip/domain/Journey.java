package io.zebless.grpcserver.trip.domain;

public class Journey {
    public int journeyId;
    public int driverId;
    public int passengerId;

    public Journey(int journeyId, int driverId, int passengerId) {
        this.journeyId = journeyId;
        this.driverId = driverId;
        this.passengerId = passengerId;
    }
}
