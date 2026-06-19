package com.example.api_monitor.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

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

    public void checkApis(){
        List<ApiDetails> apis=repo.findAll();

        for(ApiDetails api:apis){
            String url=api.getUrl();
            System.out.println(api.getName()+"->"+ url);
            URI uri=URI.create(url);

            HttpRequest request=HttpRequest.newBuilder().uri(uri).build();
            try{
              HttpResponse<Void> response=client.send(request, HttpResponse.BodyHandlers.discarding()) ;
                int statusCode=response.statusCode();
                if(statusCode==200){
                    api.setStatus("UP");
                }else {
                  api.setStatus("DOWN");
                }
                repo.save(api);
              
               System.out.println("Staus Code For"+ api.getName()+"->"+statusCode);
            }catch(Exception e){
                api.setStatus("DOWN");
                repo.save(api);
                System.out.println("Error while checking API:"+ api.getName()+"->"+ url);
            }
        }
    }
}
