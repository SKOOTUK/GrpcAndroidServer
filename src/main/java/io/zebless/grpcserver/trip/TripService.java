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
        int newTripId = tripModel.createATrip(request.getDriverId(), request.getInvitedPassengerId());
        responseObserver.onNext(CreateTripReply.newBuilder().setId(newTripId).build());
        responseObserver.onCompleted();
    }
}
