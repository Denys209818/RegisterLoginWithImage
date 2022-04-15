package com.example.accountservicewithimage.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accountservicewithimage.MainActivity;
import com.example.accountservicewithimage.R;
import com.example.accountservicewithimage.activities.abstracts.AbstractActivity;
import com.example.accountservicewithimage.network.AccountService;
import com.example.accountservicewithimage.network.HomeApplication;
import com.example.accountservicewithimage.network.dto.RegisterModelDto;
import com.example.accountservicewithimage.network.dto.RegisterReturnedDto;
import com.example.accountservicewithimage.network.dto.errors.ErrorsDto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AbstractActivity {

    private ImageView imageView;
    private TextInputEditText txtFirstname;
    private TextInputEditText txtSecondname;
    private TextInputEditText txtEmail;
    private TextInputEditText txtPhone;
    private TextInputEditText txtPassword;
    private TextInputEditText txtConfirmPassword;

    private TextInputLayout txtLayoutFirstname;
    private TextInputLayout txtLayoutSecondname;
    private TextInputLayout txtLayoutEmail;
    private TextInputLayout txtLayoutPhone;
    private TextInputLayout txtLayoutPassword;
    private TextInputLayout txtLayoutConfirmPassword;
    private String _base64Image = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageView = findViewById(R.id.previewImage);
        txtFirstname = findViewById(R.id.txtFirstName);
        txtSecondname = findViewById(R.id.txtSecondName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtPhone = findViewById(R.id.txtPhone);

        txtLayoutFirstname = findViewById(R.id.txtLayotFirstname);
        txtLayoutSecondname = findViewById(R.id.txtLayotSecondname);
        txtLayoutEmail = findViewById(R.id.txtLayotEmail);
        txtLayoutPhone = findViewById(R.id.txtLayotPhone);
        txtLayoutPassword = findViewById(R.id.txtLayotPassword);
        txtLayoutConfirmPassword = findViewById(R.id.txtFieldConfirmPassword);
    }

    public void onSelectImage(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Обрати фотографію"), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == 200)
            {
                Uri resource = data.getData();
                imageView.setImageURI(resource);

                try
                {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), resource);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    _base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

                }catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void onSubmitForm(View view)
    {
        if(onWifiCheck()) {
            this.txtLayoutFirstname.setError(null);
            this.txtLayoutSecondname.setError(null);
            this.txtLayoutEmail.setError(null);
            this.txtLayoutPhone.setError(null);
            this.txtLayoutPassword.setError(null);
            this.txtLayoutConfirmPassword.setError(null);
            try {
                if (!isValidTextFields())
                    return;

                RegisterModelDto registerModelDto = new RegisterModelDto();
                registerModelDto.setFirstName(this.txtFirstname.getText().toString());
                registerModelDto.setSecondName(this.txtSecondname.getText().toString());
                registerModelDto.setEmail(this.txtEmail.getText().toString());
                registerModelDto.setPhone(this.txtPhone.getText().toString());
                registerModelDto.setPhoto(_base64Image);
                registerModelDto.setPassword(this.txtPassword.getText().toString());
                registerModelDto.setConfirmPassword(this.txtConfirmPassword.getText().toString());

                AccountService.getInstance()
                        .getRetrofit()
                        .register(registerModelDto)
                        .enqueue(new Callback<RegisterReturnedDto>() {
                            @Override
                            public void onResponse(Call<RegisterReturnedDto> call,
                                                   Response<RegisterReturnedDto> response) {
                                if (response.isSuccessful()) {
                                    RegisterReturnedDto retRegister = response.body();
                                    HomeApplication application = HomeApplication.getApplication();
                                    application.saveToken(retRegister.getToken());
                                    Intent intent = new Intent(RegisterActivity.this,
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
                            public void onFailure(Call<RegisterReturnedDto> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(RegisterActivity.this,
                                        "Помилка в запиті: " + t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else
        {
            showDialog();
        }
    }

    public void setExceptionFromServer(String json)
    {
        Gson gson = new Gson();
        ErrorsDto errors = gson.fromJson(json, ErrorsDto.class);

        setErrorToField(this.txtLayoutFirstname, errors.getErrors().getFirstName());
        setErrorToField(this.txtLayoutSecondname, errors.getErrors().getSecondName());
        setErrorToField(this.txtLayoutPhone, errors.getErrors().getPhone());
        setErrorToField(this.txtLayoutPassword, errors.getErrors().getPassword());
        setErrorToField(this.txtLayoutConfirmPassword, errors.getErrors().getConfirmPassword());
        setErrorToField(this.txtLayoutEmail, errors.getErrors().getEmail());
    }

    public void setErrorToField(TextInputLayout input, String [] errors)
    {
        String err = "";
        if(errors != null)
        {
            for(String str : errors)
            {
                err += str;
            }
            input.setError(err);
        }
    }

    public Boolean isValidTextFields()
    {
        Boolean isFlag = true;

        TextInputEditText[] inputs = {
          this.txtFirstname,
          this.txtSecondname,
          this.txtEmail,
          this.txtPhone,
        };

        TextInputLayout[] layouts = {
                this.txtLayoutFirstname,
                this.txtLayoutSecondname,
                this.txtLayoutEmail,
                this.txtLayoutPhone,
        };

        TextInputEditText[] inputsPass = {
                this.txtPassword,
                this.txtConfirmPassword
        };

        TextInputLayout[] inputsPassLayout = {
                this.txtLayoutPassword,
                this.txtLayoutConfirmPassword
        };

        for(int i = 0; i< inputs.length; i++) {
            isFlag = isValidTextField(inputs[i],layouts[i], false);
            if(!isFlag)
                return isFlag;
        }

        if(!this.txtEmail.getText().toString().matches("^[0-9a-z]+@[a-z]+\\.[a-z]+$"))
        {
            this.txtLayoutEmail.setError("Не коректна пошта!");
            return false;
        }

        for(int i = 0; i < inputsPass.length; i++)
        {
            isFlag = isValidTextField(inputsPass[i],inputsPassLayout[i], true);
            if(!isFlag)
                return isFlag;
        }

        if(!this.txtPassword.getText().toString()
                .equals(this.txtConfirmPassword.getText().toString()))
        {
            this.txtLayoutConfirmPassword.setError("Поля не співпадають!");
            return false;
        }



        return isFlag;
    }

    public Boolean isValidTextField(TextInputEditText input, TextInputLayout layout, Boolean validateLength)
    {
        if(input.getText().toString().isEmpty())
        {
            layout.setError("Поле не може бути пустим!");
            return false;
        }
        if(validateLength && input.getText().toString().length() < 5){
            layout.setError("Поле містить менше 5 символів!");
            return false;}
        return true;
    }
}
