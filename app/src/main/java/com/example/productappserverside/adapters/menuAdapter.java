package com.example.productappserverside.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productappserverside.Common.Common;
import com.example.productappserverside.R;
import com.example.productappserverside.model.Category;
import com.example.productappserverside.view.HomeActivity;
import com.example.productappserverside.view.ProductListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.CategoryViewHolder>  {

    private ArrayList<Category> categoryArrayList;
    private Context context;
    private itemClickListener listener;
    public menuAdapter(Context context,ArrayList<Category> categoryArrayList){
        this.categoryArrayList=categoryArrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu_item,parent,false);

        return new CategoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull menuAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textCategoryName.setText(categoryArrayList.get(position).getName());
        Picasso.with(context).load(categoryArrayList.get(position).getImageUrl()).into(holder.imageView);
        final Category clickItem=categoryArrayList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductListActivity.class);
                //intent.putExtra("categoryId",categoryArrayList.get(position).getCategoryId());
                holder.context.startActivity(intent);
            }
        });



    }

        @Override
        public int getItemCount() {
            return categoryArrayList.size();
        }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{
        public Context context;
        public TextView textCategoryName;
        public ImageView imageView;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textCategoryName= itemView.findViewById(R.id.categoryName);
            imageView=(ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if(listener!=null){
                listener.onClick(view,getAdapterPosition(),false);
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            contextMenu.setHeaderTitle("Select the action");
            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);


        }

    }


    public interface itemClickListener {
        void onClick(View view,int pozition,boolean isLongClick);
    }




}
