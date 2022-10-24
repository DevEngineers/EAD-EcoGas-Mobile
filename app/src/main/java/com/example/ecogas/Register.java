package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.example.ecogas.Model.Station;
import com.example.ecogas.Model.User;
import com.example.ecogas.Service.DBMaster;
import com.example.ecogas.Service.StationService;
import com.example.ecogas.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    EditText name,userName, password, retypePassword;
    Button signup;
    DBMaster DB;
    TextView signin,shedOwnerSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        userName = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        retypePassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (TextView) findViewById(R.id.btnsignin);
        shedOwnerSignup = (TextView) findViewById(R.id.btnshedownersignup);//for checking
        DB = new DBMaster(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               String userName = username.getText().toString();
                String userPassword = password.getText().toString();
                String userRetypePassword = retypePassword.getText().toString();

                User user = new User();
                user.setUserName(userName.getText().toString());
                user.setName(name.getText().toString());
                user.setType("User");


                if(user.getUserName().equals("")||userPassword.equals("")||userRetypePassword.equals(""))
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(userPassword.equals(userRetypePassword)){
                        Boolean checkuser = DB.checkusername(user.getUserName());

                        if(!checkuser){
                            Boolean insert = DB.insertData(user.getUserName(), userPassword);

                            if(insert){
                                // API call for User
                                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.10:29193/").addConverterFactory(GsonConverterFactory.create()).build();
                                UserService userService = retrofit.create(UserService.class);
                                // create a User
                                Call<User> call = userService.createUser(user);
                                call.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(response.isSuccessful()){
                                            User createdUser=response.body();
//                                            Toast.makeText(Register.this, String.valueOf(createdUser.getId()), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Register.this, "User Registration failed2", Toast.LENGTH_SHORT).show();
                            }
                        }
                        //check if the user Already exist
                        else{
                            Toast.makeText(Register.this, "User already exists! please sign in", Toast.LENGTH_SHORT).show();
                        }
                        //check if the password and re entered password are correct
                    }else{
                        Toast.makeText(Register.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        shedOwnerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StationOwne_Register.class);
                startActivity(intent);
            }
        });
    }
}