package com.example.stardaapp.api;

public class UtilsApi {
    // 10.0.2.2 ini adalah localhost.
//    public static final String BASE_URL_API = "https://starda.allif.my.id/index.php/mobile/";
    public static final String BASE_URL_API = "http://10.0.2.2/starda_v2/index.php/mobile/";
//    public static final String ProfileUrl = "https://starda.allif.my.id/storage/profile_user/";
    public static final String ProfileUrl = "http://10.0.2.2/starda_v2/storage/profile_user/";
//    public static final String KaryaUrl = "https://starda.allif.my.id/storage/media_user/";
    public static final String KaryaUrl = "http://10.0.2.2/starda_v2/storage/media_user/";
//    public static final String BASE_URL_API = "http://172.10.12.64/starda_v2/index.php/mobile/";
    public static final String DocUrl = "http://10.0.2.2/starda_v2/storage/doc_media_user/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
