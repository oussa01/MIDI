package com.MIDI.midi.IService;

import com.MIDI.midi.Documents.UserDocument;
import com.MIDI.midi.model.RefreshToken;
import com.MIDI.midi.model.SignedInUser;
import com.MIDI.midi.model.User;

import java.util.Optional;

public interface UserService {
    UserDocument findUserByUsername(String username);
    Optional<SignedInUser> createUser(User user);
    SignedInUser getSignedInUser(UserDocument userEntity);
    Optional<SignedInUser> getAccessToken(RefreshToken refToken);
    void removeRefreshToken(RefreshToken refreshToken);

}
