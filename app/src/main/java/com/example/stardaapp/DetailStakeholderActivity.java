package com.example.stardaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailStakeholderActivity extends AppCompatActivity implements KaryaAdapter.ClickedItem{

    TextView tvFullname, tvAlamat, tvEmail, tvPhone, tvInstansi, tvKeahlian, tvAbout;
    ImageView ivPhoto;
    String Fullname,Alamat,Email,Phone,Instansi,Keahlian,About,Photo,imgurl,id,cat_file;
    StakeholdersResponse stakeholdersResponse;
    RecyclerView recyclerView;
    KaryaAdapter karyaAdapter;
    BaseApiService mApiService;
    int imageResource;
    ProgressDialog loading;
    Button btnBack;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_stakeholder);

        tvFullname = findViewById(R.id.tvFullname);
        tvAlamat = findViewById(R.id.tvAlamat);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvInstansi = findViewById(R.id.tvInstansi);
        tvKeahlian = findViewById(R.id.tvKeahlian);
        tvAbout = findViewById(R.id.tvAbout);
        ivPhoto = findViewById(R.id.ivPhoto);

        recyclerView = findViewById(R.id.rvGallery);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);


        karyaAdapter = new KaryaAdapter(this::ClickedKarya);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        loading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        getKaryaStakeholder();

        Intent intent = getIntent();
        if(intent.getExtras() !=null) {
            stakeholdersResponse = (StakeholdersResponse) intent.getSerializableExtra("data");

            id = stakeholdersResponse.getId_user();
            Fullname = stakeholdersResponse.getFullname();
            Alamat = stakeholdersResponse.getAddress_user();
            Email = stakeholdersResponse.getEmail();
            Phone = stakeholdersResponse.getPhone_user();
            Instansi = stakeholdersResponse.getInstansi();
            Keahlian = stakeholdersResponse.getKeahlian_user();
            About = stakeholdersResponse.getAbout();
            Photo = stakeholdersResponse.getPhoto_user();

            tvFullname.setText(Fullname);
            tvInstansi.setText(Instansi);
            tvKeahlian.setText(Keahlian);
            tvAbout.setText(About);

            String namaUserLogin = sharedPreferences.getString("result_nama",null);
            if(namaUserLogin == null){
                tvAlamat.setText(Alamat.replaceAll(Alamat,"*****************"));
                tvEmail.setText(Email.replaceAll(Email,"************@gmail.com"));
                tvPhone.setText(Phone.replaceAll(Phone,"+62***********"));
            }else{
                tvAlamat.setText(Alamat);
                tvEmail.setText(Email);
                tvPhone.setText(Phone);
            }



            String getUrl  = UtilsApi.ProfileUrl;
            imgurl = getUrl + Photo;
            Glide.with(this).load(imgurl).into(ivPhoto);
        }

        //btn back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getKaryaStakeholder() {
        Intent intent = getIntent();
        stakeholdersResponse = (StakeholdersResponse) intent.getSerializableExtra("data");

        id = stakeholdersResponse.getId_user();

        mApiService.getKaryaStakeholders(id).enqueue(new Callback<List<KaryaResponse>>() {
            @Override
            public void onResponse(Call<List<KaryaResponse>> call, Response<List<KaryaResponse>> response) {

                if(response.isSuccessful()){
                    loading.dismiss();
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
        Log.d("response", String.valueOf(responseKarya));
        Intent intent = new Intent(this,DetailKaryaActivity.class);
        intent.putExtra("data", responseKarya);
        startActivity(intent);
    }
}