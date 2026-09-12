package com.example.orderapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class OrderRequestDto {
    @NotBlank(message = "Item is required")
    private String item;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity should be greater than 0")
    private Integer quantity;
    @NotBlank(message = "Customer name is required")
    private String customerName;
    @Email(message = "Customer Email must be valid")
    @NotBlank(message = "Customer Email is required")
    private String customerEmail;
    @NotNull(message = "Price is required")
    @Positive(message = "Price should be greater than 0")
    private BigDecimal price;
    @NotBlank(message = "Shipping address is required")
    private String address;

}
