package com.reactive.appone.features.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.reactive.appone.R;
import com.reactive.appone.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class HomeActivity extends AppCompatActivity {

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeViewModel viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        disposable.add(bindAgeEditText(viewModel.ageInput));
        disposable.add(bindProgressBar(viewModel.loading));
        disposable.add(bindUsersRecyclerView(viewModel.users));
        disposable.add(bindErrorMessages(viewModel.errorMessage));
    }

    private Disposable bindAgeEditText(BehaviorSubject<String> ageInput) {
        EditText editText = findViewById(R.id.age_edit_text);
        return RxTextView.textChangeEvents(editText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(TextViewTextChangeEvent::text)
                .map(String::valueOf)
                .subscribe(ageInput::onNext);
    }

    private Disposable bindProgressBar(BehaviorSubject<Boolean> loading) {
        ProgressBar progressBar = findViewById(R.id.users_progress_bar);
        return loading.share()
                .map(loadingState -> loadingState ? View.VISIBLE : View.GONE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progressBar::setVisibility);
    }

    private Disposable bindUsersRecyclerView(BehaviorSubject<List<User>> users) {
        RecyclerView recyclerView = findViewById(R.id.users_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UsersAdapter adapter = new UsersAdapter(users);
        recyclerView.setAdapter(adapter);
        return adapter.getDisposable();
    }

    private Disposable bindErrorMessages(PublishSubject<String> errorMessage) {
        return errorMessage.share()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
