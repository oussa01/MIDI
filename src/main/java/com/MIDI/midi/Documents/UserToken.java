package com.MIDI.midi.Documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "Tokens")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserToken {
    @Id
    private String id;
    private String refreshToken;
    private boolean revoked;
    private boolean expired;
    @DBRef
    private UserDocument user;
}
