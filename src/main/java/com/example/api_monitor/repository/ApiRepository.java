package com.example.api_monitor.repository;

import com.example.api_monitor.model.ApiDetails;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApiRepository extends JpaRepository<ApiDetails, Long>{
    
}
