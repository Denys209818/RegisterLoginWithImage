package com.example.accountservicewithimage.network.dto.errors;

import lombok.Data;

@Data
public class ErrorsParamDto {
    private String[] email;
    private String[] firstName;
    private String[] secondName;
    private String[] phone;
    private String[] photo;
    private String[] password;
    private String[] confirmPassword;
}
