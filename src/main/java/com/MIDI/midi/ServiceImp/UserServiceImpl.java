package com.MIDI.midi.ServiceImp;
import com.MIDI.midi.Security.JwtManager;
import com.MIDI.midi.Documents.UserDocument;
import com.MIDI.midi.Documents.UserToken;
import com.MIDI.midi.Handlers.InvalidRefreshTokenException;
import com.MIDI.midi.IService.UserService;
import com.MIDI.midi.MongoRepositories.UserRepository;
import com.MIDI.midi.MongoRepositories.UserTokenRepository;
import com.MIDI.midi.model.RefreshToken;
import com.MIDI.midi.model.SignedInUser;
import com.MIDI.midi.model.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserTokenRepository userTokenRepository;
    private final JwtManager tokenManager;
    private final PasswordEncoder bCryptPasswordEncoder;
    private UserToken userToken = new UserToken();

    public UserServiceImpl(UserRepository repository, UserTokenRepository userTokenRepository, JwtManager tokenManager, PasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.userTokenRepository = userTokenRepository;
        this.tokenManager = tokenManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDocument findUserByUsername(String username) {
        if (Strings.isBlank(username)) {
            throw new UsernameNotFoundException("Invalid user.");
        }
        final String uname = username.trim();
        UserDocument UserEntity = repository.findByUsername(uname);
        return UserEntity;
    }

    @Override
    public Optional<SignedInUser> createUser(User user) {
        UserDocument userEntity = repository.save(toDocument(user));
        return Optional.of(createSignedUserWithRefreshToken(
                userEntity));
    }

    private UserDocument toDocument(User user) {
        UserDocument userDocument = new UserDocument();
        BeanUtils.copyProperties(user,userDocument);
        userDocument.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userDocument;
    }

    @Override
    public SignedInUser getSignedInUser(UserDocument userEntity) {
        userTokenRepository.deleteByUserId(userEntity.getId());
        return createSignedUserWithRefreshToken(userEntity);
    }
    private SignedInUser createSignedInUser(UserDocument userEntity) {
        String token =
                tokenManager.generateToken(
                        org.springframework.security.core.userdetails.User.builder()
                                .username(userEntity.getUsername())
                                .password(userEntity.getPassword())
                                .build());
      return SignedInUser.builder()
              .username(userEntity.getUsername())
              .accessToken(token)
              .userId(userEntity.getId())
              .build();
    }

    private SignedInUser createSignedUserWithRefreshToken(UserDocument userDocument) {
        return createSignedInUser(userDocument).builder().refreshToken(createRefreshToken(userDocument)).build();
    }

    private String createRefreshToken(UserDocument user) {
        String tokenGenerated = RandomHolder.randomKey(128);
        var token =UserToken.builder()
                .user(user)
                        .refreshToken(tokenGenerated).build();
        userTokenRepository.save(token);
        return tokenGenerated;
    }

    @Override
    public Optional<SignedInUser> getAccessToken(
            RefreshToken refreshToken) {
        return userTokenRepository
                .findByRefreshToken(refreshToken.getRefreshToken())
                .map(ut ->
                        Optional.of(createSignedInUser(ut.getUser()).builder().refreshToken(refreshToken.getRefreshToken()).build()
                               ))
                .orElseThrow(() ->
                        new InvalidRefreshTokenException
                                ("Invalid token."));
    }



    /*
    In an ideal scenario, you should remove the refresh token of a user received from the
request. You can fetch the user ID from the token and then use that ID to remove it from
the USER_TOKEN table. However, in that case, you should send a valid access token.
     */
    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        userTokenRepository
                .findByRefreshToken(refreshToken.getRefreshToken())
                .ifPresentOrElse(
                        userTokenRepository::delete,
                        () -> {
                            throw new InvalidRefreshTokenException
                                    ("Invalid token.");
                        });
    }

    private static class RandomHolder {
        static final Random random = new SecureRandom();

        public static String randomKey(int length) {
            return String.format(
                            "%" + length + "s", new BigInteger(length * 5 /*base 32,2^5*/, random).toString(32))
                    .replace('\u0020', '0');
        }
    }
}

