package com.example.stardaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stardaapp.api.UtilsApi;

import java.util.List;

public class UnduhanAdapter extends RecyclerView.Adapter<UnduhanAdapter.UnduhanAdapterVH>{
    private List<UnduhanResponse> unduhanResponseList;
    private Context mContext;
    String UserPengunduh,PhotoUser,KaryaDiunduh,TanggalUnduh;
    String imgurl;
    View layoutPengunduh;
    private UnduhanAdapter.ClickedItem clickedItem;
    int imageResource;

    public void setData(List<UnduhanResponse> unduhanResponseList) {
        this.unduhanResponseList = unduhanResponseList;
        notifyDataSetChanged();
    }

    // click adapter
    public UnduhanAdapter(UnduhanAdapter.ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedPengunduh(UnduhanResponse unduhanResponse);
    }

    @NonNull
    @Override
    public UnduhanAdapter.UnduhanAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new UnduhanAdapter.UnduhanAdapterVH(LayoutInflater.from(mContext).inflate(R.layout.row_user_pengunduh, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnduhanAdapter.UnduhanAdapterVH holder, int position) {
        UnduhanResponse unduhanResponse = unduhanResponseList.get(position);
//        Log.d("bisaya", String.valueOf(unduhanResponse));
        UserPengunduh = unduhanResponse.getFullname();
        PhotoUser = unduhanResponse.getPhoto_user();
        KaryaDiunduh = unduhanResponse.getTitle_produk();
        TanggalUnduh = unduhanResponse.getTanggal_unduh();

        //set url icon media
        String getUrl  = UtilsApi.ProfileUrl;
        imgurl = getUrl + PhotoUser;

        holder.setImage(imgurl);
        holder.tvPengunduh.setText(UserPengunduh);
        holder.tvTanggalUnduh.setText(TanggalUnduh);

        //click adapter
        layoutPengunduh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedPengunduh(unduhanResponse);
            }
        });


    }
    @Override
    public int getItemCount() {
        return unduhanResponseList.size();
    }

    public class UnduhanAdapterVH extends RecyclerView.ViewHolder {
        TextView tvPengunduh,tvTanggalUnduh;
        ImageView ivProfile;

        public UnduhanAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvPengunduh = itemView.findViewById(R.id.tvPengunduh);
            tvTanggalUnduh = itemView.findViewById(R.id.tvTanggalUnduh);

            layoutPengunduh = itemView.findViewById(R.id.layoutPengunduh);
        }

        public void setImage(String imgurl) {
            ivProfile = itemView.findViewById(R.id.ivProfile);
            Glide.with(itemView.getContext()).load(imgurl).into(ivProfile);
        }
    }
}
