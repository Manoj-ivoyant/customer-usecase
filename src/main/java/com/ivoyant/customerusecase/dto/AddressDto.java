package com.ivoyant.customerusecase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    @NotBlank(message = "first name is required")
    @Pattern(regexp = "[A-Z][a-z]*", message = "first name must starts with uppercase")
    private String addressLane1;
    private String addressLane2;
    @NotBlank(message = "city is required field")
    private String city;
    @Pattern(regexp = "\\b\\d{5}\\b", message = "zip must be a 5-digit numeric value")
    private String zip;
    @NotBlank(message = "state is required field")
    private String state;
}
