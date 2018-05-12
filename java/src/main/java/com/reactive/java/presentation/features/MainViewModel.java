package com.reactive.java.presentation.features;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.reactive.java.domain.AppDatabase;
import com.reactive.java.entities.JobSuggestion;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MainViewModel extends ViewModel {

    private static final String EMPTY_STRING = "";

    final BehaviorSubject<String> jobSuggestionName = BehaviorSubject.createDefault(EMPTY_STRING);
    final BehaviorSubject<List<JobSuggestion>> jobSuggestions = BehaviorSubject.createDefault(new ArrayList<>());
    private final BehaviorSubject<Boolean> processing = BehaviorSubject.createDefault(false);
    private final CompositeDisposable disposables = new CompositeDisposable();


    public MainViewModel() {
        disposables.add(bindJobSuggestions());
    }

    @NonNull
    private Disposable bindJobSuggestions() {
        return AppDatabase.getInstance()
                .getJobSuggestions()
                .queryAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(jobSuggestions::onNext, jobSuggestions::onError);
    }

    Maybe<Disposable> insertJobSuggestionName() {
        return processing.getValue()
                ? Maybe.empty()
                : Maybe.just(invokeInsertJobSuggestion());
    }

    @NonNull
    private Disposable invokeInsertJobSuggestion() {
        processing.onNext(true);
        return Single.just(jobSuggestionName)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(BehaviorSubject::getValue)
                .filter(name -> !EMPTY_STRING.equals(name))
                .map(this::jobSuggestion)
                .doFinally(() -> jobSuggestionName.onNext(EMPTY_STRING))
                .doFinally(() -> processing.onNext(false))
                .subscribe(this::insertJobSuggestionIntoDatabase);

    }

    private void insertJobSuggestionIntoDatabase(JobSuggestion jobSuggestion) {
        AppDatabase.getInstance().getJobSuggestions().insert(jobSuggestion);
    }

    private JobSuggestion jobSuggestion(String name) {
        return new JobSuggestion(String.valueOf(Math.random()), name, "");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
