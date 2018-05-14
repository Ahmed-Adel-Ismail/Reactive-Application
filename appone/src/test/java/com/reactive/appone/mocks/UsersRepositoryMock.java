package com.reactive.appone.mocks;

import android.support.annotation.NonNull;

import com.reactive.appone.domain.UsersRepository;
import com.reactive.appone.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class UsersRepositoryMock extends UsersRepository {


    private final boolean error;

    public UsersRepositoryMock(boolean error) {
        this.error = error;
    }

    @Override
    public Observable<List<User>> requestUserOlderThan(int age) {

        if (error) {
            return Observable.error(new UnsupportedOperationException("---error---"));
        }

        return Observable.fromIterable(users())
                .filter(user -> user.getAge() > age)
                .toList()
                .toObservable();
    }

    @NonNull
    private List<User> users() {
        List<User> users = new ArrayList<>();
        users.add(new User("A", 10));
        users.add(new User("B", 50));
        return users;
    }
}

