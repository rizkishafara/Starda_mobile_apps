package com.example.stardaapp;

import android.content.Context;
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

public class StakeholdersAdapter extends RecyclerView.Adapter<StakeholdersAdapter.StakeholderAdapterVH>{

    private List<StakeholdersResponse> responseStakeholderList;
    private Context context;
    String id_user,fullname,email,photo_user,address_user,phone_user,keahlian_user,category_user,instansi,gender,about;
    String imgurl;
    View layoutStakeholder;
    private ClickedItem clickedItem;

    public void setData(List<StakeholdersResponse> responseStakeholderList) {
        this.responseStakeholderList = responseStakeholderList;
        notifyDataSetChanged();
    }

    // click adapter
    public StakeholdersAdapter(ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedStakeholder(StakeholdersResponse responseStakeholder);
    }



    //click adapter

    @NonNull
    @Override
    public StakeholdersAdapter.StakeholderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new StakeholdersAdapter.StakeholderAdapterVH(LayoutInflater.from(context).inflate(R.layout.row_stakeholder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StakeholderAdapterVH holder, int position) {
        StakeholdersResponse responseStakeholder = responseStakeholderList.get(position);
        Log.d("bisaya", String.valueOf(responseStakeholder));
        id_user = responseStakeholder.getId_user();
        fullname = responseStakeholder.getFullname();
//        email = responseStakeholder.getEmail();
        photo_user = responseStakeholder.getPhoto_user();
//        address_user = responseStakeholder.getAddress_user();
//        keahlian_user = responseStakeholder.getKeahlian_user();
        category_user = responseStakeholder.getCategory_user();
//        instansi = responseStakeholder.getInstansi();
//        gender = responseStakeholder.getGender();
//        about = responseStakeholder.getAbout();
//        phone_user = responseStakeholder.getPhone_user();

        String getUrl  = UtilsApi.ProfileUrl;
        imgurl = getUrl + photo_user;

        holder.setImage(imgurl);
        holder.tvFullname.setText(fullname);
        holder.tvKategori.setText(category_user);
//        holder.tvEmail.setText(email);
//        holder.tvAddress.setText(address_user);
//        holder.tvKeahlian.setText(keahlian_user);
//        holder.tvCategory.setText(category_user);
//        holder.tvInstansi.setText(instansi);
//        holder.tvAbout.setText(about);
//        holder.tvPhone.setText(phone_user);

        //click adapter
        layoutStakeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedStakeholder(responseStakeholder);
            }
        });


    }
    @Override
    public int getItemCount() {
        return responseStakeholderList.size();
    }

    public class StakeholderAdapterVH extends RecyclerView.ViewHolder {
        TextView tvFullname,tvKategori,tvEmail,tvAddress,tvKeahlian,tvCategory,tvInstansi,tvAbout,tvPhone;
        ImageView ivProfile;

        public StakeholderAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvFullname = itemView.findViewById(R.id.tvFullname);
            tvKategori = itemView.findViewById(R.id.tvKategori);
//            tvEmail = itemView.findViewById(R.id.tvEmail);
//            tvAddress = itemView.findViewById(R.id.tvAlamat);
//            tvKeahlian = itemView.findViewById(R.id.tvKeahlian);
//            tvCategory = itemView.findViewById(R.id.tvKategori);
//            tvInstansi = itemView.findViewById(R.id.tvInstansi);
//            tvAbout = itemView.findViewById(R.id.tvAbout);
//            tvPhone = itemView.findViewById(R.id.tvPhone);

            layoutStakeholder = itemView.findViewById(R.id.layoutStakeholder);
        }

        public void setImage(String imgurl) {
            ivProfile = itemView.findViewById(R.id.ivProfile);
            Glide.with(itemView.getContext()).load(imgurl).into(ivProfile);
        }
    }

}
