package com.reactive.java.presentation.features;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.reactive.java.R;
import com.reactive.java.entities.JobSuggestion;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        disposables.add(bindJobNameEditText(viewModel.jobSuggestionName));
        disposables.add(bindInsertButton(viewModel));
        disposables.add(bindJobsCount(viewModel.jobSuggestions));
    }

    private Disposable bindJobNameEditText(BehaviorSubject<String> jobSuggestionName) {
        EditText jobName = findViewById(R.id.job_name);
        return RxTextView.textChangeEvents(jobName)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(String::valueOf)
                .subscribe(jobSuggestionName::onNext);
    }

    private Disposable bindInsertButton(MainViewModel viewModel) {
        Button insert = findViewById(R.id.insert_job_name);
        return RxView.clicks(insert)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapMaybe(object -> viewModel.insertJobSuggestionName())
                .subscribe(disposables::add);
    }

    private Disposable bindJobsCount(BehaviorSubject<List<JobSuggestion>> jobSuggestions) {
        TextView jobsCountTextView = findViewById(R.id.jobs_count);
        return jobSuggestions.share()
                .map(List::size)
                .map(String::valueOf)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jobsCountTextView::setText);
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
