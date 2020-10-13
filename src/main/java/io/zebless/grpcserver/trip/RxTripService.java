package io.zebless.grpcserver.trip;


import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Notification;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.zebless.grpcserver.trip.uglymodel.TripModel;
import io.zelbess.tripupdates.*;
import org.reactivestreams.Publisher;

import java.util.logging.Logger;

public class RxTripService extends RxTripServiceGrpc.TripServiceImplBase {

    private static final Logger logger = Logger.getLogger(TripService.class.getName());

    private TripModel tripModel = new TripModel();

    @Override
    public Single<CreateTripReply> createTrip(Single<CreateTripRequest> request) {
        CreateTripReply reply = CreateTripReply.newBuilder().setId(tripModel.createATrip()).build();
        return Single.just(reply);
    }

    @Override
    public Flowable<FollowTripReply> followTrip(Single<FollowTripRequest> request) {
        logger.info("FOLLOW REQUEST");

        return Flowable.combineLatest(
                request.toFlowable(),
                tripModel.tripsHistory.toFlowable(BackpressureStrategy.LATEST),
                (followTripRequest, trip) -> FollowTripReply.newBuilder().setTrip(trip).build()
        );
    }

    @Override
    public Flowable<UpdateLocationReply> updateLocation(Flowable<UpdateLocationRequest> request) {
        return request
                .doOnNext(next -> {
                    String message = next.getUserId() + " " + next.getLat() + "," + next.getLon();
                    logger.info("Location update received: " + message);
                })
                .doOnError(throwable -> logger.severe("ERROR ERROR! " + throwable.getMessage()))
                .map(updateLocationRequest -> UpdateLocationReply.newBuilder().setStatusCode(200).build());
    }

    public void onShutDown() {
        tripModel.clear();
    }
}
