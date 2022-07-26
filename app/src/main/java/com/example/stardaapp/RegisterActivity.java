package com.example.stardaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;

    EditText etFullname,etEmail,etPhone,etInstansi;
    Spinner spKategori;
    Button btnLogin,btnRegister;

    String selectedKategori,fullname,email,phone,instansi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        etFullname = findViewById(R.id.inputFullname);
        etEmail = findViewById(R.id.inputEmail);
        etPhone = findViewById(R.id.inputPhone);
        etInstansi = findViewById(R.id.inputInstansi);

        spKategori = findViewById(R.id.spKategori);
        initSpinnerKategori();

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

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {
        fullname = etFullname.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        instansi = etInstansi.getText().toString();

        if (fullname.isEmpty() ||
                email.isEmpty() ||
                phone.isEmpty() ||
                instansi.isEmpty()){
            Toast.makeText(mContext, "Kolom tidak boleh kosong!", Toast.LENGTH_SHORT).show();
        }else{
            loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

            mApiService.register(fullname,email,phone,selectedKategori,instansi).enqueue(new Callback<ResponseRegister>() {
                @Override
                public void onResponse(Call<ResponseRegister> call, Response<ResponseRegister> response) {
                    ResponseRegister serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.getSuccess()) {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        assert serverResponse != null;
                        Log.v("Response", serverResponse.toString());
                    }
                    loading.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseRegister> call, Throwable t) {
                    loading.dismiss();
                    Log.e("ERROR", t.getMessage());
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initSpinnerKategori() {
        loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);

        mApiService.getKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
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
//                    if(category != null){
//                        spKategori.setSelection(((ArrayAdapter)spKategori.getAdapter()).getPosition(category));
//                    }else{
//                        Toast.makeText(mContext, "Gagal set value adapter", Toast.LENGTH_SHORT).show();
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
}