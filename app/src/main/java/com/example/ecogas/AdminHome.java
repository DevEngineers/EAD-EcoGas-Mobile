package com.example.ecogas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

        btnRegister.setOnClickListener(view -> {
//            Intent intent = new Intent(AdminHome.this,MainActivity.class);
//            startActivity(intent);
        });
    }

    private void initImageBitmaps() {
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

    private void initRecyclerView() {
        RecyclerView recyclerView =(RecyclerView) findViewById(R.id.stationViewLayout);
        AdminStationRecycleViewAdepter adapter = new AdminStationRecycleViewAdepter(stationName,ownerName,location,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }
}