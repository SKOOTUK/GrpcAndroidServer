package io.zebless.grpcserver.trip;


import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.zebless.grpcserver.trip.domain.events.JourneyUpdateEvent;
import io.zebless.grpcserver.trip.uglymodel.TripModel;
import io.zelbess.tripupdates.*;
import org.reactivestreams.Publisher;

import java.util.logging.Logger;

public class RxTripService extends RxTripServiceGrpc.TripServiceImplBase {

    private static final Logger logger = Logger.getLogger("SERVICE");

    private TripModel tripModel = new TripModel();

    @Override
    public Single<CreateTripReply> createTrip(Single<CreateTripRequest> request) {

        return request
                .map(it -> tripModel.createATrip(it.getDriverId(), it.getInvitedPassengerId()))
                .map(tripId -> CreateTripReply.newBuilder().setId(tripId).build());
    }

    @Override
    public Flowable<FollowTripReply> followTrip(Single<FollowTripRequest> request) {
        logger.info("FOLLOW REQUEST");
        return request.toFlowable()
                .flatMap((Function<FollowTripRequest, Publisher<JourneyUpdateEvent>>) request1 -> tripModel.followTrip(request1.getId()))
                .map(event ->
                        TripNetworkModel.newBuilder()
                                .setId(event.journeyId)
                                .setEta(event.eta)
                                .setType(event.type)
                                .build())
                .map(tripNetworkModel ->
                        FollowTripReply.newBuilder()
                                .setTrip(tripNetworkModel)
                                .build());
    }

    @Override
    public Flowable<UpdateLocationReply> updateLocation(Flowable<UpdateLocationRequest> request) {
        logger.info("UPDATE LOCATION CALLED ");
        return request
                .doOnNext(next -> {
                    String message = next.getUserId() + " " + next.getLat() + "," + next.getLon();
                    logger.info("Location update received: " + message);
                    tripModel.updateUserLocation(next.getUserId(), next.getLat(), next.getLon());
                })
                .doOnError(throwable -> logger.severe("ERROR ERROR! " + throwable.getMessage()))
                .map(updateLocationRequest -> UpdateLocationReply.newBuilder().setStatusCode(200).build());
    }

    public void onShutDown() {
        tripModel.clear();
    }
}
