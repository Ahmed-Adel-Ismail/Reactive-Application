package com.reactive.appone.features.login;

import android.arch.lifecycle.ViewModel;

import com.reactive.appone.domain.AuthenticationRepository;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class LoginViewModel extends ViewModel {

    final BehaviorSubject<Boolean> loadingState = BehaviorSubject.createDefault(false);
    final BehaviorSubject<Boolean> requeustLoginResult = BehaviorSubject.create();
    private final AuthenticationRepository repository = new AuthenticationRepository();
    private final CompositeDisposable disposables = new CompositeDisposable();

    void requestLogin(String userName, String password){
        if(!loadingState.getValue()){
            disposables.add(invokeRequestingLogin(userName, password));
        }
    }

    private Disposable invokeRequestingLogin(String userName, String password) {
        loadingState.onNext(true);
        return Observable.fromCallable(() -> repository.requestLogin(userName,password))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doFinally(() -> loadingState.onNext(false))
                .subscribe(result -> requeustLoginResult.onNext(result),ex -> requeustLoginResult.onNext(false),() -> {});

    }


    @Override
    protected void onCleared() {
        disposables.clear();
        loadingState.onComplete();
        requeustLoginResult.onComplete();
        super.onCleared();
    }
}
