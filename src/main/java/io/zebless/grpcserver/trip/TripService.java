package io.zebless.grpcserver.trip;


import io.grpc.stub.StreamObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.zebless.grpcserver.trip.uglymodel.TripModel;
import io.zelbess.tripupdates.*;

import java.util.logging.Logger;

public class TripService extends TripServiceGrpc.TripServiceImplBase {

    private static final Logger logger = Logger.getLogger(TripService.class.getName());
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TripModel tripModel = new TripModel();

    public void onShutDown() {
        tripModel.clear();
        compositeDisposable.clear();
    }

    @Override
    public void createTrip(CreateTripRequest request, StreamObserver<CreateTripReply> responseObserver) {
        int newTripId = tripModel.createATrip();
        responseObserver.onNext(CreateTripReply.newBuilder().setId(newTripId).build());
        responseObserver.onCompleted();
    }

    @Override
    public void followTrip(FollowTripRequest request, StreamObserver<FollowTripReply> responseObserver) {
        compositeDisposable.add(
                tripModel.tripsHistory
                        .filter(trip -> trip.getId() == request.getId())
                        .subscribe(
                                trip -> {
                                    responseObserver.onNext(FollowTripReply.newBuilder().setTrip(trip).build());
                                    if (trip.getMessage().equalsIgnoreCase("FINISHED")) responseObserver.onCompleted();
                                },
                                throwable -> {
                                    logger.severe("Exception during follow trip:" + throwable.getMessage());
                                })
        );
    }
}
