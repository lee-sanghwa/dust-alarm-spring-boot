package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Alarm;
import com.dustalarm.repository.AlarmRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Profile("dust-alarm-jpa")
public class JpaAlarmRepositoryImpl implements AlarmRepository {

    @PersistenceContext
    private EntityManager em;

    public Alarm findById(int id) throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm JOIN FETCH alarm.station WHERE alarm.id = :id");
        query.setParameter("id", id);
        return (Alarm) query.getSingleResult();
    }

    public Collection<Alarm> findByUserId(int userId) throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm JOIN FETCH alarm.station WHERE alarm.user.id = :userId ORDER BY alarm.alarmConfig.id");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Collection<Alarm> findByTimeActivated(String time) throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm JOIN FETCH alarm.station WHERE alarm.time = :time AND alarm.activated = true");
        query.setParameter("time", time);
        return query.getResultList();
    }

    public void save(Alarm alarm) throws DataAccessException {
        if (alarm.getId() == null) {
            this.em.persist(alarm);
        } else {
            this.em.merge(alarm);
        }
    }

    public Collection<Alarm> findAll() throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm JOIN FETCH alarm.station");
        return query.getResultList();
    }

    public Collection<Alarm> findAll(Integer pageNo) throws DataAccessException {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm JOIN FETCH alarm.station");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }
}
