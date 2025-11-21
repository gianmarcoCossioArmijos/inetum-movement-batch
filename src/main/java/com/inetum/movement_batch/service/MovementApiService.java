package com.inetum.movement_batch.service;

import com.inetum.movement_batch.dto.CreditCard;
import com.inetum.movement_batch.dto.ProcessedMovement;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
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
        Map<String, Object> error = new HashMap<>();
        CreditCard creditCardResult = restClient.post()
                .uri("http://localhost:8081/credit-card/api/v1/credit-line/validate-card")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cardNumberRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    // handle rest client response error
                    error.put("statusCode", res.getStatusCode().toString());
                    error.put("errorBody", res.getBody().toString());
                })
                .body(CreditCard.class);

        // validate rest client response
        if (error.get("statusCode") != null) {
            creditCardResult.setCreditCardIdCode("{ERROR} - " + error.get("statusCode").toString() + " - " + error.get("errorBody"));
            System.out.println(creditCardResult.getCreditCardIdCode().toString());
        }
        System.out.println(creditCardResult.toString());
        return creditCardResult;
    }

    public ProcessedMovement restClientPostMovment(Map<String, Object> request) {
        Map<String, Object> error = new HashMap<>();
        ProcessedMovement result = restClient.post()
                .uri("http://localhost:8082/credit-card/api/v1/movement/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    // handle rest client response error
                    error.put("statusCode", res.getStatusCode().toString());
                    error.put("errorBody", res.getBody().toString());
                })
                .body(ProcessedMovement.class);

        // validate rest client response
        if (error.get("statusCode") != null) {
            result.setResponseMessage("{ERROR} - " + error.get("statusCode").toString() + " - " + error.get("errorBody"));
            System.out.println(result.getResponseMessage().toString());
        }
        System.out.println(result.toString());
        return result;
    }
}
