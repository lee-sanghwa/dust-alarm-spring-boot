package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
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

    public User findById(int id) {
        Query query = this.em.createQuery("SELECT user FROM User user WHERE user.id = :id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    public User findByUuid(String uuid) {
        Query query = this.em.createQuery("SELECT user FROM User user WHERE user.uuid = :uuid");
        query.setParameter("uuid", uuid);
        return (User) query.getSingleResult();
    }

    public void save(User user) {
        if (user.getId() == null) {
            this.em.persist(user);
        } else {
            this.em.merge(user);
        }
    }

    public Collection<User> findAll() {
        Query query = this.em.createQuery("SELECT user FROM User user");
        return query.getResultList();
    }

    public Collection<User> findAll(Integer pageNo) {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT user FROM User user");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }

    public Integer findCount() {
        Query query = this.em.createQuery("SELECT COUNT(user.id) FROM User user");
        return ((Long) query.getSingleResult()).intValue();
    }

}
