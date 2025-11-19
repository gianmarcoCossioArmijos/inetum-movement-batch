package com.inetum.movement_batch.dto;

@Data
public class RestMovementResponseDetails {

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
}
