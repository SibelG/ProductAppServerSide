package com.example.productappserverside.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;


import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public Context context;
    public TextView textCategoryName;
    public ImageView imageView;
    private ItemClickListener itemClicklistener;
    public static final int Update=Menu.FIRST;
    public static final int Delete=Menu.FIRST+1;


    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        textCategoryName= itemView.findViewById(R.id.categoryName);
        imageView=(ImageView) itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }


    @Override
    public void onClick(View view) {
        getItemClicklistener().onClick(view,getAdapterPosition(),false);

    }

    public ItemClickListener getItemClicklistener() {
        return itemClicklistener;
    }

    public void setItemClicklistener(ItemClickListener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }




    public interface ItemClickListener {
        void onClick(View view,int pozition,boolean isLongClick);
    }
   @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select the action");
            contextMenu.add(Menu.NONE,Update,0, Common.UPDATE);
            contextMenu.add(Menu.NONE,Delete,1, Common.DELETE);


    }
}
