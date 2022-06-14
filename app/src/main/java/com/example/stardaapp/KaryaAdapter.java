package com.example.stardaapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.UtilsApi;

import java.util.List;

public class KaryaAdapter extends RecyclerView.Adapter<KaryaAdapter.KaryaAdapterVH>{
    private List<KaryaResponse> responseKaryaList;
    private Context mContext;
    String id_produk,title_produk,name_produk,desc_produk,kategori_file,cat_file,status_produk,upload_date,uri;
    View layoutGallery;
    private KaryaAdapter.ClickedItem clickedItem;
    int imageResource;

    public void setData(List<KaryaResponse> responseKaryaList) {
        this.responseKaryaList = responseKaryaList;
        notifyDataSetChanged();
    }

    // click adapter
    public KaryaAdapter(KaryaAdapter.ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedKarya(KaryaResponse responseKarya);
    }



    //click adapter

    @NonNull
    @Override
    public KaryaAdapter.KaryaAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new KaryaAdapter.KaryaAdapterVH(LayoutInflater.from(mContext).inflate(R.layout.row_karya, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull KaryaAdapter.KaryaAdapterVH holder, int position) {
        KaryaResponse responseKarya = responseKaryaList.get(position);
        Log.d("bisaya", String.valueOf(responseKarya));
        id_produk = responseKarya.getId_produk();
        title_produk = responseKarya.getTitle_produk();
//        name_produk = responseKarya.getName_produk();
//        desc_produk = responseKarya.getDesc_produk();
//        kategori_file = responseKarya.getKategori_file();
        cat_file = responseKarya.getCat_file();
//        status_produk = responseKarya.getStatus_produk();
//        upload_date = responseKarya.getUpload_date();

        if(cat_file.contains("photo")){
             uri = "@drawable/image_solid";  // where myresource (without the extension) is the file
        }else if(cat_file.contains("video")){
             uri = "@drawable/film_solid";  // where myresource (without the extension) is the file
        }
        holder.setImage(uri);
        holder.tvTitleProduk.setText(title_produk);

        //click adapter
        layoutGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedKarya(responseKarya);
            }
        });


    }
    @Override
    public int getItemCount() {
        return responseKaryaList.size();
    }

    public class KaryaAdapterVH extends RecyclerView.ViewHolder {
        TextView tvTitleProduk;
        ImageView ivIcon;

        public KaryaAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvTitleProduk = itemView.findViewById(R.id.tvTitleProduk);

            layoutGallery = itemView.findViewById(R.id.layoutGallery);
        }

        public void setImage(String uri) {
            ivIcon = itemView.findViewById(R.id.ivIcon);
            imageResource = itemView.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable res = itemView.getResources().getDrawable(imageResource);
            ivIcon.setImageDrawable(res);
        }
    }
}
