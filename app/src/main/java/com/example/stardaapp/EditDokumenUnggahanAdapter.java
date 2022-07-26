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

import java.util.List;

public class EditDokumenUnggahanAdapter extends RecyclerView.Adapter<EditDokumenUnggahanAdapter.EditDokumenUnggahanAdapterVH>{
    private List<DokumenKaryaResponse> responseDokumenKaryaList;
    private Context mContext;
    String id_document,produk_id,name_document,title_produk,cat_file,uri;
    View layoutDokumen;
    private EditDokumenUnggahanAdapter.ClickedItem clickedItem;
    int imageResource;

    Button btnEdit, btnHapus;

    public void setData(List<DokumenKaryaResponse> responseDokumenKaryaList) {
        this.responseDokumenKaryaList = responseDokumenKaryaList;
        notifyDataSetChanged();
    }

    // click adapter
    public EditDokumenUnggahanAdapter(EditDokumenUnggahanAdapter.ClickedItem clickedItem) {
        this.clickedItem = clickedItem;
    }
    public interface ClickedItem{
        public void ClickedEditDokumen(DokumenKaryaResponse responseDokumenKarya);
        public void ClickedHapusDokumen(DokumenKaryaResponse responseDokumenKarya);
    }

    //click adapter

    @NonNull
    @Override
    public EditDokumenUnggahanAdapter.EditDokumenUnggahanAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new EditDokumenUnggahanAdapter.EditDokumenUnggahanAdapterVH(LayoutInflater.from(mContext).inflate(R.layout.row_edit_dokumen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditDokumenUnggahanAdapter.EditDokumenUnggahanAdapterVH holder, int position) {
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
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedEditDokumen(responseDokumenKarya);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedItem.ClickedHapusDokumen(responseDokumenKarya);
            }
        });
    }
    @Override
    public int getItemCount() {
        return responseDokumenKaryaList.size();
    }

    public class EditDokumenUnggahanAdapterVH extends RecyclerView.ViewHolder {
        TextView tvJudulDokumen;
        ImageView ivIcon;


        public EditDokumenUnggahanAdapterVH(@NonNull View itemView) {
            super(itemView);
            tvJudulDokumen = itemView.findViewById(R.id.tvJudulDokumen);

            layoutDokumen = itemView.findViewById(R.id.layoutDokumen);
            btnEdit = itemView.findViewById(R.id.btnEditDokumen);
            btnHapus = itemView.findViewById(R.id.btnHapusDokumen);

        }

        public void setImage(String uri) {
            ivIcon = itemView.findViewById(R.id.ivIcon);
            imageResource = itemView.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable res = itemView.getResources().getDrawable(imageResource);
            ivIcon.setImageDrawable(res);
        }
    }
}
