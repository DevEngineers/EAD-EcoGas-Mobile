package com.example.ecogas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecogas.Model.Station;
import com.example.ecogas.Service.StationService;
import com.example.ecogas.ViewAdapters.AdminStationRecycleViewAdepter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminHome extends AppCompatActivity {

    Button btnRegister,btnStation,btnUsers;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btnRegister = findViewById(R.id.btnRegisterStation);
        btnStation = findViewById(R.id.btnStation);
        btnUsers = findViewById(R.id.btnUsers);
        name = findViewById(R.id.adminName);

        name.setText(new StringBuilder().append("Welcome").append(" ").append((SessionApplication.getUserName())));

        /** dding navigation to register new station in app by admin **/
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHome.this,StationOwnerRegister.class);
            startActivity(intent);
        });

        /** dding navigation to view all station in the app **/
        btnStation.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHome.this,AdminViewStations.class);
            startActivity(intent);
        });

        /** dding navigation to view all users in the app **/
        btnUsers.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHome.this,AdminViewUsers.class);
            startActivity(intent);
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
        Intent intent = new Intent(AdminHome.this, MainActivity.class);
        startActivity(intent);
    }

    private void editProfile() {
        /** Redirecting to edit profile via Intent **/
        Intent intent = new Intent(AdminHome.this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** check user is log in**/
        if(SessionApplication.getUserName().equals("")){
            Intent intent = new Intent(AdminHome.this,MainActivity.class);
            startActivity(intent);
        }

    }
}