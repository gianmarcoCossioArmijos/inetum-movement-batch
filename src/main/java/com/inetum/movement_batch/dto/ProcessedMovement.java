package com.inetum.movement_batch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedMovement {

    private Double responseCreditAmount;
    private Double responseCurrentAmount;
    private RestMovementResponseDetails response;
    private String responseMessage;
}
