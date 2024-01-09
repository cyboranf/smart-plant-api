package com.example.smartplantbuddy.repository;

import com.example.smartplantbuddy.model.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByPlantIdOrderByIdDesc(Long plantId);}
