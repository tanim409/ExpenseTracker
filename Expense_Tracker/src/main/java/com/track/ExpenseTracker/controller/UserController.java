package com.track.ExpenseTracker.controller;

import com.track.ExpenseTracker.DTO.UserDTO;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.UserRepo;
import com.track.ExpenseTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/expense")
public class UserController {

    private final UserRepo userRepo;
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
       return userService.register(userDTO);
    }

    @PostMapping("/login")
    String verify(@RequestBody UserDTO userDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return userService.verify(userDTO);
    }

}
