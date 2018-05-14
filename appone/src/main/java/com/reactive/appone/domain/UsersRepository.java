package com.reactive.appone.domain;

import com.reactive.appone.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class UsersRepository {

    private final RetrofitMocks retrofit = new RetrofitMocks();

    public Observable<List<User>> requestUserOlderThan(int age) {
        return retrofit.requestUserOlderThan(age)
                .subscribeOn(Schedulers.io());
    }
}
