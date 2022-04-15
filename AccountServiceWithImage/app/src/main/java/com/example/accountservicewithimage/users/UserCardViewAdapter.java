package com.example.accountservicewithimage.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.accountservicewithimage.R;
import com.example.accountservicewithimage.network.AccountService;
import com.example.accountservicewithimage.network.HomeApplication;
import com.example.accountservicewithimage.users.dto.UserDTO;

import java.util.List;

public class UserCardViewAdapter  extends RecyclerView.Adapter<UserCardViewHolder> {
    private List<UserDTO> users;

    public UserCardViewAdapter(List<UserDTO> users)
    {
        this.users = users;
    }

    @NonNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater
                .from(HomeApplication.getContext())
                .inflate(R.layout.card_user, parent, false);
        return new UserCardViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCardViewHolder holder, int position) {
        if(users != null && position < users.size())
        {
            UserDTO user = users.get(position);
            holder.textView.setText(user.getFirstName());
            String url = AccountService.getBaseUrl() + user.getImage();
            Glide.with(HomeApplication.getContext())
                    .load(url)
                    .apply(new RequestOptions().override(300, 300))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }
}
