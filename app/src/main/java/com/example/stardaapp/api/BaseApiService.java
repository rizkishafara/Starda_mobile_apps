package com.example.stardaapp.api;

import android.app.DownloadManager;

import com.example.stardaapp.DokumenKaryaResponse;
import com.example.stardaapp.EditDokumenUnggahanResponse;
import com.example.stardaapp.KaryaResponse;
import com.example.stardaapp.ResponseHapus;
import com.example.stardaapp.ResponseKategori;
import com.example.stardaapp.ResponseKategoriProduk;
import com.example.stardaapp.ResponseKeahlian;
import com.example.stardaapp.ResponseRegister;
import com.example.stardaapp.ResponseUnggah;
import com.example.stardaapp.StakeholdersResponse;
import com.example.stardaapp.UnduhanResponse;
import com.example.stardaapp.UnggahanResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @FormUrlEncoded
    @POST("stakeholder/search_stakeholder")
    Call<List<StakeholdersResponse>> searchStakeholder(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("stakeholder/filter_stakeholder")
    Call<List<StakeholdersResponse>> filterStakeholder(@Field("kategori") String kategori,
                                                       @Field("keahlian") String keahlian);

    @FormUrlEncoded
    @POST("stakeholder/download_stakeholder")
    Call<List<StakeholdersResponse>> downloadStakeholder(@Field("kategori") String kategori,
                                                       @Field("keahlian") String keahlian);

    @FormUrlEncoded
    @POST("karya/search_karya")
    Call<List<KaryaResponse>> searchKarya(@Field("keyword") String keyword);

    @FormUrlEncoded
    @POST("karya/filter_karya")
    Call<List<KaryaResponse>> filterUnggahan(@Field("kategori") String kategori);

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
                                      @Part MultipartBody.Part[] fileDoc);

//    @POST("unggah/unggah_produk")
//    Call<ResponseUnggah> unggahKarya(@Body RequestBody file);

    @FormUrlEncoded
    @POST("unggahan/delete_unggahan/{id_produk}")
    Call<ResponseHapus> hapusKarya(@Field("id_produk") String id_produk);

    @Multipart
    @POST("unggahan/Edit_unggahan")
    Call<ResponseUnggah> editFile(@Part("id_user")RequestBody requestId,
                                    @Part("id_produk")RequestBody requestIdProduk,
                                    @Part("judul")RequestBody requestJudul,
                                    @Part("kategori")RequestBody requestKategori,
                                    @Part("deskripsi")RequestBody requestDeskripsi,
                                    @Part("kegiatan")RequestBody requestKegiatan,
                                    @Part("tgl_kegiatan")RequestBody requestTanggalKegiatan,
                                    @Part("file_lama")RequestBody requestOldFile,
                                    @Part MultipartBody.Part fileToUpload1);

    @Multipart
    @POST("unggahan/Edit_unggahan")
    Call<ResponseUnggah> editFileTanpaFile(@Part("id_user")RequestBody requestId,
                                             @Part("id_produk")RequestBody requestIdProduk,
                                             @Part("judul")RequestBody requestJudul,
                                             @Part("kategori")RequestBody requestKategori,
                                             @Part("deskripsi")RequestBody requestDeskripsi,
                                             @Part("kegiatan")RequestBody requestKegiatan,
                                             @Part("tgl_kegiatan")RequestBody requestTanggalKegiatan,
                                             @Part("file_lama")RequestBody requestOldFile);
    @Multipart
    @POST("unggahan/edit_dokumen")
    Call<EditDokumenUnggahanResponse> editDokumenUnggahan(@Part("id_dokumen") RequestBody requestId,
                                                          @Part MultipartBody.Part fileToUpload1);

    @FormUrlEncoded
    @POST("unggahan/hapus_dokumen")
    Call<EditDokumenUnggahanResponse> hapusDokumenUnggahan(@Field("id_dokumen")String id);

    @Multipart
    @POST("unggahan/tambah_dokumen")
    Call<EditDokumenUnggahanResponse> tambahDokumenUnggahan(@Part("id_produk") RequestBody requestId,
                                                            @Part MultipartBody.Part fileToUpload1);

    @GET("karya/get_unduhan/{id_karya}")
    Call<List<UnduhanResponse>> getJmlUnduhan(@Path("id_karya")String id_karya);

    @GET("karya/get_viewer/{id_karya}")
    Call<List<UnduhanResponse>> getJmlViewer(@Path("id_karya")String id_karya);

    @FormUrlEncoded
    @POST("unggahan/Unduh_unggahan")
    Call<ResponseBody> unduhKarya(@Field("id_produk") String id_produk,
                                  @Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseRegister> register(@Field("fullname")String fullname,
                                    @Field("email")String email,
                                    @Field("phone")String phone,
                                    @Field("selectedKategori")String selectedKategori,
                                    @Field("instansi")String instansi);

    @FormUrlEncoded
    @POST("unggahan/view_unggahan")
    Call<ResponseBody> viewKarya(@Field("id_produk") String id_unggahan,
                                  @Field("id_user") String id_user);

}
