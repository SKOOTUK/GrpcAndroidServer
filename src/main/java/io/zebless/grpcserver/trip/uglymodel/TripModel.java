package io.zebless.grpcserver.trip.uglymodel;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.zebless.grpcserver.trip.domain.Journey;
import io.zebless.grpcserver.trip.domain.events.JourneyUpdateEvent;
import io.zebless.grpcserver.trip.storage.JourneysDao;
import io.zebless.grpcserver.trip.storage.UserDao;
import io.zebless.grpcserver.trip.usecases.FollowJourneyUseCase;
import io.zebless.grpcserver.trip.usecases.UpdateUserLocationUseCase;

import java.util.Random;

public class TripModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private UserDao userStorage = new UserDao();
    private JourneysDao journeysStorage = new JourneysDao();

    private UpdateUserLocationUseCase updateUserLocation = new UpdateUserLocationUseCase(userStorage);
    private FollowJourneyUseCase followJourney = new FollowJourneyUseCase(userStorage, journeysStorage);


    public int createATrip(int driverId, int invitedPassengerId) {
        int newTripId = new Random().nextInt(100);
        Journey trip = new Journey(newTripId, driverId, invitedPassengerId);
        journeysStorage.saveJourney(trip);
        return newTripId;
    }

    public void updateUserLocation(int userId, double lat, double lon) {
        updateUserLocation.update(userId, lat, lon);
    }

    public Flowable<JourneyUpdateEvent> followTrip(int tripId) {
        return followJourney.followJourney(tripId);
    }


    public void clear() {
        compositeDisposable.clear();
    }
}
