package com.reactive.appone.domain;

import com.reactive.appone.entities.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class RetrofitMocks {

    private final UsersData usersData = new UsersData();

    Observable<List<User>> requestUserOlderThan(int age){
        return Observable.fromIterable(usersData.getUsers())
                .filter(user -> user.getAge() > age)
                .toList()
                .delay(200, TimeUnit.MILLISECONDS)
                .flatMapObservable(Observable::just);
    }
}
