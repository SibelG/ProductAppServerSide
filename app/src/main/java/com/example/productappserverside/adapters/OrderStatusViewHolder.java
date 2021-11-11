package com.example.productappserverside.adapters;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;

public class OrderStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public  itemClickListener itemClickListener;
    public TextView orderId,orderEmail,orderStatus,orderAddress;

    public OrderStatusViewHolder(@NonNull View itemView) {
        super(itemView);
        orderId=itemView.findViewById(R.id.OrderId);
        orderAddress=itemView.findViewById(R.id.statusAddress);
        orderEmail=itemView.findViewById(R.id.statusEmail);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(OrderStatusViewHolder.itemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);

    }

    public interface itemClickListener {
        void onClick(View view,int pozition,boolean isLongClick);
    }
}
