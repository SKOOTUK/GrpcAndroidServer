package io.zebless.grpcserver.trip.usecases;

import io.zebless.grpcserver.trip.storage.UserDao;

import java.util.logging.Logger;

public class UpdateUserLocationUseCase {
    private static final Logger logger = Logger.getLogger("UpdateUserLocationUseCase");
    private UserDao userDao;

    public UpdateUserLocationUseCase(UserDao userDao) {
        this.userDao = userDao;
    }

    public void update(int userId, double lat, double lon) {
        logger.info("Update on: " + userId + " lat: " + lat);
        userDao.updateUserLocation(userId, lat, lon);
    }
}
