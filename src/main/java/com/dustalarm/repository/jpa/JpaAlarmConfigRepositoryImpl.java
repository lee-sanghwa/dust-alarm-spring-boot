package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
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

    public AlarmConfig findById(int id) throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig WHERE alarmConfig.id = :id");
        query.setParameter("id", id);
        return (AlarmConfig) query.getSingleResult();
    }

    public Collection<AlarmConfig> findAll() throws DataAccessException {
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig ORDER BY alarmConfig.id");
        return query.getResultList();
    }

    public Collection<AlarmConfig> findAll(Integer pageNo) throws DataAccessException {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT alarmConfig FROM AlarmConfig alarmConfig ORDER BY alarmConfig.id");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }

    public void save(AlarmConfig alarmConfig) throws DataAccessException {
        if (alarmConfig.getId() == null) {
            this.em.persist(alarmConfig);
        } else {
            this.em.merge(alarmConfig);
        }
    }
}
