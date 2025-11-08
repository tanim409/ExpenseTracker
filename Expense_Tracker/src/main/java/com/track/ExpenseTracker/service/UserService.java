package com.track.ExpenseTracker.service;

import com.track.ExpenseTracker.DTO.UserDTO;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User register(UserDTO userDTO) {
        User user = new User();

        if (userRepo.findByUsername(userDTO.getUsername()).isPresent() || userRepo.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Username or Email is already in use");
        }
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepo.save(user);
    }

    public String verify(UserDTO userDTO) throws NoSuchAlgorithmException, InvalidKeySpecException {

        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));

        if(authentication.isAuthenticated()){
            return jwtService.generateJwtToken(username);
        }

        return null;
    }
}