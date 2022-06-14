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

import java.util.List;

public class DokumenKaryaAdapter extends RecyclerView.Adapter<DokumenKaryaAdapter.DokumenKaryaAdapterVH>{
    private List<DokumenKaryaResponse> responseDokumenKaryaList;
    private Context mContext;
    String id_document,produk_id,name_document,title_produk,cat_file,uri;
    View layoutDokumen;
    private DokumenKaryaAdapter.ClickedItem clickedItem;
    int imageResource;

    public void setData(List<DokumenKaryaResponse> responseDokumenKaryaList) {
        this.responseDokumenKaryaList = responseDokumenKaryaList;
        notifyDataSetChanged();
    }

    // click adapter
    public DokumenKaryaAdapter(DokumenKaryaAdapter.ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedDokumenKarya(DokumenKaryaResponse responseDokumenKarya);
    }

    //click adapter

    @NonNull
    @Override
    public DokumenKaryaAdapter.DokumenKaryaAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new DokumenKaryaAdapter.DokumenKaryaAdapterVH(LayoutInflater.from(mContext).inflate(R.layout.row_dokumen_karya, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DokumenKaryaAdapter.DokumenKaryaAdapterVH holder, int position) {
        DokumenKaryaResponse responseDokumenKarya = responseDokumenKaryaList.get(position);
        Log.d("bisaya", String.valueOf(responseDokumenKarya));
        id_document = responseDokumenKarya.getId_document();
        produk_id = responseDokumenKarya.getProduk_id();
        name_document = responseDokumenKarya.getName_document();
        cat_file = responseDokumenKarya.getCat_file();

        if(cat_file.contains("doc")){
            uri = "@drawable/file_word_solid";  // where myresource (without the extension) is the file
        }else if(cat_file.contains("xls")){
            uri = "@drawable/file_excel_solid";  // where myresource (without the extension) is the file
        }else if(cat_file.contains("pdf")){
            uri = "@drawable/file_pdf_solid";  // where myresource (without the extension) is the file
        }
        holder.setImage(uri);
        holder.tvJudulDokumen.setText(name_document);

        //click adapter
        layoutDokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedDokumenKarya(responseDokumenKarya);
            }
        });
    }
    @Override
    public int getItemCount() {
        return responseDokumenKaryaList.size();
    }

    public class DokumenKaryaAdapterVH extends RecyclerView.ViewHolder {
        TextView tvJudulDokumen;
        ImageView ivIcon;

        public DokumenKaryaAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvJudulDokumen = itemView.findViewById(R.id.tvJudulDokumen);

            layoutDokumen = itemView.findViewById(R.id.layoutDokumen);
        }

        public void setImage(String uri) {
            ivIcon = itemView.findViewById(R.id.ivIcon);
            imageResource = itemView.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable res = itemView.getResources().getDrawable(imageResource);
            ivIcon.setImageDrawable(res);
        }
    }
}
