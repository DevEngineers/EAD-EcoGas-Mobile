package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.example.ecogas.Model.User;
import com.example.ecogas.Service.DBMaster;
import com.example.ecogas.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is User Registration screen java file to provide logic to activity_register layout xml
 * This screen is to Register a new User and give option to navigate to login Screen after User is successfully registered .
 *
 * Author: IT19167442 Nusky M.A.M
 */

public class Register extends AppCompatActivity {

    EditText name,userName, password, retypePassword;
    Button signUp;
    DBMaster DB;
    TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        retypePassword = (EditText) findViewById(R.id.repassword);
        signUp = (Button) findViewById(R.id.btnsignup);
        signIn = (TextView) findViewById(R.id.btnsignin);
        DB = new DBMaster(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userPassword = password.getText().toString();
                String userRetypePassword = retypePassword.getText().toString();

                User user = new User();
                user.setUserName(userName.getText().toString());
                user.setName(name.getText().toString());
                user.setType("User");

                /** Validation to check  if the Registration form fields are empty or not when trying to  Register the User **/
                if(user.getUserName().equals("")||userPassword.equals("")||userRetypePassword.equals(""))
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(userPassword.equals(userRetypePassword)){
                        Boolean checkUser = DB.checkUsername(user.getUserName());

                        if(!checkUser){
                            Boolean insert = DB.insertData(user.getUserName(), userPassword);

                            if(insert){
                                /** Api call to User **/
                                Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                                UserService userService = retrofit.create(UserService.class);
                                Call<User> call = userService.createUser(user);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.isSuccessful()){
                                            User createdUser=response.body();
                                            Boolean isUserUpdated = DB.updateUserId(createdUser.getUserName(),createdUser.getId());
                                            if(isUserUpdated){
                                                Toast.makeText(Register.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(Register.this, "User Registered Failure", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });

                            }
                            else{
                                Toast.makeText(Register.this, "User Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        /** check if the user Already exist **/
                        else{
                            Toast.makeText(Register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                        /** check if the password and re entered password are matching  **/
                    }else{
                        Toast.makeText(Register.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } }
        });

        /** Redirecting to Login Screen via Intent **/
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}