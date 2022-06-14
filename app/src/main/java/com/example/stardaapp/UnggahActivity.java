package com.example.stardaapp;

import static com.example.stardaapp.FileUtils.getPath;
import static com.example.stardaapp.ProfileActivity.REQUEST_IMAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnggahActivity<doc> extends AppCompatActivity {
    UnggahActivity<doc> mContext;
    BaseApiService mApiService;
    SharedPreferences sharedPreferences;

    ProgressDialog loading;

    Button btnBack,btnPilihMedia,btnPilihDoc1,btnPilihDoc2,btnPilihDoc3,btnUnggah;
    LinearLayout layoutTanggal;
    EditText etJudul,etDeskripsi,etNamaMedia,etKegiatan,etTanggalKegiatan,etNamaDoc1,etNamaDoc2,etNamaDoc3;
    String idUser,judul, deskripsi,namaMedia,kegiatan,tanggalKegiatan,doc1,doc2,doc3, mediaPath,docPath2,docPath3,kategori;
    String docPath;
//    DatePicker
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    Button btnTanggal;

    Spinner spKategoriProduk;

    ArrayList doc;
    RequestBody requestId,requestJudul,requestDeskripsi,requestKegiatan,requestTanggalKegiatan,requestKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unggah);

        mContext = this;
        mApiService = UtilsApi.getAPIService();

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        idUser = sharedPreferences.getString("result_id", null);

        etJudul = findViewById(R.id.etJudul);
        etDeskripsi = findViewById(R.id.etDiskripsi);
        etNamaMedia = findViewById(R.id.etNamaFile);
        etKegiatan = findViewById(R.id.etKegiatan);
        etTanggalKegiatan = findViewById(R.id.etTanggalKegiatan);
        etNamaDoc1 = findViewById(R.id.etNamaDoc1);
//        etNamaDoc2 = findViewById(R.id.etNamaDoc2);
//        etNamaDoc3 = findViewById(R.id.etNamaDoc3);

        btnBack = findViewById(R.id.btnBack);
        btnPilihMedia = findViewById(R.id.btnPilihMedia);
        btnPilihDoc1 = findViewById(R.id.btnPilihDoc1);
//        btnPilihDoc2 = findViewById(R.id.btnPilihDoc2);
//        btnPilihDoc3 = findViewById(R.id.btnPilihDoc3);
        btnUnggah = findViewById(R.id.btnUnggah);
        btnTanggal = findViewById(R.id.btnPilihTanggal);

        layoutTanggal = findViewById(R.id.layoutChoseDate);

        spKategoriProduk = findViewById(R.id.spKategoriUnggahan);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        layoutTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                tampilTanggal();
            }
        });
        btnTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                tampilTanggal();
            }
        });
        etTanggalKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "harap tunggu...", true, false);
                tampilTanggal();
            }
        });
        btnPilihMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqMedia(REQUEST_IMAGE);
            }
        });
        btnPilihDoc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqDoc1(REQUEST_IMAGE);
            }
        });
        btnUnggah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadMultipleFiles();
            }
        });

        initSpinnerKategoriProduk();

        spKategoriProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategori = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinnerKategoriProduk() {

        mApiService.getKategoriProduk().enqueue(new Callback<ResponseKategoriProduk>() {
            @Override
            public void onResponse(Call<ResponseKategoriProduk>call, Response<ResponseKategoriProduk> response) {
                if (response.isSuccessful())
                {

                    List<SemuakategoriprodukItem> semuakategoriprodukItems = response.body().getSemuakategoriproduk();
//                    Log.d("respon", String.valueOf(semuakategoriItems));
                    List<String> listSpinner = new ArrayList<>();


                    for (int i = 0; i < semuakategoriprodukItems.size(); i++) {
                        listSpinner.add(semuakategoriprodukItems.get(i).getKategoriFile());

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spKategoriProduk.setAdapter(adapter);
                    if(kategori != null){
                        spKategoriProduk.setSelection(((ArrayAdapter)spKategoriProduk.getAdapter()).getPosition(kegiatan));
                    }
//                    else{
//                        Toast.makeText(mContext, "Gagal set value adapter kategori", Toast.LENGTH_SHORT).show();
//                    }
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

    void reqMedia(int requestCode){
        switch (requestCode){
            case REQUEST_IMAGE:
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/* video/*");
                    startActivityForResult(galleryIntent, 0);
                    break;
                }else{
                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
        }
    }
    void reqDoc1(int requestCode){
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                String[] filePathColumn = {MediaStore.Files.FileColumns.DATA};
                int path = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(path);
                Log.d("mediapath",mediaPath);

                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String mediaName = cursor.getString(columnIndex);
                etNamaMedia.setText(mediaName);
                // Set the Image in ImageView for Previewing the Media
                cursor.close();

            } // When an doc is picked
            else if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri fileUri = data.getData();
                Cursor cursor = getContentResolver().query(fileUri, null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                docPath = getPath(fileUri, mContext);

//                Log.d("docpath", String.valueOf(docPath));

                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                String docName1 = cursor.getString(columnIndex);
                cursor.close();
                etNamaDoc1.setText(docName1);
            }
        } catch (Exception e) {

            Log.d("ERROR", "Failed Uploading image : " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Error, image.", Toast.LENGTH_LONG).show();

//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
    private void uploadMultipleFiles() {
        loading = ProgressDialog.show(mContext, null, "Tunggu Sebentar...", true, false);;

        List<MultipartBody.Part> parts = new ArrayList<>();
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);
        File file1 = new File(String.valueOf(docPath));

        Log.d("list", parts.toString());
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file1);


        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file1", file1.getName(), requestBody2);

        judul = etJudul.getText().toString();
        deskripsi = etDeskripsi.getText().toString();
        kegiatan = etKegiatan.getText().toString();
        tanggalKegiatan = etTanggalKegiatan.getText().toString();

        requestId = RequestBody.create(MediaType.parse("text/plain"), idUser);
        requestJudul = RequestBody.create(MediaType.parse("text/plain"), judul);
        requestDeskripsi = RequestBody.create(MediaType.parse("text/plain"), deskripsi);
        requestKegiatan = RequestBody.create(MediaType.parse("text/plain"), kegiatan);
        requestTanggalKegiatan = RequestBody.create(MediaType.parse("text/plain"), tanggalKegiatan);
        requestKategori = RequestBody.create(MediaType.parse("text/plain"), kategori);

        mApiService.unggahKarya(requestId,requestJudul,requestKategori, requestDeskripsi,requestKegiatan,requestTanggalKegiatan,fileToUpload1, fileToUpload2).enqueue(new Callback<ResponseUnggah>() {
            @Override
            public void onResponse(Call<ResponseUnggah> call, Response<ResponseUnggah> response) {
                ResponseUnggah serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
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
            public void onFailure(Call<ResponseUnggah> call, Throwable t) {
                loading.dismiss();
                Log.e("ERROR", t.getMessage());
//                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void tampilTanggal() {
        /**
         * Calendar untuk mendapatkan tanggal sekarang
                */
        loading.dismiss();
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                etTanggalKegiatan.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }



}