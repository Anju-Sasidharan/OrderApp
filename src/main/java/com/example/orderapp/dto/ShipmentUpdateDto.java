package com.example.orderapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class ShipmentUpdateDto {
    @NotBlank(message = "Tracking number is required")
    private String trackingNo;
}
