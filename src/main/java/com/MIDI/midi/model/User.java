package com.MIDI.midi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter

public class User {
    private String id;
    private String firstname;
    private String lastname;
    @JsonProperty("username")
    private String username;
    private String email;
    @JsonProperty("password")
    private String password;
}
