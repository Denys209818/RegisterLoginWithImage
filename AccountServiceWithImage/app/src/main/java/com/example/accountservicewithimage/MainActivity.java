package com.example.accountservicewithimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.accountservicewithimage.activities.LoginActivity;
import com.example.accountservicewithimage.activities.RegisterActivity;
import com.example.accountservicewithimage.activities.UserActivity;
import com.example.accountservicewithimage.activities.abstracts.AbstractActivity;
import com.example.accountservicewithimage.network.HomeApplication;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeApplication.setActivityId(R.id.to_main);
    }


}