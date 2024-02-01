package com.MIDI.midi.Controllers;

import com.MIDI.midi.Documents.UserDocument;
import com.MIDI.midi.IService.UserService;
import com.MIDI.midi.ServiceImp.UserServiceImpl;
import com.MIDI.midi.model.RefreshToken;
import com.MIDI.midi.model.SignInReq;
import com.MIDI.midi.model.SignedInUser;
import com.MIDI.midi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private final UserServiceImpl userService;
    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    public TestController(UserServiceImpl userService, UserService service, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String getTest(){
    return "hello from api";
}
@PostMapping("/auth")
    public ResponseEntity<SignedInUser> signUp
            (User user) {
        return status(HttpStatus.CREATED)
                .body(userService.createUser(user).get());
    }
@PostMapping("/t")
    public ResponseEntity<Optional<SignedInUser>> getAccessToken(@RequestBody RefreshToken refreshToken) {
        return ok(service.getAccessToken(refreshToken));
    }
    @DeleteMapping
    public ResponseEntity<?> delete (RefreshToken refreshToken){
        userService.removeRefreshToken(refreshToken);
       return ResponseEntity.noContent().build();
    }
    @PostMapping("/a")
    public ResponseEntity<SignedInUser> signIn(SignInReq signInReq) {
        UserDocument userEntity = service
                .findUserByUsername(signInReq.getUsername());
        if (passwordEncoder.matches(signInReq.getPassword(),
                userEntity.getPassword())) {
            return ok(service.getSignedInUser(userEntity));
        }
        throw new IllegalArgumentException
                ("Unauthrzed");
    }

}
