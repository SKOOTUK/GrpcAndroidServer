package io.zebless.grpcserver.trip.domain;

public class User {
    public int userId;
    public double lat;
    public double lon;

    public User(int userId, double lat, double lon) {
        this.userId = userId;
        this.lat = lat;
        this.lon = lon;
    }
}
