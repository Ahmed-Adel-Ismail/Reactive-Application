package com.reactive.appone.features.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reactive.appone.R;
import com.reactive.appone.entities.User;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private final BehaviorSubject<List<User>> dataSource;
    private final Disposable disposable;

    UsersAdapter(BehaviorSubject<List<User>> dataSource) {
        this.dataSource = dataSource;
        this.disposable = dataSource.share()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(view(parent));
    }

    private View view(@NonNull ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_user_item, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.invalidate(dataSource.getValue().get(position));
    }

    @Override
    public int getItemCount() {
        return dataSource.getValue().size();
    }

    public Disposable getDisposable() {
        return disposable;
    }
}
