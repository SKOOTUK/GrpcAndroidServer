package io.zebless.grpcserver.trip.storage;

import io.reactivex.Flowable;
import io.zebless.grpcserver.trip.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UserDao {

    private static final Logger logger = Logger.getLogger("USER DAO");
    final private Map<Integer, History<User>> users = new HashMap<>();

    public void saveUser(User user) {
        if (users.containsKey(user.userId)) {
            users.remove(user.userId);
        }
        users.put(user.userId, new History<>(user));
    }

    public void updateUserLocation(int userId, double lat, double lon) {
        if (!users.containsKey(userId)) {
            logger.info("Updating user location: no user, creating one");
            users.put(userId, new History<>(new User(userId, lat, lon)));
            return;
        }

        User toUpdate = users.get(userId).getValue();
        toUpdate.lat = lat;
        toUpdate.lon = lon;
        users.get(userId).update(toUpdate);
        logger.info("Updating user location: " + userId);
    }

    public Flowable<User> getUserUpdates(int userId) {
        logger.info("Requested user updates");
        return users.get(userId).getUpdates();
    }
}
