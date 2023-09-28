package com.example.photofiesta.controller;

import com.example.photofiesta.models.User;
import com.example.photofiesta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(path = "/auth/users")
public class UserController {

    private UserService userService;

    static HashMap<String, Object> message = new HashMap<>();

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/hello/")
    public ResponseEntity<?> getHello() {
        message.put("message", "Hello");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PostMapping(path = "/register/")
    public ResponseEntity<?> createUser(@RequestBody User userObject) {
        User newUser = userService.createUser(userObject);
        if (newUser != null) {
            message.put("message", "success");
            message.put("data", newUser);
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            message.put("message", "email already exists");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
