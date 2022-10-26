package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This is Forgot Password screen java file to provide logic to activity_forgot_password layout xml
 * This screen is to Setup a new password for User if the user forgot his password and give option to navigate to login Screen after the change of password.
 *
 * Author: IT19167442 Nusky M.A.M
 */

public class ForgotPassword extends AppCompatActivity {
    EditText otp, password, repassword;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        otp = (EditText) findViewById(R.id.otp);
        password = (EditText) findViewById(R.id.forgotPassword);
        repassword = (EditText) findViewById(R.id.confirmFPassword);
        submit = (Button) findViewById(R.id.btnforgotpassword);

        /** Redirecting to Login Screen via Intent **/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

    }
}