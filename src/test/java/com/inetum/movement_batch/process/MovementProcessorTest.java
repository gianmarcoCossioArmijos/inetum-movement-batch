package com.inetum.movement_batch.process;

import com.inetum.movement_batch.dto.RestMovementResponse;
import com.inetum.movement_batch.entity.MovementCsv;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(MovementProcessor.class)
public class MovementProcessorTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private MovementProcessor processor;

    @Test
    public void MovementProcessor_RestClientPost() throws Exception {
        MovementCsv movement = new MovementCsv(
                200.00,
                "DEBIT",
                "purchase payment",
                "SOL",
                "FALABELLA REAIL S.A.C.",
                "ECOMMERCE",
                1);

        Map<String, Object> request = new HashMap<>();
        request.put("operationAmount", 200.00);
        request.put("operationtype", "DEBIT");
        request.put("operationtDescription", "purchase payment");
        request.put("operationCurrency", "SOL");
        request.put("origin", "FALABELLA REAIL S.A.C.");
        request.put("channel", "ECOMMERCE");
        request.put("creditLineId", 1);

        this.server.expect(ExpectedCount.once(), requestTo("http://localhost:8082/credit-card/api/v1/movement/create"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                            "responseMessage": "Movement succesfuly registered",
                            "responseCreditAmount": 2000.00,
                            "responseCurrentAmount": 1800.00,
                            "response": {
                                "movementId": 1,
                                "operationAmount": 200.00,
                                "exchangedAmount": 200.00,
                                "exchangeRate": 1.00,
                                "createdAt": "2025-11-20 01:01:20.745378",
                                "operationtype": "DEBIT",
                                "operationtDescription": "purchase payment",
                                "operationCurrency": "SOL",
                                "exchangeCurrency": "SOL",
                                "origin": "FALABELLA REAIL S.A.C.",
                                "channel": "ECOMMERCE",
                                "creditLineId": 1
                            }
                        }""", MediaType.APPLICATION_JSON));

        var result = this.processor.process(movement);
        RestMovementResponse response = new RestMovementResponse();
        response.setResponseCreditAmount(result.getResponseCreditAmount());
        response.setResponseCurrentAmount(result.getResponseCurrentAmount());
        response.setResponseMessage(result.getResponseMessage());
        response.setMovementId(result.getMovementId());
        response.setOperationAmount(result.getOperationAmount());
        response.setExchangedAmount(result.getExchangedAmount());
        response.setExchangeRate(result.getExchangeRate());
        response.setCreatedAt(result.getCreatedAt());
        response.setOperationtype(result.getOperationtype());
        response.setOperationtDescription(result.getOperationtDescription());
        response.setOperationCurrency(result.getOperationCurrency());
        response.setExchangeCurrency(result.getExchangeCurrency());
        response.setOrigin(result.getOrigin());
        response.setChannel(result.getChannel());
        response.setCreditLineId(result.getCreditLineId());

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getResponseMessage()).isEqualTo("Movement succesfuly registered");
        Assertions.assertThat(response.getResponseCreditAmount()).isEqualTo(2000.00);
        Assertions.assertThat(response.getResponseCurrentAmount()).isEqualTo(1800.00);
        Assertions.assertThat(response.getMovementId()).isEqualTo(1);
        Assertions.assertThat(response.getOperationAmount()).isEqualTo(200.00);
        Assertions.assertThat(response.getExchangedAmount()).isEqualTo(200.00);
        Assertions.assertThat(response.getExchangeRate()).isEqualTo(1.00);
        Assertions.assertThat(response.getCreatedAt()).isEqualTo("2025-11-20 01:01:20.745378");
        Assertions.assertThat(response.getOperationtype()).isEqualTo("DEBIT");
        Assertions.assertThat(response.getOperationtDescription()).isEqualTo("purchase payment");
        Assertions.assertThat(response.getOperationCurrency()).isEqualTo("SOL");
        Assertions.assertThat(response.getExchangeCurrency()).isEqualTo("SOL");
        Assertions.assertThat(response.getOrigin()).isEqualTo("FALABELLA REAIL S.A.C.");
        Assertions.assertThat(response.getChannel()).isEqualTo("ECOMMERCE");
        Assertions.assertThat(response.getCreditLineId()).isEqualTo(1);
        this.server.verify();
    }
}
