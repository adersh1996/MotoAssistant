package com.project.motoassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.project.motoassistant.models.Root;
import com.project.motoassistant.retrofit.APIInterface;
import com.project.motoassistant.retrofit.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    private ImageView loginImg;
    private TextView tvOne;
    private LinearLayout etBoxOne;
    @NotEmpty
    // @Pattern(regex = "^[7-9][0-9]{9}$", message = "Invalid Mobile Number")
    private TextInputEditText phoneNumberEt;
    private LinearLayout passwordLayout;
    private TextInputLayout etBoxTwo;
    @NotEmpty
    private TextInputEditText passwordEt;
    private TextView loginBt;
    private TextView regBt;
    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // changeStatusBarColor(MainActivity.this,color);

        initView();
        validator = new Validator(this);
        validator.setValidationListener(this);

        regBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    private void initView() {
        loginImg = findViewById(R.id.login_img);
        tvOne = findViewById(R.id.tv_one);
        etBoxOne = findViewById(R.id.et_box_one);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        passwordLayout = findViewById(R.id.password_layout);
        etBoxTwo = findViewById(R.id.et_box_two);
        passwordEt = findViewById(R.id.password_et);
        loginBt = findViewById(R.id.login_bt);
        regBt = findViewById(R.id.reg_bt);
    }

    @Override
    public void onValidationSucceeded() {

        apiCall();

        //Toast.makeText(this, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }

        }

    }

    public void apiCall() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("token", getApplicationContext().MODE_PRIVATE);
        String deviceToken = sharedPreferences.getString("token", "");
        // Toast.makeText(this, deviceToken, Toast.LENGTH_SHORT).show();

        APIInterface api = ApiClient.getClient().create(APIInterface.class);
        api.LOGINROOTCALL(phoneNumberEt.getText().toString(), passwordEt.getText().toString(), deviceToken).enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    if (root.status) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("login_data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name", root.userDetails.get(0).name);
                        editor.putString("phone", root.userDetails.get(0).phone);
                        editor.putString("email", root.userDetails.get(0).email);
                        editor.putString("address", root.userDetails.get(0).address);
                        editor.putString("image", root.userDetails.get(0).image);
                        editor.putString("userId", root.userDetails.get(0).id);
                        editor.commit();
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, root.message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                    String message = "Server Error";
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                    snackbar.show();
                    //Toast.makeText(MainActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

                String message = "Server Error";
                Snackbar snackbar = Snackbar.make(findViewById(R.id.login_layout), message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                snackbar.show();

            }
        });
    }
}