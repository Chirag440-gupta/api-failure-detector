package com.example.api_monitor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;
import com.example.api_monitor.service.ApiService;
@RestController
@RequestMapping("/api")
public class ApiController {
    
    private final ApiRepository repo;
    private final ApiService apiService;

    public ApiController(ApiRepository repo, ApiService apiService) {

        this.repo=repo;
        this.apiService=apiService;
    }

    @PostMapping("/add")
    public ApiDetails addApi(@RequestBody ApiDetails api) {
        api.setStatus("PENDING");
        api.setResponseTime(0L);
        return repo.save(api);
    }

    @GetMapping("/all") 
    public List<ApiDetails> getAllApis() {
        return repo.findAll();
    }
    @GetMapping("/check")
    public String checkApis() {
        apiService.checkApis();
        return "API check completed";
    }
}
