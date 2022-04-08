package com.example.accountservicewithimage.network.dto;

import lombok.Data;

@Data
public class RegisterModelDto {
    private String firstName;
    private String secondName;
    private String photo;
    private String phone;
    private String email;
    private String password;
    private String confirmPassword;
}
