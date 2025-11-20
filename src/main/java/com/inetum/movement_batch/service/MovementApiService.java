package com.inetum.movement_batch.service;

import com.inetum.movement_batch.dto.CreditCard;
import com.inetum.movement_batch.dto.ProcessedMovement;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class MovementApiService {

    private final RestClient restClient;

    public MovementApiService(RestClient.Builder restClient) {
        this.restClient = restClient
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public CreditCard restClientValidateCreditCard(Map<String, Object> cardNumberRequest) {
        CreditCard creditCardResult = restClient.post()
                .uri("http://localhost:8081/credit-card/api/v1/credit-line/validate-card")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cardNumberRequest)
                .retrieve()
                .body(CreditCard.class);
        return creditCardResult;
    }

    public ProcessedMovement restClientPostMovment(Map<String, Object> request) {
        ProcessedMovement result = restClient.post()
                .uri("http://localhost:8082/credit-card/api/v1/movement/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ProcessedMovement.class);
        return result;
    }
}
