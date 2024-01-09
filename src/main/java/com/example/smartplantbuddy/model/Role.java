package com.example.smartplantbuddy.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Represents a security role within the application.
 * Roles are used to grant or restrict access to certain functionality within the system.
 * Each role has a unique identifier and a name that describes the role.
 * Example roles might include "ADMIN", "USER", and "GUEST".
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table
@Data
public class Role {
    /**
     * The unique identifier for each role.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the role.
     * This describes the level of access or responsibility associated with the role.
     */
    private String name;
}
