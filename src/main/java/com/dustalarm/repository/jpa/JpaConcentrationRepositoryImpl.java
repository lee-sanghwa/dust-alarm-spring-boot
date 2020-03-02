package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Concentration;
import com.dustalarm.repository.ConcentrationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Profile("dust-alarm-jpa")
public class JpaConcentrationRepositoryImpl implements ConcentrationRepository {

    @PersistenceContext
    private EntityManager em;

    public Concentration findById(int id) throws DataAccessException {
        Query query = this.em.createQuery("SELECT concentration FROM Concentration concentration WHERE concentration.id = :id");
        query.setParameter("id", id);
        return (Concentration) query.getSingleResult();
    }

    public Concentration findByStationId(int stationId) throws DataAccessException {
        Query query = this.em.createQuery("SELECT concentration FROM Concentration concentration WHERE concentration.station.id = :stationId");
        query.setParameter("stationId", stationId);
        return (Concentration) query.getSingleResult();
    }

    public void save(Concentration concentration) throws DataAccessException {
        if (concentration.getId() == null) {
            this.em.persist(concentration);
        } else {
            this.em.merge(concentration);
        }
    }

    public Collection<Concentration> findAll() throws DataAccessException {
        Query query = this.em.createQuery("SELECT concentration FROM Concentration concentration");
        return query.getResultList();
    }

    public Collection<Concentration> findAll(Integer pageNo) throws DataAccessException {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT concentration FROM Concentration concentration");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }
}
