package com.example.automateelectroluxappliances.model;

import com.fasterxml.jackson.annotation.JsonProperty;
public class RefreshTokenRequest {

    @JsonProperty("refreshToken")
    private String refreshToken;

    public RefreshTokenRequest() {
        // Default constructor
    }

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}