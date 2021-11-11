package com.example.productappserverside.adapters;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;


public class productViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    private ItemClickListener itemClickListener;
    public TextView foodName;
    public ImageView foodImage;
    public static final int Update=Menu.FIRST;
    public static final int Delete=Menu.FIRST+1;

    public productViewHolder(@NonNull View itemView) {
        super(itemView);
        foodName= itemView.findViewById(R.id.productName);
        foodImage=itemView.findViewById(R.id.imageViewProduct);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {
        getItemClickListener().onClick(view, getAdapterPosition(), false);
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(Menu.NONE,Update,0, Common.UPDATE);
        contextMenu.add(Menu.NONE,Delete,1, Common.DELETE);
    }


    public interface ItemClickListener {
        void onClick(View view,int pozition,boolean isLongClick);
    }

}
