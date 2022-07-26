package com.example.stardaapp;

import static com.example.stardaapp.FileUtils.getPath;
import static com.example.stardaapp.ProfileActivity.REQUEST_IMAGE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDokumenUnggahanActivity extends AppCompatActivity implements EditDokumenUnggahanAdapter.ClickedItem {
    Context mContext;
    BaseApiService mApiService;
    SharedPreferences sharedPreferences;

    ProgressDialog loading;

    Button btnBack;

    UnggahanResponse unggahanResponse;

    RecyclerView recyclerView;
    EditDokumenUnggahanAdapter editDokumenUnggahanAdapter;
    String resIdProduk,docPath,activityCode;
    Button btnPilihDoc,btnTambahDokumen;
    EditText etNamaDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dokumen_unggahan);
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

        btnTambahDokumen = findViewById(R.id.btnTambahDokumen);
        btnTambahDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_edit_dokumen_unggahan, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setTitle("Form Tambah Dokumen");

                btnPilihDoc    = (Button) dialogView.findViewById(R.id.btnPilihDoc);
                etNamaDoc    = (EditText) dialogView.findViewById(R.id.etNamaDoc);

                btnPilihDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reqDoc(REQUEST_IMAGE);
                    }
                });

                dialog.setPositiveButton("Unggah", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tambahDokumen(resIdProduk);

                        dialog.dismiss();
                    }
                });

                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Intent intent = getIntent();


        if(intent.getExtras() !=null) {
            activityCode=intent.getExtras().getString("activity_code");

            unggahanResponse = (UnggahanResponse) intent.getSerializableExtra("data");
            resIdProduk = unggahanResponse.getId_produk();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        editDokumenUnggahanAdapter = new EditDokumenUnggahanAdapter(this);
        mApiService = UtilsApi.getAPIService();
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(mLayoutManager);

        getDokumen();

    }

    private void tambahDokumen(String resIdProduk) {
        Log.d("id",resIdProduk);

        File fileDoc = new File(docPath);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), fileDoc);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("fileDoc", fileDoc.getName(), requestBody1);
        RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), resIdProduk);

        mApiService.tambahDokumenUnggahan(requestId,fileToUpload1).enqueue(new Callback<EditDokumenUnggahanResponse>() {
            @Override
            public void onResponse(Call<EditDokumenUnggahanResponse> call, Response<EditDokumenUnggahanResponse> response) {
                EditDokumenUnggahanResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, UnggahanDitinjauActivity.class);
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
            public void onFailure(Call<EditDokumenUnggahanResponse> call, Throwable t) {
                loading.dismiss();
                Log.e("ERROR", t.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getDokumen() {
        String id = resIdProduk;

        mApiService.getDokumenKarya(id).enqueue(new Callback<List<DokumenKaryaResponse>>() {
            @Override
            public void onResponse(Call<List<DokumenKaryaResponse>> call, Response<List<DokumenKaryaResponse>> response) {

                if(response.isSuccessful()){
                    List<DokumenKaryaResponse> obj = response.body();
                    editDokumenUnggahanAdapter.setData(obj);
                    recyclerView.setAdapter(editDokumenUnggahanAdapter);

//                    Log.e("hasil", String.valueOf(editDokumenUnggahanAdapter.getItemCount()));
                    int jumlahDoc=editDokumenUnggahanAdapter.getItemCount();
                    if (jumlahDoc>=3){
                        btnTambahDokumen.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<DokumenKaryaResponse>> call, Throwable t) {
                Log.e("failure",t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void ClickedEditDokumen(DokumenKaryaResponse responseDokumenKarya) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_dokumen_unggahan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Form Edit Dokumen");

        btnPilihDoc    = (Button) dialogView.findViewById(R.id.btnPilihDoc);
        etNamaDoc    = (EditText) dialogView.findViewById(R.id.etNamaDoc);

        btnPilihDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqDoc(REQUEST_IMAGE);
            }
        });

        dialog.setPositiveButton("Unggah", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                editDokumenUnggahan(responseDokumenKarya);

                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void reqDoc(int requestCode) {
        switch (requestCode){
            case REQUEST_IMAGE:
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("*/*");
                    galleryIntent = Intent.createChooser(galleryIntent, "Choose a file");
                    startActivityForResult(galleryIntent, 1);
                    break;
                }else{
                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
        }
    }

    private void editDokumenUnggahan(DokumenKaryaResponse responseDokumenKarya) {
        String id_dokumen = responseDokumenKarya.getId_document();

        File fileDoc = new File(docPath);
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), fileDoc);
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("fileDoc", fileDoc.getName(), requestBody1);
        RequestBody requestId = RequestBody.create(MediaType.parse("text/plain"), id_dokumen);
        mApiService.editDokumenUnggahan(requestId,fileToUpload1).enqueue(new Callback<EditDokumenUnggahanResponse>() {
            @Override
            public void onResponse(Call<EditDokumenUnggahanResponse> call, Response<EditDokumenUnggahanResponse> response) {
                EditDokumenUnggahanResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, UnggahanDitinjauActivity.class);
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
            public void onFailure(Call<EditDokumenUnggahanResponse> call, Throwable t) {
                loading.dismiss();
                Log.e("ERROR", t.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                Uri fileUri = data.getData();
                Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                docPath = getPath(fileUri, mContext);
//                Log.d("docpath", String.valueOf(docPath));

                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String docName = cursor.getString(columnIndex);
                cursor.close();
                etNamaDoc.setText(docName);

            }
        } catch (Exception e) {

            Log.d("ERROR", "Failed Uploading image : " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error, image.", Toast.LENGTH_LONG).show();

//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void ClickedHapusDokumen(DokumenKaryaResponse responseDokumenKarya) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Yakin Untuk Menghapus Dokumen?");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Yakin untuk hapus!")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yakin",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        HapusDokumen(responseDokumenKarya);
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

    private void HapusDokumen(DokumenKaryaResponse responseDokumenKarya) {
        String id = responseDokumenKarya.getId_document();

        mApiService.hapusDokumenUnggahan(id).enqueue(new Callback<EditDokumenUnggahanResponse>() {
            @Override
            public void onResponse(Call<EditDokumenUnggahanResponse> call, Response<EditDokumenUnggahanResponse> response) {
                EditDokumenUnggahanResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("kode",activityCode);
                        if (activityCode.equals("1")){
                            Intent intent = new Intent(mContext, UnggahanDiterimaActivity.class);
                            startActivity(intent);
                        }else if (activityCode.equals("2")){
                            Intent intent = new Intent(mContext, UnggahanDitinjauActivity.class);
                            startActivity(intent);

                        }else if (activityCode.equals("3")){
                            Intent intent = new Intent(mContext, UnggahanDitolakActivity.class);
                            startActivity(intent);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<EditDokumenUnggahanResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

}