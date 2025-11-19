package com.inetum.movement_batch;

port lombok.Data;

@Data
public class MovementProcessed {

    private Double movementAmount;
    private String movementType;
    private String movementCurrency;
    private String movementChannel;
    private int movementcreditLineId;
    private String movementOrigin;
    private String movementDescription;
    private String creditLineCurrency;
    private Double credliLineExchangeRate;
    private Double movementConvertedOperationAmount;
    private String processStatusCode;
    private String processCode;
    private String processInfo;

}
