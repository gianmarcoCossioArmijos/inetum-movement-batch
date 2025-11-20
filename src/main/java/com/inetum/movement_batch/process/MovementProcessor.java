package com.inetum.movement_batch.process;

import com.inetum.movement_batch.service.MovementApiService;
import com.inetum.movement_batch.dto.CreditCard;
import com.inetum.movement_batch.dto.ProcessedMovement;
import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import com.inetum.movement_batch.service.MovementResponseValidator;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Component
public class MovementProcessor implements ItemProcessor<MovementCsv, RestMovementResponse> {

    private final MovementApiService apiService;
    private final MovementResponseValidator validator;

    @Override
    public RestMovementResponse process(MovementCsv item) throws Exception {
        // entity for parsing csv row
        System.out.println("Writing movement data...");
        MovementCsv movement = new MovementCsv(
                item.getOperationAmount(),
                item.getOperationType(),
                item.getOperationDescription(),
                item.getOperationCurrency(),
                item.getOrigin(),
                item.getChannel(),
                item.getCreditCardNumber());

        // printing process state on console
        System.out.println(String.format("MOVEMENT PROCESSED: %s", movement));
        System.out.println("Sending request to validate card number...");

        // post request for validate card number and get credit line id
        Map<String, Object> cardNumberRequest = new HashMap<>();
        cardNumberRequest.put("creditCardNumber", movement.getCreditCardNumber());
        cardNumberRequest.put("creditLineCurrency", movement.getOperationCurrency());
        CreditCard creditCardResult = apiService.restClientValidateCreditCard(cardNumberRequest);

        // setting credit line id to process
        int creditLineId = Integer.parseInt(creditCardResult.getCreditCardIdCode());
        System.out.println(String.format("CREDIT LINE ID GETTED: %s", creditCardResult));

        // seting up request object for rest client
        Map<String, Object> request = new HashMap<>();
        request.put("operationAmount", movement.getOperationAmount());
        request.put("operationtype", movement.getOperationType());
        request.put("operationtDescription", movement.getOperationDescription());
        request.put("operationCurrency", movement.getOperationCurrency());
        request.put("origin", movement.getOrigin());
        request.put("channel", movement.getChannel());
        request.put("creditLineId", creditLineId);

        // post request for creating movements extracted from csv
        ProcessedMovement result = apiService.restClientPostMovment(request);

        // validating and parsing data to object for writing new csv
        RestMovementResponse response = validator.reponseMapping(result, movement);

        // printing process end and returning formated resposne object
        System.out.println(String.format("REST CLIENT RESPONSE: %s", response));
        return response;
    }
}
