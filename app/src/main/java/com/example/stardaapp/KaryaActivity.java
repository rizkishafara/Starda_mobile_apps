package com.example.stardaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryaActivity extends AppCompatActivity implements KaryaAdapter.ClickedItem{
    Context mContext;
    BaseApiService mApiService;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    KaryaAdapter karyaAdapter;
    Button btnBack, btnSearch;
    EditText etSearch;
    String SearchKeyword;

    ProgressDialog loading;

    RelativeLayout layoutFilter;
    Spinner spKategoriDokumentasi;
    String selectedKategoriDokumentasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karya);
        mContext = this;
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerview);

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

        btnSearch = findViewById(R.id.btnSearch);

        etSearch = findViewById(R.id.etSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchKarya();
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

    private void dialogFilter() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.dialog_filter_unggahan);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((8 * width) / 9, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btnTampil = (Button) dialog.findViewById(R.id.btnTampil);
        Button btnTampilUnduh = (Button) dialog.findViewById(R.id.btnTampilUnduh);
        Button tutupDialog = (Button) dialog.findViewById(R.id.btnTutup);

        spKategoriDokumentasi = dialog.findViewById(R.id.spKategoriUnggahan);

        initSpinnerKategoriDokumentasi();


        spKategoriDokumentasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedKategoriDokumentasi = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUnggahanFilter();
                dialog.dismiss();
            }
        });

        btnTampilUnduh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                downloadStakholderFilter();
                getUnggahanFilter();
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

    private void getUnggahanFilter() {
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.filterUnggahan(selectedKategoriDokumentasi).enqueue(new Callback<List<KaryaResponse>>() {
            @Override
            public void onResponse(Call<List<KaryaResponse>> call, Response<List<KaryaResponse>> response) {
                loading.dismiss();
                List<KaryaResponse> obj = response.body();
                karyaAdapter.setData(obj);
                recyclerView.setAdapter(karyaAdapter);
            }

            @Override
            public void onFailure(Call<List<KaryaResponse>> call, Throwable t) {
                loading.dismiss();
                Log.e("failure",t.getLocalizedMessage());
            }
        });
    }

    private void initSpinnerKategoriDokumentasi() {
        mApiService.getKategoriProduk().enqueue(new Callback<ResponseKategoriProduk>() {
            @Override
            public void onResponse(Call<ResponseKategoriProduk>call, Response<ResponseKategoriProduk> response) {
                if (response.isSuccessful())
                {

                    List<SemuakategoriprodukItem> semuakategoriprodukItems = response.body().getSemuakategoriproduk();
//                    Log.d("respon", String.valueOf(semuakategoriprodukItems));
                    List<String> listSpinner = new ArrayList<>();


                    for (int i = 0; i < semuakategoriprodukItems.size(); i++) {
                        listSpinner.add(semuakategoriprodukItems.get(i).getKategoriFile());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKategoriDokumentasi.setAdapter(adapter);
                    if(selectedKategoriDokumentasi != null){
                        spKategoriDokumentasi.setSelection(((ArrayAdapter)spKategoriDokumentasi.getAdapter()).getPosition(selectedKategoriDokumentasi));
                    }
                } else {
                    loading.dismiss();
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseKategoriProduk> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(mContext, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void searchKarya() {
        SearchKeyword = etSearch.getText().toString();
        mApiService.searchKarya(SearchKeyword).enqueue(new Callback<List<KaryaResponse>>() {
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
        String id_unggahan = responseKarya.getId_produk();
        String id_user = sharedPreferences.getString("result_id", null);
        loading = ProgressDialog.show(mContext, null, "Mengambil Data", true, false);
        mApiService.viewKarya(id_unggahan,id_user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                Intent intent = new Intent(mContext,DetailKaryaActivity.class);
                intent.putExtra("data", responseKarya);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Log.e("failure",t.getLocalizedMessage());
            }
        });

    }

}