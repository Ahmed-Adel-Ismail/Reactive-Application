package com.reactive.appone.features.home;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.reactive.appone.domain.UsersRepository;
import com.reactive.appone.entities.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class HomeViewModel extends ViewModel {

    private static final String EMPTY_STRING = "";
    final BehaviorSubject<String> ageInput = BehaviorSubject.createDefault(EMPTY_STRING);
    final BehaviorSubject<Boolean> loading = BehaviorSubject.createDefault(false);
    final BehaviorSubject<List<User>> users = BehaviorSubject.createDefault(new ArrayList<>());
    final PublishSubject<String> errorMessage = PublishSubject.create();

    private final CompositeDisposable disposables = new CompositeDisposable();
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
        disposables.add(requestUsersOnAgeInput());
    }

    @NonNull
    private Disposable requestUsersOnAgeInput() {
        return this.ageInput.share()
                .subscribe(ignoredValue -> requestUsers());
    }


    void requestUsers() {
        disposables.add(invokeRequestUsers());
    }

    @NonNull
    private Disposable invokeRequestUsers() {
        return Observable.just(ageInput.getValue())
                .observeOn(scheduler)
                .filter(this::nonEmptyText)
                .filter(this::notLoading)
                .doOnNext(this::startLoading)
                .map(Integer::parseInt)
                .flatMap(repository::requestUserOlderThan)
                .doOnError(this::printExceptionMessage)
                .doOnError(this::updateErrorMessage)
                .onErrorReturnItem(new ArrayList<>())
                .doFinally(this::stopLoading)
                .subscribe(users::onNext);
    }

    private void updateErrorMessage(Throwable ex) {
        errorMessage.onNext(ex.getMessage());
    }

    private void printExceptionMessage(Throwable ex) {
        System.err.println(ex.getMessage());
    }

    private void startLoading(String ignoredValue) {
        loading.onNext(true);
    }

    private boolean notLoading(String ignoredValue) {
        return !loading.getValue();
    }

    private boolean nonEmptyText(String value) {
        return !EMPTY_STRING.equals(value);
    }

    private void stopLoading() {
        loading.onNext(false);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        ageInput.onComplete();
        loading.onComplete();
        users.onComplete();
        errorMessage.onComplete();
        super.onCleared();
    }
}
