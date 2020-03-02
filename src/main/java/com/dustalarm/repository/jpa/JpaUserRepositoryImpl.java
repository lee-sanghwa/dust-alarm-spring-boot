package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.User;
import com.dustalarm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Profile("dust-alarm-jpa")
public class JpaUserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    public User findById(int id) throws DataAccessException {
        Query query = this.em.createQuery("SELECT user FROM User user WHERE user.id = :id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    public User findByUuid(String uuid) throws DataAccessException {
        Query query = this.em.createQuery("SELECT user FROM User user WHERE user.uuid = :uuid");
        query.setParameter("uuid", uuid);
        return (User) query.getSingleResult();
    }

    public void save(User user) throws DataAccessException {
        if (user.getId() == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }

    public Collection<User> findAll() throws DataAccessException {
        Query query = this.em.createQuery("SELECT user FROM User user");
        return query.getResultList();
    }

    public Collection<User> findAll(Integer pageNo) throws DataAccessException {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT user FROM User user");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }


}
