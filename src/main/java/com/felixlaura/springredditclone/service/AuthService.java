package com.felixlaura.springredditclone.service;

import com.felixlaura.springredditclone.dto.RegisterRequest;
import com.felixlaura.springredditclone.model.User;
import com.felixlaura.springredditclone.model.VerificationToken;
import com.felixlaura.springredditclone.repository.UserRepository;
import com.felixlaura.springredditclone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    /**
     * signup method responsible to create User object through registration process and storing in the database
     */
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        userRepository.save(user);

        String token = generateVerificationToken(user);
    }

    /**
     * create a new token
     */
    public String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
