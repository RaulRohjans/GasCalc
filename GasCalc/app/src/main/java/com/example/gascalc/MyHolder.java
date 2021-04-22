package com.example.gascalc;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mMedia, mDist, mGas, mPreco;
    public CardView mCrv;
    ItemClickListener itemClickListener;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.mMedia = itemView.findViewById(R.id.lbl_media);
        this.mDist = itemView.findViewById(R.id.lbl_dist);
        this.mGas = itemView.findViewById(R.id.lbl_gas);
        this.mPreco = itemView.findViewById(R.id.lbl_preco);
        this.mCrv = itemView.findViewById(R.id.crv_);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick (View v)
    {
        this.itemClickListener.onItemCLickListener(v, getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener = ic;
    }
}
