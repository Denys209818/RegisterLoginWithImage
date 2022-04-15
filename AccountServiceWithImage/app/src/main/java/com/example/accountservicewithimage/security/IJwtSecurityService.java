package com.example.accountservicewithimage.security;

public interface IJwtSecurityService {
    void saveToken(String token);
    String getToken();
    void deleteToken();
}
