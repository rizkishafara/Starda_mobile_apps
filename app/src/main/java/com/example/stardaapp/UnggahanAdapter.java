package com.example.stardaapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.UtilsApi;

import java.util.List;

public class UnggahanAdapter extends RecyclerView.Adapter<UnggahanAdapter.UnggahanAdapterVH>{
    private List<UnggahanResponse> unggahanResponseList;
    private Context mContext;
    String Judul_unggahan,Desc_unggahan,Date_unggahan,id_unggahan,Ext_unggahan,Status_unggahan;
    String uri;
    View layoutUnggahan;
    Button btnDetail,btnEdit,btnAlasan,btnHapus;
    private UnggahanAdapter.ClickedItem clickedItem;
    int imageResource;

    public void setData(List<UnggahanResponse> unggahanResponseList) {
        this.unggahanResponseList = unggahanResponseList;
        notifyDataSetChanged();
    }

    // click adapter
    public UnggahanAdapter(UnggahanAdapter.ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedUnggahan(UnggahanResponse unggahanResponse);
    }

    //click adapter

    @NonNull
    @Override
    public UnggahanAdapter.UnggahanAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new UnggahanAdapter.UnggahanAdapterVH(LayoutInflater.from(mContext).inflate(R.layout.row_unggahan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnggahanAdapter.UnggahanAdapterVH holder, int position) {
        UnggahanResponse unggahanResponse = unggahanResponseList.get(position);
        Log.d("bisaya", String.valueOf(unggahanResponse));
        Judul_unggahan = unggahanResponse.getTitle_produk();
        Desc_unggahan = unggahanResponse.getDesc_produk();
        Date_unggahan = unggahanResponse.getUpload_date();
        Ext_unggahan = unggahanResponse.getCat_file();
        Status_unggahan = unggahanResponse.getStatus();

        //set url icon media
        if(Ext_unggahan.contains("photo")){
            uri = "@drawable/image_solid";  // where myresource (without the extension) is the file
        }else if(Ext_unggahan.contains("video")){
            uri = "@drawable/film_solid";  // where myresource (without the extension) is the file
        }

        //enable button alasan
        if(Status_unggahan.contains("Ditolak")){
            btnAlasan.setVisibility(View.VISIBLE);
        }

        Log.d("ext", uri);
        holder.setImage(uri);
        holder.tvJudul.setText(Judul_unggahan);
        holder.tvDesc.setText('"'+Desc_unggahan+'"');
        holder.tvDate.setText(Date_unggahan);

        //click adapter
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedUnggahan(unggahanResponse);
            }
        });


    }

    @Override
    public int getItemCount() {
        return unggahanResponseList.size();
    }

    public class UnggahanAdapterVH extends RecyclerView.ViewHolder {
        TextView tvJudul,tvDesc,tvDate;
        ImageView ivIconUnggahan;

        public UnggahanAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudulUnggahan);
            tvDesc = itemView.findViewById(R.id.tvDescUnggahan);
            tvDate = itemView.findViewById(R.id.tvDateUnggahan);

            layoutUnggahan = itemView.findViewById(R.id.layoutUnggahan);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnAlasan = itemView.findViewById(R.id.btnAlasan);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnDelete);
        }

        public void setImage(String uri) {
            Log.d("ext", uri);
            ivIconUnggahan = itemView.findViewById(R.id.ivIconUnggahan);
            imageResource = itemView.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable res = itemView.getResources().getDrawable(imageResource);
            ivIconUnggahan.setImageDrawable(res);
        }
    }
}
