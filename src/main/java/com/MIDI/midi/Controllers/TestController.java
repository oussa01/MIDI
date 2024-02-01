package com.MIDI.midi.Controllers;

import com.MIDI.midi.ServiceImp.UserServiceImpl;
import com.MIDI.midi.model.SignedInUser;
import com.MIDI.midi.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    private final UserServiceImpl userService;

    public TestController(UserServiceImpl userService) {
        this.userService = userService;
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

}
