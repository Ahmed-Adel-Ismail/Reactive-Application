package com.reactive.appone.features.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.reactive.appone.R;
import com.reactive.appone.entities.User;

class UsersViewHolder extends RecyclerView.ViewHolder {

    private final TextView userName;

    UsersViewHolder(View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.user_name);
    }

    void invalidate(User user) {
        userName.setText(user.getName());
    }

}
