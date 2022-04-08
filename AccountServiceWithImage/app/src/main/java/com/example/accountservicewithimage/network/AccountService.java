package com.example.accountservicewithimage.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountService {
    private String BASE = "http://10.0.2.2:5124";
    private static AccountService accountService;
    private Retrofit retrofit;

    private AccountService()
    {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RetrofitService getRetrofit()
    {
        return retrofit.create(RetrofitService.class);
    }

    public static AccountService getInstance()
    {
        if(accountService == null)
            accountService = new AccountService();

        return accountService;
    }
}
