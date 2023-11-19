package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {

}
