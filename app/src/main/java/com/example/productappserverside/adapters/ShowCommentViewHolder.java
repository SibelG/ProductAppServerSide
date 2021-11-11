package com.example.productappserverside.adapters;

import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productappserverside.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public EditText userEmail,userComment;
    public RatingBar userRating;
    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userEmail=itemView.findViewById(R.id.email);
        userComment=itemView.findViewById(R.id.Comment);
        userRating=itemView.findViewById(R.id.rating);
    }
}
