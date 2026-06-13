package com.example.api_monitor.controller;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
