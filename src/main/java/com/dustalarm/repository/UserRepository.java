package com.dustalarm.repository;

import com.dustalarm.model.User;

import java.util.Collection;

public interface UserRepository {
    User findById(int id);

    User findByUuid(String uuid);

    void save(User user);

    Collection<User> findAll();

    Collection<User> findAll(Integer pageNo);

    Integer findCount();

}
