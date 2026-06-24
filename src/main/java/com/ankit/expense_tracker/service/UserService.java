package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.DTO.UserDTO;
import com.ankit.expense_tracker.DTO.UserRegisterRequest;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    public User registerUser(UserRegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        return userRepo.save(user);
    }

    public List<UserDTO> getAllUsers(String query) {
        List<User> users = userRepo.searchUser(query);

        return users.stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail()
        );
    }
}
