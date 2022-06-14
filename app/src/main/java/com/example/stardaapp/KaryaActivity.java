package com.example.stardaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class KaryaActivity extends AppCompatActivity implements KaryaAdapter.ClickedItem{
    Context mContext;
    BaseApiService mApiService;
    RecyclerView recyclerView;

    KaryaAdapter karyaAdapter;
    Button btnBack;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karya);

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

        karyaAdapter = new KaryaAdapter(this::ClickedKarya);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        getAllKarya();
    }

    public void getAllKarya(){

        mApiService.getAllKarya().enqueue(new Callback<List<KaryaResponse>>() {
            @Override
            public void onResponse(Call<List<KaryaResponse>> call, Response<List<KaryaResponse>> response) {

                if(response.isSuccessful()){
                    List<KaryaResponse> obj = response.body();
                    karyaAdapter.setData(obj);
                    recyclerView.setAdapter(karyaAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<KaryaResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void ClickedKarya(KaryaResponse responseKarya) {

        Intent intent = new Intent(this,DetailKaryaActivity.class);
        intent.putExtra("data", responseKarya);
        startActivity(intent);
    }

}