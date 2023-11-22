package com.example.smartplantbuddy.model;

import com.example.smartplantbuddy.model.enums.Light;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
@Data
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageUrl;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime wateringFrequency;
    private Light lightAccess;
    private int lightScore;

    @ManyToMany(mappedBy = "plants")
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getWateringTime() {
        return wateringTime;
    }

    public void setWateringTime(LocalDateTime wateringTime) {
        this.wateringTime = wateringTime;
    }

    public LocalDateTime getWateringFrequency() {
        return wateringFrequency;
    }

    public void setWateringFrequency(LocalDateTime wateringFrequency) {
        this.wateringFrequency = wateringFrequency;
    }

    public Light getLightAccess() {
        return lightAccess;
    }

    public void setLightAccess(Light lightAccess) {
        this.lightAccess = lightAccess;
    }

    public int getLightScore() {
        return lightScore;
    }

    public void setLightScore(int lightScore) {
        this.lightScore = lightScore;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
