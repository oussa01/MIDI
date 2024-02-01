package com.MIDI.midi.Documents;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Getter
@Setter
@Document(collection = "Users")
@Data
public class UserDocument implements Serializable {

    @Id
    private String id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Data CreationDate;

}
