package com.exodus.dome.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";
  private long expiresIn;

  public AuthResponse(String accessToken, String refreshToken, long expiresIn) {
    this.refreshToken = refreshToken;
    this.expiresIn = expiresIn;
    this.accessToken = accessToken;
  }
}
