package com.example.stardaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKaryaActivity extends AppCompatActivity implements DokumenKaryaAdapter.ClickedItem, UnduhanAdapter.ClickedItem{
    private static final int PERMISSION_STORAGE_CODE = 1000;
    LinearLayout layoutJmlUnduh;
    TextView tvJudulKarya, tvNamaPenggunggah, tvKategori, tvKegiatan, tvDeskripsi,tvJmlUnduh,tvJumlahView;
    Button btnBack,btnUnduh;
    ImageView ivMedia;
    VideoView vvVideo;
    RecyclerView recyclerView;
//    DokumenKaryaResponse dokumenKaryaResponse;
    UnduhanAdapter unduhanAdapter;
    DokumenKaryaAdapter dokumenKaryaAdapter;
    BaseApiService mApiService;
    SharedPreferences sharedPreferences;
    ProgressDialog loading,pDialog;
    Context mContext;

    LinearLayout layoutDokumenTerkait;
    String id_Karya, judul_karya, nama_pengunggah, kategori,kegiatan,deskripsi,nama_karya,cat_file,mediaUrl,docUrl,nameDoc;
    KaryaResponse responseKarya;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_karya);

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mContext = this;

        tvJudulKarya = findViewById(R.id.tvJudul);
        tvNamaPenggunggah = findViewById(R.id.tvNamaPengunggah);
        tvKategori = findViewById(R.id.tvKategori);
        tvKegiatan = findViewById(R.id.tvKegiatan);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        tvJmlUnduh = findViewById(R.id.tvJmlUnduh);
        tvJumlahView = findViewById(R.id.tvJmlView);

        btnBack = findViewById(R.id.btnBack);
        btnUnduh = findViewById(R.id.btnUnduh);

        ivMedia = findViewById(R.id.ivMedia);
        vvVideo = findViewById(R.id.vvVideo);

        recyclerView = findViewById(R.id.rvDokumen);
//        recyclerViewUnduhan = findViewById(R.id.recyclerviewUnduhan);
        layoutJmlUnduh = findViewById(R.id.layoutJmlUnduh);
        layoutDokumenTerkait = findViewById(R.id.layoutDokumenTerkait);

        dokumenKaryaAdapter = new DokumenKaryaAdapter(this::ClickedDokumenKarya);
        unduhanAdapter = new UnduhanAdapter(this);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);


//        editDokumenUnggahanAdapter = new DetailKaryaActivity(this);

        loading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        getDokumenKaryaStakeholder();


        Intent intent = getIntent();
        if(intent.getExtras() !=null) {
            responseKarya = (KaryaResponse) intent.getSerializableExtra("data");

            id_Karya = responseKarya.getId_produk();
            judul_karya = responseKarya.getTitle_produk();
            nama_pengunggah = responseKarya.getFullname();
            kategori = responseKarya.getKategori_file();
            deskripsi = responseKarya.getDesc_produk();
            nama_karya = responseKarya.getName_produk();
            cat_file = responseKarya.getCat_file();
            kegiatan = responseKarya.getKegiatan();


            Log.e("judul",judul_karya);

            tvJudulKarya.setText(judul_karya);
            tvNamaPenggunggah.setText(nama_pengunggah);
            tvKategori.setText(kategori);
            tvKegiatan.setText(kegiatan);
            tvDeskripsi.setText(deskripsi);

            String getUrl  = UtilsApi.KaryaUrl;
            mediaUrl = getUrl + nama_karya;
            if(cat_file.contains("photo")){
                vvVideo.setVisibility(View.GONE);
                Glide.with(this).load(mediaUrl).into(ivMedia);
            }else if(cat_file.contains("video")){
                ivMedia.setVisibility(View.GONE);
                vvVideo.setVideoURI(Uri.parse(mediaUrl));

                MediaController mediaController = new MediaController(this);
                vvVideo.start();
                vvVideo.setMediaController(mediaController);
            }
            //btn back
            btnBack = findViewById(R.id.btnBack);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            getJmlUnduhan();
            getJmlViewer();

            layoutJmlUnduh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialogPengunduh();
                }
            });
        }

        String namaUserLogin = sharedPreferences.getString("result_nama",null);
        if (!nama_pengunggah.equals(namaUserLogin)){

            layoutDokumenTerkait.setVisibility(View.GONE);
        }
//        docUrl = folder_url + ;
        btnUnduh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                    }else{
                        startDownload();
                    }
                }else{
                    startDownload();
                }
            }
        });
    }

    private void showDialogPengunduh() {
        final Dialog dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Add person");

        //Mengeset layout
        dialog.setContentView(R.layout.dialog_unduhan);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        //Membuat dialog agar berukuran responsive
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        dialog.getWindow().setLayout((7 * width) / 8, LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerViewUnduhan = dialog.findViewById(R.id.recyclerviewUnduhan);
        Log.d("tes", String.valueOf(unduhanAdapter.getItemCount()));
        recyclerViewUnduhan.setAdapter(unduhanAdapter);
        recyclerViewUnduhan.setLayoutManager(new LinearLayoutManager(this));
//        Button editKarya = (Button) dialog.findViewById(R.id.btn_EditUnggahan);
//        Button editDokumen = (Button) dialog.findViewById(R.id.btn_EditDokumen);
        Button btnTutup = (Button) dialog.findViewById(R.id.btnTutup);
//
//        editKarya.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(mContext, EditUnggahanActivity.class);
////                intent.putExtra("data", unggahanResponse);
////                dialog.dismiss();
////                startActivity(intent);
//            }
//        });
//
//        editDokumen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent intent = new Intent(mContext, EditDokumenUnggahanActivity.class);
////                intent.putExtra("data", unggahanResponse);
////                intent.putExtra("activity_code","1");
////                dialog.dismiss();
////                startActivity(intent);
//            }
//        });
        btnTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, EditUnggahanActivity.class);
//                intent.putExtra("data", unggahanResponse);
                dialog.dismiss();
//                startActivity(intent);
            }
        });

        //Menampilkan custom dialog
        dialog.show();
    }

    private void getJmlUnduhan() {
        Log.e("id", id_Karya);
        mApiService.getJmlUnduhan(id_Karya).enqueue(new Callback<List<UnduhanResponse>>() {
            @Override
            public void onResponse(Call<List<UnduhanResponse>> call, Response<List<UnduhanResponse>> response) {

                if(response.isSuccessful()){;
                    loading.dismiss();
                    List<UnduhanResponse> unduhanResponse = response.body();
                    unduhanAdapter.setData(unduhanResponse);

                    int jmlUnduhan = unduhanAdapter.getItemCount();
//                    Log.e("unduhan", String.valueOf(jmlUnduhan));
                    tvJmlUnduh.setText(String.valueOf(jmlUnduhan));



                }

            }

            @Override
            public void onFailure(Call<List<UnduhanResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }
    private void getJmlViewer(){
        Log.e("id", id_Karya);
        mApiService.getJmlViewer(id_Karya).enqueue(new Callback<List<UnduhanResponse>>() {
            @Override
            public void onResponse(Call<List<UnduhanResponse>> call, Response<List<UnduhanResponse>> response) {

                if(response.isSuccessful()){
                    loading.dismiss();
                    List<UnduhanResponse> unduhanResponse = response.body();
                    unduhanAdapter.setData(unduhanResponse);

                    int jmlViewer = unduhanAdapter.getItemCount();
//                    Log.e("unduhan", String.valueOf(jmlUnduhan));
                    tvJumlahView.setText(String.valueOf(jmlViewer));



                }

            }

            @Override
            public void onFailure(Call<List<UnduhanResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }

    public void getDokumenKaryaStakeholder() {
        Intent intent = getIntent();
        responseKarya = (KaryaResponse) intent.getSerializableExtra("data");

        String id = responseKarya.getId_produk();


        mApiService.getDokumenKarya(id).enqueue(new Callback<List<DokumenKaryaResponse>>() {
            @Override
            public void onResponse(Call<List<DokumenKaryaResponse>> call, Response<List<DokumenKaryaResponse>> response) {

                if(response.isSuccessful()){
                    loading.dismiss();
                    List<DokumenKaryaResponse> obj = response.body();
                    dokumenKaryaAdapter.setData(obj);

                    recyclerView.setAdapter(dokumenKaryaAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<DokumenKaryaResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }
    @Override
    public void ClickedDokumenKarya(DokumenKaryaResponse responseDokumenKarya) {
        nameDoc = responseDokumenKarya.getName_document();
        Log.d("name dokumen", nameDoc);

        String getUrlDoc  = UtilsApi.DocUrl;
        docUrl = getUrlDoc + nameDoc;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,PERMISSION_STORAGE_CODE);
            }else{
                startDownloadDoc();
            }
        }else{
            startDownloadDoc();
        }
    }
    private void startDownloadDoc() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(docUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(mContext.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void startDownload() {
        inputDataUnduh();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mediaUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(mContext.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void inputDataUnduh() {
        String id_produk = id_Karya;
        String id_user = sharedPreferences.getString("result_id", null);;
        mApiService.unduhKarya(id_produk,id_user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("download","berhasil");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("download","gagal");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownload();
                }
                else{
                    Toast.makeText(mContext, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void ClickedPengunduh(UnduhanResponse unduhanResponse) {
//        Intent intent = new Intent(this,DetailStakeholderActivity.class);
//        intent.putExtra("data", unduhanResponse);
//        startActivity(intent);
    }
}