package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "stations_station")
@SqlResultSetMapping(
    name = "nearResultMapping",
    classes = {
        @ConstructorResult(
            targetClass = Station.class,
            columns = {
                @ColumnResult(name = "id", type = Integer.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "address", type = String.class),
                @ColumnResult(name = "latitude", type = Double.class),
                @ColumnResult(name = "longitude", type = Double.class),
                @ColumnResult(name = "date_updated", type = LocalDateTime.class),
                @ColumnResult(name = "distance", type = Double.class)
            }
        )
    }

)
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @UpdateTimestamp
    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @Transient
    private Double distance;

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "station"
    )
    @JsonBackReference(value = "station-concentration")
    private Concentration concentration;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "station"
    )
    @JsonBackReference(value = "station-alarms")
    private Set<Alarm> alarms;

    public Station() {
    }

    public Station(Integer id, String name, String address, Double latitude, Double longitude, LocalDateTime dateUpdated, Double distance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateUpdated = dateUpdated;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Concentration getConcentration() {
        return concentration;
    }

    public void setConcentration(Concentration concentration) {
        this.concentration = concentration;
    }

    public Set<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<Alarm> alarms) {
        this.alarms = alarms;
    }
}
