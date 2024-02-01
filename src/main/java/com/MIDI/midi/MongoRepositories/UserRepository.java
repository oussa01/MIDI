package com.MIDI.midi.MongoRepositories;


import com.MIDI.midi.Documents.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends MongoRepository<UserDocument,String> {
    Integer findByUsernameOrEmail(String username,String email);
    UserDocument findByUsername(String username);
}
