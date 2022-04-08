package com.example.accountservicewithimage.network.dto.errors;

import lombok.Data;

@Data
public class ErrorsDto {
    private ErrorsParamDto errors;
    private int status;
    private String title;
}
