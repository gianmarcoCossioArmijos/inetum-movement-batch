package com.inetum.movement_batch.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementCsv {

    private Double operationAmount;
    private String operationType;
    private String operationDescription;
    private String operationCurrency;
    private String origin;
    private String channel;
    private String creditCardNumber;
}
