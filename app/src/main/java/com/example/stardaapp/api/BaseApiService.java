package com.example.stardaapp.api;

import com.example.stardaapp.DokumenKaryaResponse;
import com.example.stardaapp.KaryaResponse;
import com.example.stardaapp.ResponseKategori;
import com.example.stardaapp.ResponseKategoriProduk;
import com.example.stardaapp.ResponseKeahlian;
import com.example.stardaapp.ResponseUnggah;
import com.example.stardaapp.StakeholdersResponse;
import com.example.stardaapp.UnggahanResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BaseApiService {
    @GET("profile/get_profile/{resultId}")
    Call<ResponseBody> getProfile(@Path("resultId") String resultId);

    @GET("profile/get_kategori")
//    Call<String> getKategori();
    Call<ResponseKategori> getKategori();

    @GET("profile/get_keahlian")
    Call<ResponseKeahlian> getKeahlian();

    @GET("unggah/get_kategori_produk")
    Call<ResponseKategoriProduk> getKategoriProduk();

    @GET("stakeholder")
    Call<List<StakeholdersResponse>> getAllStakeholders();

    @GET("stakeholder/get_karya_stakeholder/{id}")
    Call<List<KaryaResponse>> getKaryaStakeholders(@Path("id")String id);

    @GET("stakeholder/get_dokumen/{id}")
    Call<List<DokumenKaryaResponse>> getDokumenKarya(@Path("id")String id);

    @GET("karya")
    Call<List<KaryaResponse>> getAllKarya();

    @GET("unggahan/Unggahan_diterima/{idUser}")
    Call<List<UnggahanResponse>> getUnggahanDiterima(@Path("idUser")String idUser);

    @GET("unggahan/Unggahan_ditinjau/{idUser}")
    Call<List<UnggahanResponse>> getUnggahanDitinjau(@Path("idUser")String idUser);

    @GET("unggahan/Unggahan_ditolak/{idUser}")
    Call<List<UnggahanResponse>> getUnggahanDitolak(@Path("idUser")String idUser);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/login.php
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> loginRequest(@Field("email") String email, @Field("password") String password);

    // Fungsi ini untuk memanggil API http://10.0.2.2/mahasiswa/register.php
    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("nama") String nama, @Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("profile/update_profile/{resultId}")
    Call<ResponseBody> updateProfile(@Path("resultId")String resultId,
                                     @Field("fullname") String nama,
                                     @Field("address") String address,
                                     @Field("phone") String phone,
                                     @Field("category") String kategori,
                                     @Field("gender")String kelamin,
                                     @Field("keahlian")String keahlian,
                                     @Field("instansi") String instansi,
                                     @Field("about") String about);
    @Multipart
    @POST("profile/update_photo_user/{resultId}")
    Call<ResponseBody> updateFotoProfile(@Path("resultId") String resultId,
                                         @Part MultipartBody.Part body);
    @Multipart
    @POST("unggah/unggah_produk")
    Call <ResponseUnggah> unggahKarya(@Part("idUser") RequestBody idUser,
                                      @Part("judul") RequestBody judul,
                                      @Part("kategori") RequestBody kategori,
                                      @Part("deskripsi") RequestBody deskripsi,
                                      @Part("kegiatan") RequestBody kegiatan,
                                      @Part("tanggalKegiatan") RequestBody tanggalKegiatan,
                                      @Part MultipartBody.Part file,
                                      @Part MultipartBody.Part file1);
//    @Multipart
//    @POST("unggah/unggah_produk")
//    Call <ResponseUnggah> unggahKarya(@Part MultipartBody.Part file, @Part List<MultipartBody.Part> parts);


}
