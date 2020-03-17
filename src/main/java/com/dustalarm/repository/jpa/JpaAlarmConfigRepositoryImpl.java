package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import com.dustalarm.model.AlarmConfig;
import com.dustalarm.repository.AlarmConfigRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Profile("dust-alarm-jpa")
public class JpaAlarmConfigRepositoryImpl implements AlarmConfigRepository {

    @PersistenceContext
    private EntityManager em;

    public AlarmConfig findById(int id) {
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig WHERE alarmConfig.id = :id");
        query.setParameter("id", id);
        return (AlarmConfig) query.getSingleResult();
    }

    public Collection<AlarmConfig> findAll() {
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig ORDER BY alarmConfig.id");
        return query.getResultList();
    }

    public Collection<AlarmConfig> findAll(Integer pageNo) {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig ORDER BY alarmConfig.id");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }

    public void save(AlarmConfig alarmConfig) {
        if (alarmConfig.getId() == null) {
            this.em.persist(alarmConfig);
        } else {
            this.em.merge(alarmConfig);
        }
    }

    public Integer findCount() {
        Query query = this.em.createQuery("SELECT COUNT(alarmConfig.id) FROM AlarmConfig alarmConfig");
        return ((Long) query.getSingleResult()).intValue();
    }
}
