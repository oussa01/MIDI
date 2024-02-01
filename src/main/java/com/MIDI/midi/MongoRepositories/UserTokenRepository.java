package com.MIDI.midi.MongoRepositories;

import com.MIDI.midi.Documents.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTokenRepository extends MongoRepository<UserToken,String> {
    Optional<UserToken> findByRefreshToken(String refreshToken);
    Optional<UserToken> deleteByUserId(String userId);
    @Query(value = "{ 'test.id': ?0, $or: [ { expired: false }, { revoked: false } ] }")
    List<UserToken> findByAgentIdAndExpiredOrRevokedFalse(String id);
}
