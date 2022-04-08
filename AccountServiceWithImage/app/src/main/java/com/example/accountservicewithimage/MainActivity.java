package com.example.accountservicewithimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.accountservicewithimage.activities.LoginActivity;
import com.example.accountservicewithimage.activities.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater Inflater = getMenuInflater();
        Inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.register:
                {
                    Intent intent = new Intent(this, RegisterActivity.class);
                    startActivity(intent);
                    return true;
                }
            case R.id.login:
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    return true;
                }
            default:
                {
                    return super.onOptionsItemSelected(item);
                }
        }
    }
}