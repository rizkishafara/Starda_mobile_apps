package com.example.stardaapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        mContext = this;
        recyclerView = findViewById(R.id.recyclerview);

        //btn back
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        unggahanAdapter = new UnggahanAdapter(this);
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
    @Override
    public void ClickedHapus(UnggahanResponse unggahanResponse) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Yakin Untuk Menghapus Unggahan?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Yakin untuk hapus!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yakin",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        HapusUnggahan(unggahanResponse);
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void HapusUnggahan(UnggahanResponse unggahanResponse) {
        String id_produk = unggahanResponse.getId_produk();
        Log.d("id_produk",id_produk);

        mApiService.hapusKarya(id_produk).enqueue(new Callback<ResponseHapus>() {
            @Override
            public void onResponse(Call<ResponseHapus> call, Response<ResponseHapus> response) {
                ResponseHapus serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(mContext, UnggahanDiterimaActivity.class);
//                        startActivity(intent);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseHapus> call, Throwable t) {

            }
        });
    }

    @Override
    public void ClickedEdit(UnggahanResponse unggahanResponse) {
        showDialog(unggahanResponse);
    }
    private void showDialog(UnggahanResponse unggahanResponse) {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.dialog_edit_unggahan);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button editKarya = (Button) dialog.findViewById(R.id.btn_EditUnggahan);
        Button editDokumen = (Button) dialog.findViewById(R.id.btn_EditDokumen);
        TextView tutupDialog = (TextView) dialog.findViewById(R.id.tutupDialog);

        editKarya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditUnggahanActivity.class);
                intent.putExtra("data", unggahanResponse);
                dialog.dismiss();
                startActivity(intent);
            }
        });

        editDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Edit Dokumen nanti!", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
                Intent intent = new Intent(mContext, EditDokumenUnggahanActivity.class);
                intent.putExtra("data", unggahanResponse);
                intent.putExtra("activity_code","2");
                dialog.dismiss();
                startActivity(intent);
            }
        });
        tutupDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        //Menampilkan custom dialog
        dialog.show();

    }
    @Override
    public void ClickedAlasan(UnggahanResponse unggahanResponse) {

    }
}