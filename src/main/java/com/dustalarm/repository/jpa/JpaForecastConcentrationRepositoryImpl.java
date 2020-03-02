package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.ForecastConcentration;
import com.dustalarm.repository.ForecastConcentrationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
@Profile("dust-alarm-jpa")
public class JpaForecastConcentrationRepositoryImpl implements ForecastConcentrationRepository {

    @PersistenceContext
    private EntityManager em;

    public ForecastConcentration findByRegionName(String regionName) throws DataAccessException {
        Query query = this.em.createQuery("SELECT fc FROM ForecastConcentration fc WHERE fc.regionName = :regionName");
        query.setParameter("regionName", regionName);
        return (ForecastConcentration) query.getSingleResult();
    }

    public void save(ForecastConcentration fs) throws DataAccessException {
        if (fs.getRegionName() == null) {
            this.em.persist(fs);
        } else {
            this.em.merge(fs);
        }
    }
}
