package com.myblog9.controller;

import com.myblog9.entity.User;
import com.myblog9.payload.LoginDto;
import com.myblog9.payload.SignUpDto;
import com.myblog9.repository.RoleRepository;
import com.myblog9.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepo;



    @Autowired
    private AuthenticationManager authenticationManager;




    @PostMapping("/signin")//http://localhost:8080/api/auth/signup
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto
                                                           loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),
                        loginDto.getPassword())//verifies username and password,if valid generated token,if token is generated authentication address points to token
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.",
                HttpStatus.OK);
    }



    @PostMapping("/signup") //http://localhost:8080/api/auth/signup
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        Boolean emailExist = userRepo.existsByEmail(signUpDto.getEmail());
        if(emailExist){
            return new ResponseEntity<>("Email id exist", HttpStatus.BAD_REQUEST);
        }
        Boolean userNameExist = userRepo.existsByUsername(signUpDto.getUsername());
        if(userNameExist){
            return new ResponseEntity<>("Username exist",  HttpStatus.BAD_REQUEST);
        }
        User user=new User();
user.setName(signUpDto.getName());
user.setEmail(signUpDto.getEmail());
user.setUsername(signUpDto.getUsername());
user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userRepo.save(user);
        return new ResponseEntity<>("user is registered", HttpStatus.CREATED);
    }
}
