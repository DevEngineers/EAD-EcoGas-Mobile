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

/**
 * This is  Login screen java file to provide logic to activity_main layout xml
 * This screen is to login to the application according to the user types, user will navigate
 * to their particular home Screens According to the type of the user after after User is successfully registered .
 *
 * Author: IT19167442 Nusky M.A.M
 */

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

        /** check user Login **/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_Name = userName.getText().toString();
                String pass = password.getText().toString();

                /** Validation to check  if the login form fields are empty or not when trying to Login **/
                if(user_Name.equals("")||pass.equals(""))
                    Toast.makeText(MainActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkUserPass = DB.checkUserNamePassword(user_Name, pass);
                    if(checkUserPass) {
                        User getUser=DB.getUserData(user_Name);

                        /** Api call to user **/
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

                                                /** check the User type  and redirecting the users to their home Screens according to their type
                                                 * if the user is station Owner redirecting the station owner to station owner Home Screen
                                                 * if the user is Admin redirecting Admin to the Admin Home Screen
                                                 *   * if the user is Normal User redirecting User to the User Profile Screen
                                                 * **/
                                                if(user.getType().equals("User")){
                                                    Intent intent  = new Intent(getApplicationContext(), ViewStations.class);
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
                        checkOtherUsers(user_Name,pass);
                    }
                }
            }
        });


        /** Redirecting to ForgotPassword Screen via Intent **/
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }

        });

        /** Redirecting to register Screen via Intent **/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }

        });

    }

    private void checkOtherUsers(String name,String password) {
        /** Api call to user **/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "User/").addConverterFactory(GsonConverterFactory.create()).build();
        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.getUserDetailsByName(name);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Boolean isUserCreated = DB.insertData(name,password);
                    Boolean isUserUpdated = DB.updateUserId(name,user.getId());
                    if(isUserUpdated && isUserCreated){
                        SessionApplication.setUserName(user.getUserName());
                        SessionApplication.setUserID(user.getId());
                        SessionApplication.setUserType(user.getType());

                        if(user.getType().equals("StationOwner")){
                            Intent intent  = new Intent(getApplicationContext(), StationOwnerHome.class);
                            startActivity(intent);
                        }else if(user.getType().equals("Admin")){
                            Intent intent  = new Intent(getApplicationContext(), AdminHome.class);
                            startActivity(intent);
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Unable to login,Try again", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}