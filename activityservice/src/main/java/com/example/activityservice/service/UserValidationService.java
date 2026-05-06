package com.example.activityservice.service;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebClient;

    public Boolean validateUser(String userId){
        log.info("calling validation user api for userid {}",userId);

        try {

            return userServiceWebClient.get().uri("api/users/{userId}/validate",userId).retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

        } catch (WebClientResponseException e) {
            if(e.getStatusCode() == HttpStatus.NOT_FOUND){
                throw new RuntimeException("user not found:"+userId);
            }
            else if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException("invalid request:"+userId);
            }
            throw new RuntimeException("user not found:"+userId);

        }

    }
}
