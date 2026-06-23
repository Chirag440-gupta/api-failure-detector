package com.example.api_monitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;
import com.example.api_monitor.service.ApiService;
import com.example.api_monitor.service.GeminiService;
@RestController
@RequestMapping("/api")
public class ApiController {
    
    private final ApiRepository repo;
    private final ApiService apiService;
    private final GeminiService geminiService;

    public ApiController(ApiRepository repo, ApiService apiService, GeminiService geminiService) {

        this.repo=repo;
        this.apiService=apiService;
        this.geminiService=geminiService;
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

    @DeleteMapping("/{id}")
    public String deleteApi(@PathVariable Long id){
        repo.deleteById(id);
        return "API WITH ID "+ id + " DELETED SUCCESSFULLY";
    }

    @PutMapping("/update/{id}")
    public ApiDetails updateApi(@PathVariable Long id, @RequestBody ApiDetails updatedApi){
        return repo.findById(id).orElseThrow(()-> new RuntimeException("API not found with id: "+ id ));

    }

    @GetMapping("/stats")
    public Map<String, Long> getStats(){
        List<ApiDetails> apis=repo.findAll();
        long upCount = apis.stream().filter(a -> "UP".equalsIgnoreCase(a.getStatus())).count();
        long downCount = apis.stream().filter(a -> "DOWN".equalsIgnoreCase(a.getStatus())).count();
        long totalCount=apis.size();
        Map<String, Long> stats=new HashMap<>();
        stats.put("UP", upCount);
        stats.put("DOWN",downCount);
        stats.put("TOTAL", totalCount);
        return stats;
        }    

        @GetMapping("/ai-summary") 
            public String getAiSummary(){
                return geminiService.getAiSummary();
            }
}
