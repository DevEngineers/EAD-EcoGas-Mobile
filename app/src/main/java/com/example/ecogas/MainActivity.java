package com.example.ecogas;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    EditText userName, password;
    Button btnLogin;
    DBMaster DB;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnLogin = (Button) findViewById(R.id.btnsignin1);
        final TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword1);
        final TextView signup = (TextView) findViewById(R.id.register1);
        DB = new DBMaster(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_Name = userName.getText().toString();
                String pass = password.getText().toString();

                if(user_Name.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkUserPass = DB.checkUserNamePassword(user_Name, pass);
                    if(checkUserPass) {
                        User getUser=DB.getUserData(user_Name);
                                     // API call for User
                                    Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "User/").addConverterFactory(GsonConverterFactory.create()).build();
                                    UserService userService = retrofit.create(UserService.class);
                                    Call<User> call = userService.getUserDetails(getUser.getId());
                                    call.enqueue(new Callback<User>() {
                                        @Override
                                        public void onResponse(Call<User> call, Response<User> response) {
                                            if(response.isSuccessful()){
                                                User user =response.body();
                                                SessionApplication.setUserName(user.getUserName());
                                                SessionApplication.setUserID(user.getId());
                                                SessionApplication.setUserType(user.getType());

                                                if(user.getType().equals("User")){
                                                    Intent intent  = new Intent(getApplicationContext(), EditProfile.class);
                                                    startActivity(intent);
                                                }
                                                else if(user.getType().equals("StationOwner")){
                                                    Intent intent  = new Intent(getApplicationContext(), StationOwnerHome.class);
                                                    startActivity(intent);
                                                }
                                                else if(user.getType().equals("Admin")){
                                                    Intent intent  = new Intent(getApplicationContext(), AdminHome.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<User> call, Throwable t) {

                                        }
                                    });
                    } else{
                        checkForStationOwner(user_Name,pass);
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }

        });

    }

    private void checkForStationOwner(String name,String password) {
        Boolean isUserCreated = DB.insertData(name,password);
        if(isUserCreated){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "User/").addConverterFactory(GsonConverterFactory.create()).build();
            UserService userService = retrofit.create(UserService.class);
            Call<User> call = userService.getUserDetailsByName(name);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        User user = response.body();
                        if(user.getType().equals("StationOwner")){
                            Boolean isUserUpdated = DB.updateUserId(name,user.getId());
                            if(isUserUpdated){
                                Intent intent  = new Intent(getApplicationContext(), StationOwnerHome.class);
                                startActivity(intent);
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }
}