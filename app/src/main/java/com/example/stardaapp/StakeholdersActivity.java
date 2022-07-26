package com.example.stardaapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StakeholdersActivity extends AppCompatActivity implements StakeholdersAdapter.ClickedItem {
    Context mContext;
    BaseApiService mApiService;
    RecyclerView recyclerView;

    StakeholdersAdapter stakeholdersAdapter;
    Button btnBack,btnSearch;
    EditText etSearch;
    ProgressDialog loading;
    String SearchKeyword;
    RelativeLayout layoutFilter;
    Spinner spKategori,spKeahlian;
    String selectedKeahlian,selectedKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stakeholders);
        mApiService = UtilsApi.getAPIService();
        mContext = this;

        recyclerView = findViewById(R.id.recyclerview);
        btnSearch = findViewById(R.id.btnSearch);

        etSearch = findViewById(R.id.etSearch);
        layoutFilter = findViewById(R.id.layoutFilter);
        layoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFilter();
            }
        });

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

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        getAllStakeholder();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchStakeholders();
            }
        });
    }

    private void dialogFilter() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.dialog_filter_stakeholder);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btnTampil = (Button) dialog.findViewById(R.id.btnTampil);
        Button btnTampilUnduh = (Button) dialog.findViewById(R.id.btnTampilUnduh);
        Button tutupDialog = (Button) dialog.findViewById(R.id.btnTutup);

        spKategori = dialog.findViewById(R.id.spKategori);
        spKeahlian = dialog.findViewById(R.id.spKeahlian);

        initSpinnerKategori();
        initSpinnerKeahlian();

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedKategori = parent.getItemAtPosition(position).toString();
//                requestDetailDosen(selectedName);
//                Toast.makeText(mContext, "Kamu memilih dosen " + selectedName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKeahlian.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedKeahlian = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStakholderFilter();
                dialog.dismiss();
            }
        });

        btnTampilUnduh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadStakholderFilter();
                dialog.dismiss();
            }
        });

        tutupDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //Menampilkan custom dialog
        dialog.show();
    }

    private void initSpinnerKategori() {
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.getKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori>call, Response<ResponseKategori> response) {
                if (response.isSuccessful())
                {

                    loading.dismiss();
                    List<SemuakategoriItem> semuakategoriItems = response.body().getSemuakategori();
//                    Log.d("respon", String.valueOf(semuakategoriItems));
                    List<String> listSpinner = new ArrayList<>();


                    for (int i = 0; i < semuakategoriItems.size(); i++) {
                        listSpinner.add(semuakategoriItems.get(i).getCategory_user());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKategori.setAdapter(adapter);
                    if(selectedKategori != null){
                        spKategori.setSelection(((ArrayAdapter)spKategori.getAdapter()).getPosition(selectedKategori));
                    }
//                    else if(spKategori.setOnItemSelectedListener()){
//                        spKategori.noSel;
//                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

//

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }


//
        });
    }
    private void initSpinnerKeahlian() {

        mApiService.getKeahlian().enqueue(new Callback<ResponseKeahlian>() {
            @Override
            public void onResponse(Call<ResponseKeahlian>call, Response<ResponseKeahlian> response) {
                if (response.isSuccessful())
                {

                    List<SemuakeahlianItem> semuakeahlianItems = response.body().getSemuakeahlian();
//                    Log.d("respon", String.valueOf(semuakategoriItems));
                    List<String> listSpinner = new ArrayList<>();


                    for (int i = 0; i < semuakeahlianItems.size(); i++) {
                        listSpinner.add(semuakeahlianItems.get(i).getKeahlian_user());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKeahlian.setAdapter(adapter);
                    if(selectedKeahlian != null){
                        spKeahlian.setSelection(((ArrayAdapter)spKeahlian.getAdapter()).getPosition(selectedKeahlian));
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseKeahlian> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void getStakholderFilter() {
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.filterStakeholder(selectedKategori,selectedKeahlian).enqueue(new Callback<List<StakeholdersResponse>>() {
            @Override
            public void onResponse(Call<List<StakeholdersResponse>> call, Response<List<StakeholdersResponse>> response) {
                loading.dismiss();
                List<StakeholdersResponse> obj = response.body();
                stakeholdersAdapter.setData(obj);
                recyclerView.setAdapter(stakeholdersAdapter);
            }

            @Override
            public void onFailure(Call<List<StakeholdersResponse>> call, Throwable t) {
                loading.dismiss();
                Log.e("failure",t.getLocalizedMessage());
            }
        });
    }
    private void downloadStakholderFilter(){
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.downloadStakeholder(selectedKategori,selectedKeahlian).enqueue(new Callback<List<StakeholdersResponse>>() {
            @Override
            public void onResponse(Call<List<StakeholdersResponse>> call, Response<List<StakeholdersResponse>> response) {
                loading.dismiss();
                List<StakeholdersResponse> obj = response.body();
                stakeholdersAdapter.setData(obj);
                recyclerView.setAdapter(stakeholdersAdapter);
            }

            @Override
            public void onFailure(Call<List<StakeholdersResponse>> call, Throwable t) {
                loading.dismiss();
                Log.e("failure",t.getLocalizedMessage());
            }
        });
    }

    private void searchStakeholders() {
        SearchKeyword = etSearch.getText().toString();

        mApiService.searchStakeholder(SearchKeyword).enqueue(new Callback<List<StakeholdersResponse>>() {
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