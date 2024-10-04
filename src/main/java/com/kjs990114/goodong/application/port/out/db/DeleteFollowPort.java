package com.kjs990114.goodong.application.port.out.db;

public interface DeleteFollowPort {
    void delete(Long followerId, Long followeeId);
}
