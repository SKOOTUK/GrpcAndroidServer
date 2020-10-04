package io.zebless.grpcserver.trip.uglymodel;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.zebless.grpcserver.trip.TripService;
import io.zelbess.tripupdates.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TripModel {
    private static final Logger logger = Logger.getLogger(TripService.class.getName());

    public BehaviorSubject<Trip> tripsHistory = BehaviorSubject.create();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public int createATrip() {
        int newTripId = new Random().nextInt(100);
        startTripUpdates(newTripId);
        return newTripId;
    }

    public void clear() {
        compositeDisposable.clear();
    }


    private void startTripUpdates(Integer requestId) {
        List<Integer> times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            times.add(i);
        }

        compositeDisposable.add(Observable.zip(
                Observable.fromIterable(times),
                Observable.interval(3, TimeUnit.SECONDS),
                (integer, aLong) -> integer)
                .map(integer ->
                        Trip.newBuilder()
                                .setId(requestId)
                                .setMessage(getMessage(integer))
                                .build()
                )
                .doOnNext(trip -> {
                    logger.info("Updating trip history:" + requestId);
                    tripsHistory.onNext(trip);
                })
                .subscribe(trip -> {

                }, throwable -> logger.severe(throwable.getMessage())));
    }

    private String getMessage(int updateNo) {
        if (updateNo == 0) return "CREATED";
        if (updateNo < 14) return "Update no: " + updateNo;
        return "FINISHED";
    }
}
