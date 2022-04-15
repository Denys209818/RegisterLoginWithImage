package com.example.accountservicewithimage.activities.abstracts;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;

import com.example.accountservicewithimage.MainActivity;
import com.example.accountservicewithimage.R;
import com.example.accountservicewithimage.activities.LoginActivity;
import com.example.accountservicewithimage.activities.RegisterActivity;
import com.example.accountservicewithimage.activities.UserActivity;
import com.example.accountservicewithimage.network.HomeApplication;

public abstract class AbstractActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    protected Boolean onWifiCheck()
    {
        ConnectivityManager manager = (ConnectivityManager) HomeApplication
                .getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isConnected())
            return false;

        return true;
    }

    protected void showDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AbstractActivity.this);
        this.finishActivity(500);
        dialog.setMessage("Відсутнє підклчення до інтернету!")
                .setCancelable(false)
                .setPositiveButton("Підключитися", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }


                })
                .setNegativeButton("Відмінити", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(AbstractActivity.this,
                                MainActivity.class));
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activities, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int ident = item.getItemId();
        switch(ident)
        {
            case R.id.to_register:
            {
                return startActivityFromMenu(R.id.to_register, RegisterActivity.class);
            }
            case R.id.to_login:
            {
                return startActivityFromMenu(R.id.to_login, LoginActivity.class);
            }
            case R.id.to_users: {
                return startActivityFromMenu(R.id.to_users, UserActivity.class);
            }
            case R.id.to_main:
            {
                return startActivityFromMenu(R.id.to_main, MainActivity.class);
            }
            default:
            {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private Boolean startActivityFromMenu(int identifier, Class clas)
    {
        if(HomeApplication.getActivityId() != identifier)
        {
            HomeApplication.setActivityId(identifier);
            Intent intent = new Intent(this, clas);
            startActivity(intent);

        }
        return true;
    }
}
