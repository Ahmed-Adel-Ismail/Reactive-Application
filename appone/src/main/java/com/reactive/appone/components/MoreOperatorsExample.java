package com.reactive.appone.components;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MoreOperatorsExample {

    public static void main(String... args) {


    }

    private static void combineLatest() {
        // ViewModel
        Subject<String> userName = BehaviorSubject.create();
        Subject<String> password = BehaviorSubject.create();
        Subject<String> validation = BehaviorSubject.create();

        // Activity / Fragment : display red text instead of printing
        validation.subscribe(System.out::println);


        // in ViewModel Constructor
        Observable.combineLatest(userName, password, validate())
                .subscribe(validation::onNext);


        userName.onNext("A");
        password.onNext("1");

        userName.onNext("AA");

        userName.onNext("AAA");
        password.onNext("");

        password.onNext("A");
        password.onNext("AA");
        password.onNext("AAA");
    }

    @NonNull
    private static BiFunction<String, String, String> validate() {
        return (userNameText, passwordText) ->
                userNameText.equals("AAA") && passwordText.equals("AAA")
                        ? "valid userName & password : " + userNameText + ", " + passwordText
                        : "invalid userName & password : " + userNameText + ", " + passwordText;
    }

    private static void zipOperator() {
        Observable<List<Integer>> one = Observable.fromCallable(() -> {
            Thread.sleep(500);
            return Arrays.asList(1, 2, 3, 4);
        });

        Observable<String> two = Observable.fromCallable(() -> {
            Thread.sleep(1000);
            return "finished";
        });

        Observable<Boolean> three = Observable.fromCallable(() -> {
            Thread.sleep(500);
            return true;
        });


        Observable.zip(one, two, three, new Function3<List<Integer>, String, Boolean, String>() {
            @Override
            public String apply(List<Integer> oneResult, String twoResult, Boolean threeResult) throws Exception {
                return "Results : 1|" + oneResult + ", 2|" + twoResult + ", 3|" + threeResult;
            }
        }).subscribe(System.out::println);
    }

    private static void flatMapWithMultipleItems() {
        List<MyObject> myObjects = Arrays.asList(new MyObject(), new MyObject(), new MyObject());
        Observable.fromIterable(myObjects)
                .map(MyObject::getList)
                .flatMap(list -> Observable.fromIterable(list))
                .buffer(3)
                .subscribe(System.out::println);
    }

    private static void flatMapOnOneObservable() {
        Observable.just(new MyObject())
                .doOnNext(object -> System.out.println(object.getNumber()))
                .map(MyObject::getList)
                .flatMap(list -> Observable.fromIterable(list))
                .subscribe(System.out::println);
    }


    static class MyObject {

        private final int number = 1;
        private final List<Integer> list = Arrays.asList(10, 20, 30, 40, 50, 60);

        public int getNumber() {
            return number;
        }

        public List<Integer> getList() {
            return list;
        }
    }


}
