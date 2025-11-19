package com.inetum.movement_batch.process;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import com.inetum.movement_batch.entity.MovementProcessed;
import java.time.LocalDateTime;

@Service
public class MovementProcessor implements ItemProcessor<MovementCsv, MovementProcessed> {

    private final RestClient restClient;
    private final String endpoint;

    public CreditLineApiService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
            .baseUrl("http://localhost:8082/credit-card/api/v1/movement/create")
            .defaultHeader("Accept", "application/json")
            .build();
    }

    @Override
    public RestMovementResponse process(MovementCsv item) throws Exception {
        LocalDateTime time = LocalDateTime.now();
        String processStatusCode = "10";
        String processCode = "OK"
        String process = "se ejecuto";

        RestMovementResponse response = restClient.post()
            .uri(endpoint + "/create")
            .contentType(MediaType.APPLICATION_JSON) 
            .body(item)
            .retrieve()
            .doby(RestMovementResponse.class)
            .block();

        return response;
    }
}
