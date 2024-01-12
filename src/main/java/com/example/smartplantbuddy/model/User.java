package com.example.smartplantbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user of the application.
 * Users have unique login credentials and can be assigned multiple roles that define their access rights.
 * Users can also be associated with multiple plants, indicating the plants they are responsible for or interested in.
 * This entity links users to their roles and plants for access control and personalized user experiences.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "user")
@Data
public class User {
    /**
     * The unique identifier for each user.
     * This ID is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The login name for the user.
     * This is used along with the password to authenticate the user.
     */
    private String login;

    /**
     * The password for the user.
     * It is recommended that the password be stored in a secure, encrypted format.
     */
    private String password;

    /**
     * The set of roles assigned to the user.
     * This determines what the user is allowed to do within the application.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * The set of plants associated with the user.
     * This indicates which plants the user is taking care of or tracking.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_plant",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "plant_id")
    )
    private Set<Plant> plants;

    /**
     * The set of users who are friends with this user.
     * This represents a many-to-many relationship where users can have multiple friends.
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<User> friends = new HashSet<>();

    /**
     * The set of invitations received by the user.
     * This field represents all the friend invitations that this user has received from other users.
     * It is mapped as a one-to-many relationship where one user can receive many invitations.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "invitee")
    private Set<Invitation> receivedInvitations;

    /**
     * The set of invitations sent by the user.
     * This field represents all the friend invitations that this user has sent to other users.
     * It is mapped as a one-to-many relationship where one user can send many invitations.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "inviter")
    private Set<Invitation> sentInvitations;
    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return id != null && id.equals(other.getId());
    }

}
