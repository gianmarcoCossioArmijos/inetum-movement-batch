package com.inetum.movement_batch;

import lombok.Data;

@Data
public class MovementCsv {

    private Double operationAmount;
    private String operationType;
    private String operationDescription;
    private String operationCurrency;
    private String origin;
    private String channel;
    private int creditLineId;
}
