package com.inetum.movement_batch.service;

import com.inetum.movement_batch.dto.ProcessedMovement;
import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.dto.RestMovementResponseDetails;
import com.inetum.movement_batch.entity.MovementCsv;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovmentResponseValidatorTest {

    @Mock
    private MovementResponseValidator validator;

    @Test
    public void reponseMappingTest() {
        MovementCsv movement = new MovementCsv(
                200.00,
                "DEBIT",
                "purchase payment",
                "SOL",
                "FALABELLA REAIL S.A.C.",
                "ECOMMERCE",
                "0000-0000-0000-0001");

        RestMovementResponseDetails detail  = new RestMovementResponseDetails();
        detail.setMovementId(1);
        detail.setOperationAmount(200.00);
        detail.setExchangedAmount(200.00);
        detail.setExchangeRate(1.00);
        detail.setCreatedAt("2025-11-20 01:01:20.745378");
        detail.setOperationtype("DEBIT");
        detail.setOperationtDescription("purchase payment");
        detail.setOperationCurrency("SOL");
        detail.setExchangeCurrency("SOL");
        detail.setOrigin("FALABELLA REAIL S.A.C.");
        detail.setChannel("ECOMMERCE");
        detail.setCreditLineId(1);

        ProcessedMovement processed = new ProcessedMovement();
        processed.setResponseMessage("Movement succesfuly registered");
        processed.setResponseCreditAmount(2000.00);
        processed.setResponseCurrentAmount(1800.00);
        processed.setResponse(detail);

        RestMovementResponse response = new RestMovementResponse();
        response.setResponseCreditAmount(200.00);
        response.setResponseCurrentAmount(200.00);
        response.setResponseMessage("Movement succesfuly registered");
        response.setMovementId(1);
        response.setOperationAmount(200.00);
        response.setExchangedAmount(200.00);
        response.setExchangeRate(1.00);
        response.setCreatedAt("2025-11-20 01:01:20.745378");
        response.setOperationtype("DEBIT");
        response.setOperationtDescription("purchase payment");
        response.setOperationCurrency("SOL");
        response.setExchangeCurrency("SOL");
        response.setOrigin("FALABELLA REAIL S.A.C.");
        response.setChannel("ECOMMERCE");
        response.setCreditLineId(1);
        response.setResponseStatus("PROCESSED");
        response.setResponseCode("02");

        Mockito.when(validator.reponseMapping(Mockito.any(ProcessedMovement.class), Mockito.any(MovementCsv.class)))
                .thenReturn(response);
        var result = this.validator.reponseMapping(processed, movement);
        Assertions.assertThat(result).isNotNull();
    }
}
