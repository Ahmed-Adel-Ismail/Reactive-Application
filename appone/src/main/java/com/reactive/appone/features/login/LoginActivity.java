package com.reactive.appone.features.login;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.reactive.appone.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class LoginActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginViewModel viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initializeLoginButton(viewModel);
        disposables.add(bindLoadingState(viewModel.loadingState));
        disposables.add(bindRequestLoginResult(viewModel.requeustLoginResult));
    }

    private void initializeLoginButton(LoginViewModel viewModel) {
        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(view -> viewModel.requestLogin("",""));
    }

    private Disposable bindRequestLoginResult(BehaviorSubject<Boolean> requeustLoginResult) {
            return requeustLoginResult
                    .share()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> showToast());
    }

    private void showToast() {
        Toast.makeText(this,"result recieved",Toast.LENGTH_SHORT).show();
    }

    private Disposable bindLoadingState(BehaviorSubject<Boolean> loadingState) {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        return loadingState
                .share()
                .observeOn(AndroidSchedulers.mainThread())
                .map(loading -> loading ? View.VISIBLE : View.GONE)
                .subscribe(progressBar::setVisibility);
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
