package com.inetum.movement_batch.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestMovementResponse {

    private Double responseCreditAmount;
    private Double responseCurrentAmount;
    private String responseMessage;
    private Integer movementId;
    private Double operationAmount;
    private Double exchangedAmount;
    private Double exchangeRate;
    private String createdAt;
    private String operationtype;
    private String operationtDescription;
    private String operationCurrency;
    private String exchangeCurrency;
    private String origin;
    private String channel;
    private Integer creditLineId;
    private String  responseCode;
    private String responseStatus;
}
