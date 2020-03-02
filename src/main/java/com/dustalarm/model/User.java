package com.dustalarm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.dustalarm.rest.JacksonCustomUserDeserializer;
import com.dustalarm.rest.JacksonCustomUserSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@JsonSerialize(using = JacksonCustomUserSerializer.class)
@JsonDeserialize(using = JacksonCustomUserDeserializer.class)
@Table(name = "users_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "push_token")
    private String pushToken;

    @CreationTimestamp
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @JsonIgnore
    @Transient
    private String version;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        mappedBy = "user"
    )
    @JsonBackReference(value = "user-alarms")
    private Set<Alarm> alarms;

    public User() {
    }

    public User(Integer id, String uuid, String pushToken, LocalDateTime createdTime, LocalDateTime updatedTime, String version, Set<Alarm> alarms) {
        this.id = id;
        this.uuid = uuid;
        this.pushToken = pushToken;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.version = version;
        this.alarms = alarms;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime createdTime) {
        this.updatedTime = updatedTime;
    }

    public Set<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(Set<Alarm> alarms) {
        this.alarms = alarms;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
