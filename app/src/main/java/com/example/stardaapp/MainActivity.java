package com.example.stardaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RelativeLayout btnProfile, btnUnggah, btnStakeholders, btnGaleri;
    LinearLayout btnDitinjau,btnDitolak,btnDiterima;
    Intent intent;
    BaseApiService mApiService;
    SharedPreferences sharedPreferences;
    String resultId;
    ProgressDialog loading;
    Context mContext;

    TextView tvUsername;
    String resultNama, profileUrl;
    BottomNavigationView bottomNavigation;
    LinearLayout btnDropdownMenu;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        tvUsername = (TextView) findViewById(R.id.username);
        resultNama = sharedPreferences.getString("result_nama",null);
        tvUsername.setText(resultNama);

        btnProfile= findViewById(R.id.btnBiodata);
        btnUnggah= findViewById(R.id.btnMulaiUnggah);
        btnStakeholders= findViewById(R.id.btnStakeholders);
        btnGaleri= findViewById(R.id.btnGaleriKarya);
        btnDropdownMenu = (LinearLayout) findViewById(R.id.btnDropdownMenu);

        btnDiterima = findViewById(R.id.btnUnggahanDiterima);
        btnDitinjau = findViewById(R.id.btnUnggahanDitinjau);
        btnDitolak = findViewById(R.id.btnUnggahanDitolak);

        profile = findViewById(R.id.user_image);

        profileUrl = UtilsApi.ProfileUrl+sharedPreferences.getString("result_photo",null);
        Log.d("link_profile",profileUrl);

        Glide.with(this)
                .load(profileUrl)
                .centerCrop()
                .into(profile);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataUser();
            }
        });
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,UnggahActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });
        btnStakeholders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,StakeholdersActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });
        btnGaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,KaryaActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });

        btnDropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), btnDropdownMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.side_top_navigation, dropDownMenu.getMenu());
                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case(R.id.menu_profile):
                                getDataUser();
                                break;
                            case (R.id.menu_changepswd):
                                Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                                break;
                            case (R.id.menu_logout):
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                Intent login = new Intent(mContext, LoginActivity.class);
                                startActivity(login);
                                break;
                        }
                        return false;
                    }
                });
                dropDownMenu.show();
            }
        });
        btnDitinjau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,UnggahanDitinjauActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });
        btnDiterima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,UnggahanDiterimaActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });
        btnDitolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                intent = new Intent(mContext,UnggahanDitolakActivity.class);
                loading.dismiss();
                mContext.startActivity(intent);
            }
        });
    }

    private void getDataUser() {
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        String resultId = sharedPreferences.getString("result_id", null);
        mApiService.getProfile(resultId ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        String id = jsonRESULTS.getJSONObject("user").getString("id");
                        String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                        String email = jsonRESULTS.getJSONObject("user").getString("email");
                        String photo = jsonRESULTS.getJSONObject("user").getString("photo");
                        String address = jsonRESULTS.getJSONObject("user").getString("address");
                        String phone = jsonRESULTS.getJSONObject("user").getString("phone");
                        String category = jsonRESULTS.getJSONObject("user").getString("category");
                        String keahlian = jsonRESULTS.getJSONObject("user").getString("keahlian");
                        String instansi = jsonRESULTS.getJSONObject("user").getString("instansi");
                        String gender = jsonRESULTS.getJSONObject("user").getString("gender");
                        String about = jsonRESULTS.getJSONObject("user").getString("about");

                        intent = new Intent(mContext,ProfileActivity.class);
                        intent.putExtra("nama",nama);
                        intent.putExtra("id",id);
                        intent.putExtra("email",email);
                        intent.putExtra("photo",photo);
                        intent.putExtra("address",address);
                        intent.putExtra("phone",phone);
                        intent.putExtra("category",category);
                        intent.putExtra("keahlian",keahlian);
                        intent.putExtra("instansi",instansi);
                        intent.putExtra("gender",gender);
                        intent.putExtra("about",about);

                        loading.dismiss();
                        mContext.startActivity(intent);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}