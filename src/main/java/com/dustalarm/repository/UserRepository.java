package com.dustalarm.repository;

import org.springframework.dao.DataAccessException;
import com.dustalarm.model.User;

import java.util.Collection;

public interface UserRepository {
    User findById(int id) throws DataAccessException;

    User findByUuid(String uuid) throws DataAccessException;

    void save(User user) throws DataAccessException;

    Collection<User> findAll() throws DataAccessException;

    Collection<User> findAll(Integer pageNo) throws DataAccessException;

}
