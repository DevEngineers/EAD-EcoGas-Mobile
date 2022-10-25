package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.example.ecogas.Model.User;
import com.example.ecogas.Service.DBMaster;
import com.example.ecogas.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
    EditText name,userName,password;
    Button submitEditProfile,delete;
    DBMaster DB;
    User userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.Editname);
        userName = (EditText) findViewById(R.id.Editusername);
        password = (EditText) findViewById(R.id.Editpassword);
        submitEditProfile = (Button) findViewById(R.id.btnEditSubmit);
        delete = (Button) findViewById(R.id.btnDeleteuser);
        DB = new DBMaster(this);
        userData = new User();

        if(SessionApplication.getUserType().equals("Admin")){
            delete.setVisibility(View.GONE);
        }


        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "User/").addConverterFactory(GsonConverterFactory.create()).build();
        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.getUserDetails(SessionApplication.getUserID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user =response.body();
                    name.setText(String.valueOf(user.getName()));
                    userName.setText(String.valueOf(user.getUserName()));

                    User getUser = DB.getUserData(user.getUserName());
                    password.setText(String.valueOf(getUser.getPassword()));

                    userData.setUserName(user.getUserName());
                    userData.setName(user.getName());
                    userData.setPassword(getUser.getPassword());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        submitEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setName(name.getText().toString().trim());
                user.setUserName(userName.getText().toString().trim());
                user.setPassword(password.getText().toString().trim());

                if(user.getUserName().equals(userData.getUserName()) && user.getName().equals(userData.getName()) && user.getPassword().equals(userData.getPassword())){
                    Toast.makeText(EditProfile.this, "Please update any field to update your user profile ", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean isUpdated = DB.updateUserData(SessionApplication.getUserID(),user.getUserName(),user.getPassword());

                    if(isUpdated){
                        user.setPassword("");
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl() + "User/").addConverterFactory(GsonConverterFactory.create()).build();
                        UserService userService = retrofit.create(UserService.class);
                        Call<User> call = userService.updateUserDetails(SessionApplication.getUserID(),user);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
                                    if(SessionApplication.getUserType().equals("StationOwner")){
                                        Intent intent = new Intent(getApplicationContext(), StationOwnerHome.class);
                                        startActivity(intent);
                                    }else if(SessionApplication.getUserType().equals("Admin")){
                                        Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                                        startActivity(intent);

                                    }else if(SessionApplication.getUserType().equals("User")) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                else{
                                    Toast.makeText(EditProfile.this, "User Updated Failure", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });
                    }
                }

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /** Menu bar actions**/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                editProfile();
                return true;
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        SessionApplication.setUserID("");
        SessionApplication.setUserName("");
        SessionApplication.setUserType("");
        SessionApplication.setStationID("");

        /** Redirecting to login screen after logout via Intent **/
        Intent intent = new Intent(EditProfile.this, MainActivity.class);
        startActivity(intent);
    }

    private void editProfile() {
        /** Redirecting to edit profile via Intent **/
        Intent intent = new Intent(EditProfile.this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** check user is log in**/
        if(SessionApplication.getUserName().equals("")){
            Intent intent = new Intent(EditProfile.this,MainActivity.class);
            startActivity(intent);
        }

    }
}