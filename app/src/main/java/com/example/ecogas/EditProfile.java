package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

                    User getUser=DB.getUserData(SessionApplication.getUserName());
                    password.setText(String.valueOf(getUser.getPassword()));

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
                user.setName(name.getText().toString());
                user.setUserName(userName.getText().toString());
                user.setPassword(password.getText().toString());


                Boolean isUpdated = DB.updateUserData(SessionApplication.getUserID(),user.getUserName(),user.getPassword());
                if(isUpdated){
                    user.setPassword("");
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.10:29193/User").addConverterFactory(GsonConverterFactory.create()).build();
                    UserService userService = retrofit.create(UserService.class);
                    Call<User> call = userService.updateUserDetails(SessionApplication.getUserID(),user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                    Toast.makeText(EditProfile.this, "User Updated Successfully", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                                    startActivity(intent);
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
        });

    }
}