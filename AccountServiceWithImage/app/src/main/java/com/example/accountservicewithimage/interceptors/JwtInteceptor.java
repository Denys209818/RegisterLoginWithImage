package com.example.accountservicewithimage.interceptors;

import com.example.accountservicewithimage.network.HomeApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInteceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String token = HomeApplication.getApplication()
                .getToken();
        if(token != null && !token.isEmpty())
        {
            builder.header("Authorization", "Bearer "+token);
        }

        return chain.proceed(builder.build());
    }
}
