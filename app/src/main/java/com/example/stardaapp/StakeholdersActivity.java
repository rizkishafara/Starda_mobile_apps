package com.example.stardaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StakeholdersActivity extends AppCompatActivity implements StakeholdersAdapter.ClickedItem {
    Context mContext;
    BaseApiService mApiService;
    RecyclerView recyclerView;

    StakeholdersAdapter stakeholdersAdapter;
    Button btnBack;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stakeholders);

        recyclerView = findViewById(R.id.recyclerview);

        //btn back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        stakeholdersAdapter = new StakeholdersAdapter(this::ClickedStakeholder);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        getAllStakeholder();
    }

    public void getAllStakeholder(){

        mApiService.getAllStakeholders().enqueue(new Callback<List<StakeholdersResponse>>() {
            @Override
            public void onResponse(Call<List<StakeholdersResponse>> call, Response<List<StakeholdersResponse>> response) {

                if(response.isSuccessful()){
                    List<StakeholdersResponse> obj = response.body();
                    stakeholdersAdapter.setData(obj);
                    recyclerView.setAdapter(stakeholdersAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<StakeholdersResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void ClickedStakeholder(StakeholdersResponse responseStakeholder) {

        Intent intent = new Intent(this,DetailStakeholderActivity.class);
        intent.putExtra("data", responseStakeholder);
        startActivity(intent);
    }

}