package com.ankit.expense_tracker.controller;

import com.ankit.expense_tracker.DTO.UserDTO;
import com.ankit.expense_tracker.DTO.UserRegisterRequest;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.service.JwtService;
import com.ankit.expense_tracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterRequest request){
        User registeredUser = userService.registerUser(request);
        UserDTO userDTO = new UserDTO(registeredUser.getUserId(),registeredUser.getName(),registeredUser.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
    @PostMapping("/login")
    public Map<String,String> authenticateUser(@RequestBody User user){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
        if(authentication.isAuthenticated()){
            String token = jwtService.getAuthenticationToken(user.getEmail());
            Map<String,String> response = new HashMap<>();
            response.put("token",token);
            return response;
        }
        throw new RuntimeException("Invalid credentials");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam String query){
        List<UserDTO> users = userService.getAllUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
