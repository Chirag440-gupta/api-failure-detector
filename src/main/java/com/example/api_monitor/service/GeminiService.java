package com.example.api_monitor.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;

@Service
public class GeminiService {
    private final ApiRepository repo;
    public GeminiService(ApiRepository repo){
        this.repo=repo;
    }


    @Value("${gemini.api.key}")
     private String apiKey;

     private final RestTemplate restTemplate= new RestTemplate();

    public String getAiSummary(){
        List<ApiDetails> apis= repo.findAll();
        

        StringBuilder prompt=new StringBuilder();
             prompt.append(
    """
You are an API monitoring assistant.
Analyze the APIs and provide:
1. Overall system health.
2. APIs that are DOWN.
3. Slow APIs (>500 ms).
4. Any unusual observations.

API Data:
"""
);

        for(ApiDetails api:apis){
            if(api.getName() ==null || api.getUrl() == null){
            continue;
            }
               prompt.append("API Name: ").append(api.getName())
          .append("\nURL: ").append(api.getUrl())
          .append("\nStatus: ").append(api.getStatus())
          .append("\nStatus Code: ").append(api.getStatusCode())
          .append("\nResponse Time: ").append(api.getResponseTime()).append(" ms")
          .append("\nLast Checked: ").append(api.getLastChecked())
          .append("\n-----------------------------------\n");


        }
            String finalPrompt = prompt.toString();
            String url="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            Map<String, Object> textPart = Map.of(
                "text", finalPrompt
            );
            Map<String,Object> part = Map.of(
                "parts",List.of(textPart)
            );
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(part)
            );

             try  { 
                String response= restTemplate.postForObject(
                url,
                requestBody,
                String.class
            );
             return response;
        } 
       catch(Exception e){
           e.printStackTrace();
           return e.getClass().getName() + " : " + e.getMessage();
        }
            // return "Gemini API call next step";   
    }

}


