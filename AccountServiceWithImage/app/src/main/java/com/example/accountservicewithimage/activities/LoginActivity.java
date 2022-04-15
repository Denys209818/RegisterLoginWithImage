package com.example.accountservicewithimage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accountservicewithimage.MainActivity;
import com.example.accountservicewithimage.R;
import com.example.accountservicewithimage.activities.abstracts.AbstractActivity;
import com.example.accountservicewithimage.network.AccountService;
import com.example.accountservicewithimage.network.HomeApplication;
import com.example.accountservicewithimage.network.dto.LoginModelDto;
import com.example.accountservicewithimage.network.dto.LoginReturnedDto;
import com.example.accountservicewithimage.network.dto.RegisterReturnedDto;
import com.example.accountservicewithimage.network.dto.errors.ErrorsDto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AbstractActivity {

    private TextInputEditText txtLoginEmail;
    private TextInputEditText txtLoginPassword;
    private TextInputLayout txtLoginLayoutEmail;
    private TextInputLayout txtLoginLayoutPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLoginEmail = findViewById(R.id.txtLoginEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
        txtLoginLayoutEmail = findViewById(R.id.textInputLoginLayoutEmail);
        txtLoginLayoutPassword = findViewById(R.id.textInputLoginLayout);
    }

    public void onLoginHandler(View view)
    {
        if(onWifiCheck()) {
            if (!isValidTextFields())
                return;

            LoginModelDto loginModelDto = new LoginModelDto();
            loginModelDto.setEmail(this.txtLoginEmail.getText().toString());
            loginModelDto.setPassword(this.txtLoginPassword.getText().toString());

            AccountService
                    .getInstance()
                    .getRetrofit()
                    .login(loginModelDto)
                    .enqueue(new Callback<LoginReturnedDto>() {
                        @Override
                        public void onResponse(Call<LoginReturnedDto> call, Response<LoginReturnedDto> response) {
                            if (response.isSuccessful()) {
                                LoginReturnedDto retRegister = response.body();
                                HomeApplication application = HomeApplication.getApplication();
                                application.saveToken(retRegister.getToken());
                                Intent intent = new Intent(LoginActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                            } else {
                                try {
                                    String jsonError = response.errorBody().string();
                                    setExceptionFromServer(jsonError);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginReturnedDto> call, Throwable t) {
                            String text = "Помилка запиту: " + t.getMessage();
                            Toast.makeText(LoginActivity.this,
                                    text
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
        }else
            {
                showDialog();
            }
    }

    public void setExceptionFromServer(String json)
    {

        Gson gson = new Gson();
        ErrorsDto errors = gson.fromJson(json, ErrorsDto.class);

        String err = "";
        if(errors.getErrors().getEmail() != null)
        {
            for(String str : errors.getErrors().getEmail())
            {
                err += str;
            }
            this.txtLoginLayoutEmail.setError(err);
        }

        err = "";
        if(errors.getErrors().getPassword() != null) {
            for (String str : errors.getErrors().getPassword()) {
                err += str;
            }
            this.txtLoginLayoutPassword.setError(err);
        }
    }

    public Boolean isValidTextFields()
    {
        this.txtLoginLayoutEmail.setError(null);
        this.txtLoginLayoutPassword.setError(null);

        Boolean isFlag = true;

        TextInputEditText[] inputs  = {
                this.txtLoginEmail,
                this.txtLoginPassword
        };

        TextInputLayout [] layouts =
                {
                        this.txtLoginLayoutEmail,
                        this.txtLoginLayoutPassword
                };

        for(int i = 0; i< inputs.length; i++) {
            isFlag = isValidTextField(inputs[i], layouts[i]);
            if(!isFlag)
                return isFlag;
        }

        if(!this.txtLoginEmail.getText().toString().matches("^[0-9a-z]+@[a-z]+\\.[a-z]+$"))
        {
            this.txtLoginLayoutEmail.setError("Не коректна пошта!");
            return false;
        }

        return isFlag;
    }

    public Boolean isValidTextField(TextInputEditText input, TextInputLayout layout)
    {
        if(input.getText().toString().isEmpty())
        {
            layout.setError("Поле не може бути пустим!");
            return false;
        }
        return true;
    }


}
