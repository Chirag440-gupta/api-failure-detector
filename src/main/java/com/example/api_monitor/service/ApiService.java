package com.example.api_monitor.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.api_monitor.model.ApiDetails;
import com.example.api_monitor.repository.ApiRepository;
@Service
public class ApiService {
    private final ApiRepository repo;
    private final HttpClient client;

    public ApiService(ApiRepository repo){
        this.repo=repo;
        this.client=HttpClient.newHttpClient();
    }
    public List<ApiDetails> getAllApis(){
        return repo.findAll();
    }

    @Scheduled(fixedRate=60000)
    public void checkApis(){
        System.out.println("Scheduler Running...");
        List<ApiDetails> apis=repo.findAll();

        for(ApiDetails api:apis){
            String url=api.getUrl();
            System.out.println(api.getName()+"->"+ url);
            if(url==null || url.isBlank()){
                System.out.println("Ivalid URL");
                continue;
            }
            URI uri=URI.create(url);

            HttpRequest request=HttpRequest.newBuilder().uri(uri).build();
            long start=System.currentTimeMillis();
            try{
              HttpResponse<Void> response=client.send(request, HttpResponse.BodyHandlers.discarding()) ;
              long end=System.currentTimeMillis();
              long responseTime=end-start;
              System.out.println("Response Time For "+ api.getName()+" -> "+ responseTime+"ms");
              api.setResponseTime(responseTime);
              int statusCode=response.statusCode();
               api.setStatusCode(statusCode);
               
               api.setLastChecked(LocalDateTime.now());

                if(statusCode >= 200 && statusCode < 400){
                    api.setStatus("UP");
                }else {
                  api.setStatus("DOWN");
                }
                repo.save(api);
              
               System.out.println("Staus Code For "+ api.getName()+" -> "+statusCode);
            }catch(Exception e){
                api.setStatus("DOWN");
                repo.save(api);
                System.out.println("Error while checking API:"+ api.getName()+"->"+ url);
            }
        }
    }

    public ApiDetails updateApi(Long id, ApiDetails updatedApi){
        ApiDetails api=repo.findById(id).orElseThrow(()-> new RuntimeException("Api not found by id: "+id));
        api.setName(updatedApi.getName());
        api.setUrl(updatedApi.getUrl());
        return repo.save(api);
    }


}
