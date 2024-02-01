package com.MIDI.midi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
}
