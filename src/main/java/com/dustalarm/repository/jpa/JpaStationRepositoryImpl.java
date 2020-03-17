package com.dustalarm.repository.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import com.dustalarm.model.Station;
import com.dustalarm.repository.StationRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;

@Repository
@Profile("dust-alarm-jpa")
public class JpaStationRepositoryImpl implements StationRepository {

    @PersistenceContext
    private EntityManager em;

    public Station findById(int id) throws DataAccessException {
        Query query = this.em.createQuery("SELECT station FROM Station station WHERE station.id=:id ");
        query.setParameter("id", id);
        return (Station) query.getSingleResult();
    }

    public Station findByLocation(Double latitude, Double longitude) throws DataAccessException {
        Query query = this.em.createNativeQuery("SELECT *, ROUND(ST_DISTANCE(POINT(latitude, longitude), POINT(?1, ?2)) * 100, 1) AS distance FROM stations_station WHERE latitude IS NOT NULL ORDER BY distance", "nearResultMapping");
        query.setParameter(1, latitude);
        query.setParameter(2, longitude);
        return (Station) query.setMaxResults(1).getSingleResult();
    }

    public Station findByName(String name) throws DataAccessException {
        Query query = this.em.createQuery("SELECT DISTINCT station FROM Station station WHERE station.name = :name");
        query.setParameter("name", name);
        return (Station) query.getSingleResult();
    }

    public Collection<Station> findByNameLike(String name) throws DataAccessException {
        Query query = this.em.createQuery("SELECT DISTINCT station FROM Station station WHERE station.name LIKE :name");
        query.setParameter("name", name);
        return query.getResultList();
    }

    public Collection<Station> findByAddressLike(String address) throws DataAccessException {
        Query query = this.em.createQuery("SELECT DISTINCT station FROM Station station WHERE station.name NOT LIKE :address AND station.address Like :address");
        query.setParameter("address", address);
        return query.getResultList();
    }

    public void save(Station station) throws DataAccessException {
        if (station.getId() == null) {
            this.em.persist(station);
        } else {
            this.em.merge(station);
        }
    }

    public Collection<Station> findAll() throws DataAccessException {
        Query query = this.em.createQuery("SELECT station FROM Station station");
        return query.getResultList();
    }

    public Collection<Station> findAll(Integer pageNo) throws DataAccessException {
        int pageSize = 10;
        Query query = this.em.createQuery("SELECT station FROM Station station");
        query.setFirstResult((pageNo.intValue() - 1) * pageSize);
        query.setMaxResults((pageNo.intValue()) * pageSize);
        return query.getResultList();
    }

    public Integer findCount() {
        Query query = this.em.createQuery("SELECT COUNT(station.id) FROM Station station");
        return ((Long) query.getSingleResult()).intValue();
    }
}
