package com.example.accountservicewithimage.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountservicewithimage.R;
import com.example.accountservicewithimage.activities.abstracts.AbstractActivity;
import com.example.accountservicewithimage.network.AccountService;
import com.example.accountservicewithimage.network.HomeApplication;
import com.example.accountservicewithimage.users.UserCardViewAdapter;
import com.example.accountservicewithimage.users.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AbstractActivity {

    private RecyclerView rcView;
    private UserCardViewAdapter userCardViewAdapter;
    private ProgressBar progressBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_user);

            rcView = findViewById(R.id.rcView);
            progressBar = findViewById(R.id.progressBar);
            rcView.setHasFixedSize(true);
            rcView.setLayoutManager(new GridLayoutManager(
               this,2,
                LinearLayoutManager.VERTICAL, false
            ));

        sendRequest();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        sendRequest();
    }

    public void sendRequest()
    {

        progressBar.setVisibility(View.VISIBLE);
        AccountService
                .getInstance()
                .getRetrofit()
                .getUsers()
                .enqueue(new Callback<List<UserDTO>>() {
                    @Override
                    public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                        userCardViewAdapter = new UserCardViewAdapter(response.body());
                        rcView.setAdapter(userCardViewAdapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }


                    @Override
                    public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(!onWifiCheck())
                        {
                            showDialog();

                        }else
                        {
                            t.printStackTrace();
                        }
                    }
                });
    }
}
