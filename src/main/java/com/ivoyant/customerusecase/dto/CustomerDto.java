package com.ivoyant.customerusecase.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    @NotBlank(message = "first name is required")
    @Pattern(regexp = "[A-Z][a-z]*", message = "first name must starts with uppercase")
    private String firstName;
    private String lastName;
    @NotBlank(message = "phone number is required")
    @Pattern(regexp = "[6-9]\\d{9}", message = "phone number is invalid")
    private String phone;
    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    private String email;
    @Valid
    private AddressDto addressDto;
}
