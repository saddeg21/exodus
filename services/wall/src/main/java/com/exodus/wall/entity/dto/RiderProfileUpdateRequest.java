package com.exodus.wall.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiderProfileUpdateRequest {

  @NotBlank
  private String fullName;

  @NotBlank
  @Pattern(regexp = "^\\+?[0-9]{8,15}$",
      message = "Invalid phone number format")
  private String phoneNumber;

}