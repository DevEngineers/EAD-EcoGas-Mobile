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

    Button btnRegister;

    private ArrayList<String> stationName = new ArrayList<>();
    private ArrayList<String> ownerName = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        initImageBitmaps();

        btnRegister = findViewById(R.id.btnRegisterStation);

        /** dding navigation to register new station in app by admin **/
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(AdminHome.this,StationOwnerRegister.class);
            startActivity(intent);
        });
    }

    /** Initializing recycle view to view all station data fetched from backend api in the screen
     * Adding all the fetched data to arraylists to view in recycle view **/
    private void initImageBitmaps() {
        /** Api call to retrieve all station details fuel status **/
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SessionApplication.getApiUrl()).addConverterFactory(GsonConverterFactory.create()).build();
        StationService stationService = retrofit.create(StationService.class);
        Call<List<Station>> call = stationService.getAllStationDetails();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.isSuccessful()) {
                    List<Station> stations = response.body();
                    for (Station station: stations){
                        stationName.add(station.getStationName());
                        ownerName.add(station.getOwnerName());
                        location.add(station.getLocation());
                        initRecyclerView();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {

            }
        });
    }

    /** Setting data of all station in recycle view
     *  initiating the recycle view **/
    private void initRecyclerView() {
        RecyclerView recyclerView =(RecyclerView) findViewById(R.id.stationViewLayout);
        AdminStationRecycleViewAdepter adapter = new AdminStationRecycleViewAdepter(stationName,ownerName,location,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
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