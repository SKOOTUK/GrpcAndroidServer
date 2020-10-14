package io.zebless.grpcserver.trip.storage;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.BehaviorSubject;

public class History<Type> {
    private BehaviorSubject<Type> updates;

    public History(Type data) {
        this.updates = BehaviorSubject.createDefault(data);
    }

    public void update(Type data) {
        updates.onNext(data);
    }

    public Flowable<Type> getUpdates() {
        return updates.toFlowable(BackpressureStrategy.BUFFER);
    }

    public Type getValue() {
        return updates.getValue();
    }
}
