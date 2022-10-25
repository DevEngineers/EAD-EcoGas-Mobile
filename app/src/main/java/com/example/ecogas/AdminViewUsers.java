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

import com.example.ecogas.Model.Station;
import com.example.ecogas.Model.User;
import com.example.ecogas.Service.StationService;
import com.example.ecogas.Service.UserService;
import com.example.ecogas.ViewAdapters.AdminStationRecycleViewAdepter;
import com.example.ecogas.ViewAdapters.AdminUsersRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminViewUsers extends AppCompatActivity {

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> userName = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);
        initImageBitmaps();
    }

    /**
     * Initializing recycle view to view all users data fetched from backend api in the screen
     * Adding all the fetched data to arraylists to view in recycle view
     **/
    private void initImageBitmaps() {
        /** Api call to retrieve all users details  **/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        UserService userService = retrofit.create(UserService.class);
        Call<List<User>> call = userService.getAllUserDetails();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    for (User user : users) {
                        if(!user.getType().equals("Admin")) {
                            name.add(user.getName());
                            userName.add(user.getUserName());
                            initRecyclerView();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    /**
     * Setting data of all users in recycle view
     * initiating the recycle view
     **/
    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.usersViewLayout);
        AdminUsersRecycleViewAdapter adapter = new AdminUsersRecycleViewAdapter(name,userName,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Menu bar actions
     **/
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
        Intent intent = new Intent(AdminViewUsers.this, MainActivity.class);
        startActivity(intent);
    }

    private void editProfile() {
        /** Redirecting to edit profile via Intent **/
        Intent intent = new Intent(AdminViewUsers.this, EditProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /** check user is log in**/
        if (SessionApplication.getUserName().equals("")) {
            Intent intent = new Intent(AdminViewUsers.this, MainActivity.class);
            startActivity(intent);
        }

    }
}