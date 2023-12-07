package com.example.smartplantbuddy.model;

import com.example.smartplantbuddy.model.enums.Light;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
    private int wateringFrequency; // because it is in hours
    private Light lightAccess;
    private int lightScore;
    @ManyToMany(mappedBy = "plants")
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "plant", cascade = CascadeType.ALL)
    private List<Note> notes;
}
