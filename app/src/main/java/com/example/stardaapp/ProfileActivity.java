package com.example.stardaapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.BaseApiService;
import com.example.stardaapp.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    Context mContext;
    BaseApiService mApiService;

    String profileUrl,kelamin,id,nama, address,email, phone, photo,category,keahlian,gender,instansi,about,simpan, selectedKategori, selectedKeahlian;
    EditText etnama,etaddress,etemail,etphone,imgphoto,etcategory,etkeahlian,etgender,etinstansi,etabout;
    Spinner spKategori, spKeahlian;
    ProgressDialog loading;
    RadioGroup rgGender;
    RadioButton rbMale,rbFemale,rbGender;
    RelativeLayout btnSimpan;
    Button btnBack;
    static SharedPreferences sharedPreferences;
    ImageView profile, edit_image;
    public static final int REQUEST_IMAGE = 100;

    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        ButterKnife.bind(this);
        mContext = this;
        mApiService = UtilsApi.getAPIService();
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        Bundle extras=getIntent().getExtras();

        id=extras.getString("id");
        nama=extras.getString("nama");
        address=extras.getString("address");
        email=extras.getString("email");
        phone=extras.getString("phone");
        photo=extras.getString("photo");
        category=extras.getString("category");
        keahlian=extras.getString("keahlian");
        gender=extras.getString("gender");
        instansi=extras.getString("instansi");
        about=extras.getString("about");

        etnama= (EditText) findViewById(R.id.etNama);
        etemail= (EditText) findViewById(R.id.etEmail);
        etaddress= (EditText) findViewById(R.id.etAlamat);
        etphone= (EditText) findViewById(R.id.etPhone);
        etinstansi= (EditText) findViewById(R.id.etInstansi);
        etabout= (EditText) findViewById(R.id.etAbout);
        spKategori= (Spinner) findViewById(R.id.spKategori);
        spKeahlian= (Spinner) findViewById(R.id.spKeahlian);

        btnSimpan = (RelativeLayout) findViewById(R.id.btnSimpan);

        btnBack = (Button) findViewById(R.id.btnBack);

        rbMale=(RadioButton) findViewById(R.id.rbMale);
        rbFemale=(RadioButton) findViewById(R.id.rbFemale);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

        profile = (ImageView) findViewById(R.id.user_image);
        edit_image = (ImageView) findViewById(R.id.edit_image);


//        set selected geder from db
        String gend = String.valueOf(gender);
        Log.d("nama", String.valueOf(etnama));

        if ("male".equals(gend)) {
            rbMale.setChecked(true);
        } else if ("female".equals(gend)) {
            rbFemale.setChecked(true);
        }


        etnama.setText(nama);
        etemail.setText(email);
        etaddress.setText(address);
        etphone.setText(phone);
        etinstansi.setText(instansi);
        etabout.setText(about);


        initSpinnerKategori();
        initSpinnerKeahlian();



        profileUrl = UtilsApi.ProfileUrl+sharedPreferences.getString("result_photo",null);
        Log.d("link_profile",profileUrl);

        Glide.with(this)
                .load(profileUrl)
                .centerCrop()
                .into(profile);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get selected radio button from radioGroup
                int selectedId = rgGender.getCheckedRadioButtonId();
                rbGender=(RadioButton) findViewById(selectedId);

                if ("Laki - laki".equals(rbGender.getText())) {
                    kelamin = "male";
                } else if ("Perempuan".equals(rbGender.getText())) {
                    kelamin = "female";
                }
//                Log.d("keahlian",selectedKeahlian);
//                Toast.makeText(mContext, selectedKeahlian, Toast.LENGTH_SHORT).show();
                loading = ProgressDialog.show(mContext, null, "Sedang menyimpan data...", true, false);
                updatedata();
            }
        });

        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                execute(REQUEST_IMAGE);
            }
        });

//
    }
    void execute(int requestCode){
        switch (requestCode){
            case REQUEST_IMAGE:
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
                    break;
                }else{
                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
        }
    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
    void uploadFile(Uri contentURI){
        String resultId = sharedPreferences.getString("result_id", null);
        String filePath = getRealPathFromURIPath(contentURI,this);
        File file = new File(filePath);
        Log.d("File",""+file.getName());
        Log.d("url", String.valueOf(contentURI));

        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),mFile);

        mApiService.updateFotoProfile(resultId, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    loading.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        Log.d("response", String.valueOf(jsonRESULTS.getJSONObject("user")));
                        if (jsonRESULTS.getString("error").equals("false")) {
                            Toast.makeText(mContext, "Berhasil mengupdate foto", Toast.LENGTH_SHORT).show();
                            String id = jsonRESULTS.getJSONObject("user").getString("id");
                            String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                            String photo = jsonRESULTS.getJSONObject("user").getString("photo");

                            sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            sharedPreferences.contains("id");
                            sharedPreferences.contains("nama");
                            sharedPreferences.contains("photo");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.clear();
                            editor.putString("result_id", id);
                            editor.putString("result_nama", nama);
                            editor.putString("result_photo", photo);
                            editor.apply();

                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                        }else{
                            String error_message = jsonRESULTS.getString("error_msg");
                            Log.d("Error_message",error_message);
                            Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR", "Failed Uploading image : " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error, image.", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            uri = data.getData();
            uploadFile(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatedata() {
        String resultId = sharedPreferences.getString("result_id", null);

        mApiService.updateProfile(resultId,
                etnama.getText().toString(),
                etaddress.getText().toString(),
                etphone.getText().toString(),
                selectedKategori,
                kelamin,
                selectedKeahlian,
                etinstansi.getText().toString(),
                etabout.getText().toString()
        )
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    Toast.makeText(mContext, "Berhasil mengupdate data", Toast.LENGTH_SHORT).show();
                                    String id = jsonRESULTS.getJSONObject("user").getString("id");
                                    String nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    String photo = jsonRESULTS.getJSONObject("user").getString("photo");

                                    sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                    sharedPreferences.contains("id");
                                    sharedPreferences.contains("nama");
                                    sharedPreferences.contains("photo");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.clear();
                                    editor.putString("result_id", id);
                                    editor.putString("result_nama", nama);
                                    editor.putString("result_photo", photo);
                                    editor.apply();

                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Log.d("Error_message",error_message);
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        Toast.makeText(mContext, "Gagal memperbaharui data", Toast.LENGTH_SHORT).show();
                    }
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
                    if(spKeahlian != null){
                        spKeahlian.setSelection(((ArrayAdapter)spKeahlian.getAdapter()).getPosition(keahlian));
                    }else{
                        Toast.makeText(mContext, "Gagal set value adapter keahlian", Toast.LENGTH_SHORT).show();
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
                    if(category != null){
                        spKategori.setSelection(((ArrayAdapter)spKategori.getAdapter()).getPosition(category));
                    }else{
                        Toast.makeText(mContext, "Gagal set value adapter", Toast.LENGTH_SHORT).show();
                    }
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