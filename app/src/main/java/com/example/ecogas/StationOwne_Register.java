package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecogas.Model.User;
import com.example.ecogas.Service.DBMaster;
import com.example.ecogas.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationOwne_Register extends AppCompatActivity {
    EditText name,userName, password, reTypepassword;
    Button signup;
    DBMaster DB;
    TextView signin, shedOwnerSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_owne_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.stationOwnername);
        userName = (EditText) findViewById(R.id.shedOwnerUsername);
        password = (EditText) findViewById(R.id.shedOwnerPassword);
        reTypepassword = (EditText) findViewById(R.id.shedOwnerepassword);
        signup = (Button) findViewById(R.id.shedOwnerbtnsignup);
        signin = (TextView) findViewById(R.id.shedOwnerbtnsignin);
        DB = new DBMaster(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String userName = username.getText().toString();
                String shedOwnerPassword = password.getText().toString();
                String shedOwnerreTypepassword = reTypepassword.getText().toString();


                User shedOwner = new User();
          shedOwner.setUserName(userName.getText().toString());
                shedOwner.setName(name.getText().toString());
                shedOwner.setType("StationOwner");

                if (shedOwner.getUserName().equals("") || shedOwnerPassword.equals("") || shedOwnerreTypepassword.equals(""))
                    Toast.makeText(StationOwne_Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {

                    if (shedOwnerPassword.equals(shedOwnerreTypepassword)) {
                        Boolean checkuser = DB.checkusername(shedOwner.getUserName());

                        if (checkuser == false) {
                            Boolean insert = DB.insertData(shedOwner.getUserName(), shedOwnerPassword);

                            if (insert == true) {
                                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.10:29193/").addConverterFactory(GsonConverterFactory.create()).build();
                                UserService userService = retrofit.create(UserService.class);
                                Call<User> call = userService.createUser(shedOwner);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.isSuccessful()){
                                            User createdUser=response.body();
                                            Toast.makeText(StationOwne_Register.this, String.valueOf(createdUser.getId()), Toast.LENGTH_SHORT).show();
                                            Boolean isUserUpdated = DB.updateUserId(createdUser.getUserName(),createdUser.getId());
                                            if(isUserUpdated){
                                                Toast.makeText(StationOwne_Register.this, "Station Owner Registered Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                Toast.makeText(StationOwne_Register.this, "Station Owner Registered Failure", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }


                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });
                            }
                            else {
                                Toast.makeText(StationOwne_Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StationOwne_Register.this, "Station Owner  already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StationOwne_Register.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}