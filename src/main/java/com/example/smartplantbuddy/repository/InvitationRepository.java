package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
}
