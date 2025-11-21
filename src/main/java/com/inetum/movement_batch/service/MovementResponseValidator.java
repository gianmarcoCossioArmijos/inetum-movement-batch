package com.inetum.movement_batch.service;

import com.inetum.movement_batch.dto.ProcessedMovement;
import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MovementResponseValidator {

    public RestMovementResponse reponseMapping(ProcessedMovement result, MovementCsv movement) {

        RestMovementResponse response = new RestMovementResponse();
        if (!result.getResponseMessage().contains("{ERROR}")) {
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
            response.setResponseCode("02");
            response.setResponseStatus("PROCESSED");
        } else {
            response.setOperationAmount(movement.getOperationAmount());
            response.setOperationtype(movement.getOperationType());
            response.setOperationtDescription(movement.getOperationDescription());
            response.setOperationCurrency(movement.getOperationCurrency());
            response.setOrigin(movement.getOrigin());
            response.setChannel(movement.getChannel());
            response.setResponseCode("00");
            response.setResponseStatus("NOT PROCESSED");
            response.setResponseMessage("Operation failed to process: ---> " + result.getResponseMessage().toString());
        }
        return response;
    }
}
