package com.example.api_monitor.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;
@RestController
@RequestMapping("/api")
public class ApiController {
    
    private final ApiRepository repo;

    public ApiController(ApiRepository repo) {

        this.repo=repo;
    }

    @PostMapping("/add")
    public ApiDetails addApi(@RequestBody ApiDetails api) {
        return repo.save(api);
    }
    @GetMapping("/all") 
    public List<ApiDetails> getAllApis() {
        return repo.findAll();
    }

}
