package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
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

    public Alarm findById(int id) {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm WHERE alarm.id = :id");
        query.setParameter("id", id);
        return (Alarm) query.getSingleResult();
    }

    public Collection<Alarm> findByUserId(int userId) {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm WHERE alarm.user.id = :userId ORDER BY alarm.alarmConfig.id");
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Collection<Alarm> findByTimeActivated(String time) {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm WHERE alarm.time = :time AND alarm.activated = true");
        query.setParameter("time", time);
        return query.getResultList();
    }

    public void save(Alarm alarm) {
        if (alarm.getId() == null) {
            this.em.persist(alarm);
        } else {
            this.em.merge(alarm);
        }
    }

    public Collection<Alarm> findAll() {
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm");
        return query.getResultList();
    }

    public Collection<Alarm> findAll(Integer pageNo) {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT alarm FROM Alarm alarm");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }

    public Integer findCount() {
        Query query = this.em.createQuery("SELECT COUNT(alarm.id) FROM Alarm alarm");
        return ((Long) query.getSingleResult()).intValue();
    }
}
