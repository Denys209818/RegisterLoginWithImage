package com.example.accountservicewithimage.network;

import com.example.accountservicewithimage.network.dto.LoginModelDto;
import com.example.accountservicewithimage.network.dto.LoginReturnedDto;
import com.example.accountservicewithimage.network.dto.RegisterModelDto;
import com.example.accountservicewithimage.network.dto.RegisterReturnedDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("/api/account/register")
    public Call<RegisterReturnedDto> register(@Body RegisterModelDto registerModelDto);

    @POST("/api/account/login")
    public Call<LoginReturnedDto> login(@Body LoginModelDto loginModelDto);
}
