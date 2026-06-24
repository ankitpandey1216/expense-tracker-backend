package com.ankit.expense_tracker.DTO;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
}
