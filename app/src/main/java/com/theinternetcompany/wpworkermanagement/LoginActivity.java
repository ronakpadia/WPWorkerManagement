package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView username, password;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        String enteredUsername = username.getText().toString();
        String enteredPassword = password.getText().toString();
        if(enteredUsername.isEmpty()){
            Toast.makeText(LoginActivity.this,"Enter Username", Toast.LENGTH_SHORT).show();
        } else if (enteredPassword.isEmpty()){
            Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            if(enteredUsername.equals("WPAdmin") && enteredPassword.equals("WPassword")){
                loginUser();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void loginUser() {

    }

}