package io.zebless.grpcserver.trip;

import io.grpc.stub.StreamObserver;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.zelbess.tripupdates.StartTripRequest;
import io.zelbess.tripupdates.Trip;
import io.zelbess.tripupdates.TripServiceGrpc;
import io.zelbess.tripupdates.TripUpdateReply;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TripService extends TripServiceGrpc.TripServiceImplBase {

    private static final Logger logger = Logger.getLogger(TripService.class.getName());

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onShutDown() {
        compositeDisposable.clear();
    }

    @Override
    public void startTrip(StartTripRequest request, StreamObserver<TripUpdateReply> responseObserver) {

        logger.info("Start Trip");
        compositeDisposable.add(
                getTripUpdates(request.getId())
                        .doOnNext(trip -> {
                            logger.info("Posting trip update: " + trip.getMessage());
                            TripUpdateReply reply = TripUpdateReply.newBuilder().setTrip(trip).build();
                            responseObserver.onNext(reply);
                        })
                        .doOnError(responseObserver::onError)
                        .doOnComplete(responseObserver::onCompleted)
                        .subscribe()
        );
    }

    private Observable<Trip> getTripUpdates(Integer requestId) {
        List<Integer> times = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            times.add(i);
        }

        return Observable.zip(
                Observable.fromIterable(times),
                Observable.interval(3, TimeUnit.SECONDS),
                (integer, aLong) -> integer)
                .map(integer ->
                        Trip.newBuilder()
                                .setId(requestId)
                                .setMessage("Update no: " + integer)
                                .build()
                );
    }

}
