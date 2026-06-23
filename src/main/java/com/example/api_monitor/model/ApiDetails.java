package com.example.api_monitor.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApiDetails {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    private String status;
    private Long responseTime;

    private Integer statusCode;
    private LocalDateTime LastChecked;
    // public Object getStatus() {
    //     throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
    // }

}