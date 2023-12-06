package com.example.smartplantbuddy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    private String name;
}
