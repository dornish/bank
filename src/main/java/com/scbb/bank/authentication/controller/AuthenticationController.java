package com.scbb.bank.authentication.controller;

import com.scbb.bank.authentication.payload.ApiResponse;
import com.scbb.bank.authentication.payload.JwtAuthenticationResponse;
import com.scbb.bank.authentication.security.JwtTokenProvider;
import com.scbb.bank.person.model.User;
import com.scbb.bank.person.repository.RoleRepository;
import com.scbb.bank.person.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        // Check user existence
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {

            User existingUser = userRepository.findByUsername(user.getUsername()).get();

            // Check password
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.create(false, "Your password is wrong!!!"));

            else {

                // Check lock status
                if (!existingUser.getAccountNonLocked())
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.create(false, "Your account is Locked !!! Contact Manager for Unlock"));

                    //Check expiration status
                else if (!existingUser.getCredentialsNonExpired())
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.create(false, "Your account credentials is expired!!!"));

                    //Check enable status
                else if (!existingUser.getEnabled())
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.create(false, "Your account is disabled!!!"));

                else {

                    // Put current user details to authenticate object
                    Authentication authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

                    // set it to security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    //generate token
                    String jwt = tokenProvider.generateToken(authentication);

                    // return token
                    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
                }
            }

        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.create(false, "Username is incorrect")
            );

    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByToken(@RequestHeader("Authorization") String token) {
        Integer userId = tokenProvider.getUserIdFromToken(token.substring(7));
        User user = userRepository.getOne(userId);
        return ResponseEntity.ok(user);
    }

    @Transactional
    @PostMapping("/changePassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            User user1 = userRepository.findByUsername(user.getUsername()).get();
            user1.setPassword(passwordEncoder.encode(user.getPassword()));
            user1.setCredentialsNonExpired(true);
            return ResponseEntity.ok(new ApiResponse(true, "Password is Changed Successfully"));

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse(false, "Password is Changed Successfully"));
    }

    @Transactional
    @PostMapping("/matchPasswords")
    public ResponseEntity<Boolean> machPasswords(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            User user1 = userRepository.findByUsername(user.getUsername()).get();
            if (passwordEncoder.matches(user.getPassword(), user1.getPassword()))
                return ResponseEntity.ok(true);
            else
                return ResponseEntity.badRequest().body(false);

        }
        return ResponseEntity.badRequest().body(false);
    }

    @Transactional
    @PostMapping("/checkUsername")
    public ResponseEntity<?> checkUsernameAvailability(@RequestBody String username) {
        if (userRepository.existsByUsername(username))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Username has already taken"));
        return ResponseEntity.ok(new ApiResponse(true, "Username is available"));
    }

    @Transactional
    @PostMapping("/lock")
    public ResponseEntity<ApiResponse> lockAccount(@RequestBody User user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(user1 -> user1.setAccountNonLocked(true));
        return ResponseEntity.ok(new ApiResponse(false, "Your account has been Locked !!! Contact Manager for Unlock"));
    }


}
