package com.MIDI.midi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Getter
@Setter
@Builder
public class SignedInUser {
    @JsonProperty("username")
    public String username;
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("user_id")
    public String userId;

    public SignedInUser(String username, String accessToken, String refreshToken, String userId) {
        this.username = username;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public SignedInUser refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
