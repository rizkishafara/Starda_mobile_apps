package com.example.stardaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnggahanDitinjauActivity extends AppCompatActivity implements UnggahanAdapter.ClickedItem{
    Context mContext;
    BaseApiService mApiService;
    RecyclerView recyclerView;
    static SharedPreferences sharedPreferences;

    UnggahanAdapter unggahanAdapter;
    Button btnBack;
    UnggahanResponse unggahanResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unggahan_ditinjau);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerview);

        //btn back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        unggahanAdapter = new UnggahanAdapter(this::ClickedUnggahan);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        getAllUngahanDitinjau();
    }
    public void getAllUngahanDitinjau(){

        String idUser = sharedPreferences.getString("result_id", null);
        mApiService.getUnggahanDitinjau(idUser).enqueue(new Callback<List<UnggahanResponse>>() {
            @Override
            public void onResponse(Call<List<UnggahanResponse>> call, Response<List<UnggahanResponse>> response) {

                if(response.isSuccessful()){
                    List<UnggahanResponse> obj = response.body();
                    unggahanAdapter.setData(obj);
                    recyclerView.setAdapter(unggahanAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<UnggahanResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void ClickedUnggahan(UnggahanResponse unggahanResponse) {

        Intent intent = new Intent(this,DetailUnggahanActivity.class);
        intent.putExtra("data", unggahanResponse);
        startActivity(intent);
    }
}