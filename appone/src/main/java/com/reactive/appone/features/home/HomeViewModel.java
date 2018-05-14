package com.reactive.appone.features.home;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.chaining.Chain;
import com.reactive.appone.domain.UsersRepository;
import com.reactive.appone.entities.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class HomeViewModel extends ViewModel {

    private static final String EMPTY_STRING = "";
    final BehaviorSubject<String> ageInput = BehaviorSubject.createDefault(EMPTY_STRING);
    final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);
    final BehaviorSubject<List<User>> users = BehaviorSubject.createDefault(new ArrayList<>());
    final PublishSubject<String> errorMessage = PublishSubject.create();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final UsersRepository repository;
    private final Scheduler scheduler;

    @SuppressLint("RestrictedApi")
    public HomeViewModel() {
        this(new UsersRepository(), Schedulers.computation());
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    HomeViewModel(UsersRepository repository, Scheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }


    void requestUsers() {
        disposable.add(invokeRequestUsers());
    }

    @NonNull
    private Disposable invokeRequestUsers() {
        return Observable.just(ageInput.getValue())
                .observeOn(scheduler)
                .filter(value -> !EMPTY_STRING.equals(value))
                .filter(ignoredValue -> !loading.getValue())
                .doOnNext(ignoredValue -> loading.onNext(true))
                .doFinally(this::stopLoading)
                .map(Integer::parseInt)
                .flatMap(repository::requestUserOlderThan)
                .doOnError(ex -> System.err.println(ex.getMessage()))
                .doOnError(ex -> errorMessage.onNext(ex.getMessage()))
                .onErrorReturnItem(new ArrayList<>())
                .subscribe(users::onNext);
    }

    private void stopLoading() {
        loading.onNext(false);
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
