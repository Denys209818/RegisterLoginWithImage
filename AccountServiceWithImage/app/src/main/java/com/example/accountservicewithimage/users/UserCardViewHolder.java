package com.example.accountservicewithimage.users;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accountservicewithimage.R;

public class UserCardViewHolder extends RecyclerView.ViewHolder {
    private View currentItem;
    public ImageView imageView;
    public TextView textView;

    public UserCardViewHolder(@NonNull View itemView) {
        super(itemView);
        currentItem = itemView;
        imageView = itemView.findViewById(R.id.userimage);
        textView = itemView.findViewById(R.id.usrname);
    }

    public View getCurrentItem()
    {
        return currentItem;
    }
}
