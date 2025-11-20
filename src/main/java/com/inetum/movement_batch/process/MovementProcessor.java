package com.inetum.movement_batch.process;

import com.inetum.movement_batch.dto.ProcessedMovement;
import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.HashMap;
import java.util.Map;

@Component
public class MovementProcessor implements ItemProcessor<MovementCsv, RestMovementResponse> {

    private final RestClient restClient;

    public MovementProcessor(RestClient.Builder restClient) {
        this.restClient = restClient
                .baseUrl("http://localhost:8082/credit-card/api/v1/movement")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Override
    public RestMovementResponse process(MovementCsv item) throws Exception {
        // entity for parsing csv row
        MovementCsv movement = new MovementCsv(
                item.getOperationAmount(),
                item.getOperationType(),
                item.getOperationDescription(),
                item.getOperationCurrency(),
                item.getOrigin(),
                item.getChannel(),
                item.getCreditLineId());

        // printing process state on console
        System.out.println(String.format("MOVEMENT PROCESSED: %s", movement));
        System.out.println("Writing movement data...");

        // seting up request object for rest client
        Map<String, Object> request = new HashMap<>();
        request.put("operationAmount", movement.getOperationAmount());
        request.put("operationtype", movement.getOperationType());
        request.put("operationtDescription", movement.getOperationDescription());
        request.put("operationCurrency", movement.getOperationCurrency());
        request.put("origin", movement.getOrigin());
        request.put("channel", movement.getChannel());
        request.put("creditLineId", movement.getCreditLineId());

        // post request for creating movements extracted from csv
        ProcessedMovement result = restClient.post()
                .uri("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ProcessedMovement.class);

        // parsing data to object for writing new csv
        RestMovementResponse response = new RestMovementResponse();
        response.setResponseCreditAmount(result.getResponseCreditAmount());
        response.setResponseCurrentAmount(result.getResponseCurrentAmount());
        response.setResponseMessage(result.getResponseMessage());
        response.setMovementId(result.getResponse().getMovementId());
        response.setOperationAmount(result.getResponse().getOperationAmount());
        response.setExchangedAmount(result.getResponse().getExchangedAmount());
        response.setExchangeRate(result.getResponse().getExchangeRate());
        response.setCreatedAt(result.getResponse().getCreatedAt());
        response.setOperationtype(result.getResponse().getOperationtype());
        response.setOperationtDescription(result.getResponse().getOperationtDescription());
        response.setOperationCurrency(result.getResponse().getOperationCurrency());
        response.setExchangeCurrency(result.getResponse().getExchangeCurrency());
        response.setOrigin(result.getResponse().getOrigin());
        response.setChannel(result.getResponse().getChannel());
        response.setCreditLineId(result.getResponse().getCreditLineId());

        // printing process end and returning formated resposne object
        System.out.println(String.format("REST CLIENT RESPONSE: %s", response));
        return response;
    }
}
