package com.inetum.movement_batch.service;

import com.inetum.movement_batch.service.MovementApiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(MovementApiService.class)
public class MovementApiServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private MovementApiService api;

    @Test
    public void restClientTest_ValidateCreditCard() throws Exception {
        Map<String, Object> cardNumberRequest = new HashMap<>();
        cardNumberRequest.put("creditCardNumber", "0000-0000-0000-0001");
        cardNumberRequest.put("creditLineCurrency", "SOL");

        this.server.expect(ExpectedCount.once(), requestTo("http://localhost:8081/credit-card/api/v1/credit-line/validate-card"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("""
                        {
                            "creditCardIdCode": 1
                        }""", MediaType.APPLICATION_JSON));

        var result = this.api.restClientValidateCreditCard(cardNumberRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCreditCardIdCode()).isNotNull();
        this.server.verify();
    }

    @Test
    public void restClientTest_postMovment() throws Exception {
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

        var result = this.api.restClientPostMovment(request);

        Assertions.assertThat(result).isNotNull();
        this.server.verify();
    }
}

