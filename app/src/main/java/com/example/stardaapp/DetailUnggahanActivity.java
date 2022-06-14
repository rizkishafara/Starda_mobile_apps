package com.example.stardaapp;

import static com.example.stardaapp.FileUtils.getPath;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUnggahanActivity extends AppCompatActivity implements DokumenKaryaAdapter.ClickedItem{
    private static final int PERMISSION_STORAGE_CODE = 1000;

    TextView tvJudulKarya, tvNamaPenggunggah, tvKategori, tvKegiatan, tvDeskripsi;
    Button btnBack,btnUnduh;
    ImageView ivMedia;
    VideoView vvVideo;
    RecyclerView recyclerView;
    //    DokumenKaryaResponse dokumenKaryaResponse;
    DokumenKaryaAdapter dokumenKaryaAdapter;
    BaseApiService mApiService;
    ProgressDialog loading,pDialog;
    Context mContext;

    UnggahanResponse unggahanResponse;
    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    private static String folder_url = "http://10.0.2.2/starda_v2/storage/doc_media_user/";
    private long downloadID;

    String id_Karya, judul_karya, nama_pengunggah, kategori,kegiatan,deskripsi,nama_karya,cat_file,mediaUrl,docUrl,nameDoc;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_unggahan);

        mContext = this;

        tvJudulKarya = findViewById(R.id.tvJudul);
        tvNamaPenggunggah = findViewById(R.id.tvNamaPengunggah);
        tvKategori = findViewById(R.id.tvKategori);
        tvKegiatan = findViewById(R.id.tvKegiatan);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);

        btnBack = findViewById(R.id.btnBack);
        btnUnduh = findViewById(R.id.btnUnduh);

        ivMedia = findViewById(R.id.ivMedia);
        vvVideo = findViewById(R.id.vvVideo);

        recyclerView = findViewById(R.id.rvDokumen);

        dokumenKaryaAdapter = new DokumenKaryaAdapter(this::ClickedDokumenKarya);
        mApiService = UtilsApi.getAPIService();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        loading = ProgressDialog.show(this, null, "harap tunggu...", true, false);
        getDokumenKaryaStakeholder();


        Intent intent = getIntent();
        if(intent.getExtras() !=null) {
            unggahanResponse = (UnggahanResponse) intent.getSerializableExtra("data");

            id_Karya = unggahanResponse.getId_produk();
            judul_karya = unggahanResponse.getTitle_produk();
            nama_pengunggah = unggahanResponse.getFullname();
            kategori = unggahanResponse.getKategori_file();
            deskripsi = unggahanResponse.getDesc_produk();
            nama_karya = unggahanResponse.getName_produk();
            cat_file = unggahanResponse.getCat_file();
            kegiatan = unggahanResponse.getKegiatan();


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

        docUrl = folder_url + nama_karya;
    }
    private void getDokumenKaryaStakeholder() {
        Intent intent = getIntent();
        unggahanResponse = (UnggahanResponse) intent.getSerializableExtra("data");

        String id = unggahanResponse.getId_produk();

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
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        request.setTitle(nameDoc);
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(mContext.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void startDownload() {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mediaUrl));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(judul_karya);
        request.setDescription("Downloading file...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager) getSystemService(mContext.DOWNLOAD_SERVICE);
        manager.enqueue(request);
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


}