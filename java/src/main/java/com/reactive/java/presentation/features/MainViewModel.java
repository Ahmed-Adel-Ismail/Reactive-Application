package com.reactive.java.presentation.features;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.reactive.java.domain.AppDatabase;
import com.reactive.java.entities.JobSuggestion;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public class MainViewModel extends ViewModel {

    final BehaviorSubject<List<JobSuggestion>> jobSuggestions = BehaviorSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();


    public MainViewModel() {
        disposables.add(bindJobSuggestions());
    }

    @NonNull
    private Disposable bindJobSuggestions() {
        return AppDatabase.getInstance()
                .getJobSuggestions()
                .queryAll()
                .subscribe(jobSuggestions::onNext, jobSuggestions::onError);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
