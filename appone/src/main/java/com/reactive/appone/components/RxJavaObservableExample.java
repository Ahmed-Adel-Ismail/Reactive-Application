package com.reactive.appone.components;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class RxJavaObservableExample {


    public static void main(String... args) {


    }

    private static void completableOne() throws InterruptedException {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Thread.sleep(2000);
                System.out.println("action completed");
            }
        }).subscribe(()-> System.out.println("---"),Throwable::printStackTrace);
    }

    private static void maybeOne() {
        parseInteger("ouihoac")
                .map(i -> i * 2)
                .subscribe(System.out::println,Throwable::printStackTrace,
                        () -> System.out.println("1 completed"));

        parseInteger("11")
                .subscribe(System.out::println,Throwable::printStackTrace,
                        () -> System.out.println("2 completed"));
    }

    private static Maybe<Integer> parseInteger(String integerValue){
        try{
            return Maybe.just(Integer.parseInt(integerValue));
        }catch (Exception e){
            return Maybe.empty();
        }
    }

    private static void singleOne() {
        final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12,13);
        Observable.fromIterable(numbers)
                .map(String::valueOf)
                .toList()
                .subscribe(new SingleObserver<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<String> strings) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private static void bufferOne() {
        final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8,9,10,11,12,13);
        Observable.fromIterable(numbers)
                .buffer(3)
                .subscribe(System.out::println);
    }

    private static void concatDelayErrorOne() {
        Callable<Integer> successCallable = () -> 10;
        Callable<Integer> errorCallable = () -> {throw new UnsupportedOperationException();};


        List<Observable<Integer>> observables = new ArrayList<>();
        observables.add(Observable.fromCallable(errorCallable));
        observables.add(Observable.fromCallable(successCallable));


        Observable.concatDelayError(observables)
                .firstElement()
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private static void concatOne() {
        final List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        numbers.remove(null);

        Observable<Integer> evens = Observable.fromIterable(numbers)
                .filter(integer -> integer % 2 == 0);

        Observable<Integer> odds = Observable.fromIterable(numbers)
                .filter(integer -> integer % 2 != 0);


        List<Observable<Integer>> observables = new ArrayList<>();
        observables.add(evens);
        observables.add(odds);

        Observable.concat(observables)
                .firstElement()
                .subscribe(System.out::println);
    }

    private static void iterableObserversOne(List<Integer> numbers) {
        Observable.fromIterable(numbers)
                .compose(toEvenStrings())
                .map(text -> "__" + text + "__")
                .filter(text -> !text.contains("2"))
                .subscribe(value -> System.out.println(value));


        Observable.fromArray(10,20,30,40,50)
                .compose(toEvenStrings())
                .subscribe(System.out::println);
    }

    @NonNull
    private static ObservableTransformer<Integer, String> toEvenStrings() {
        return upstream ->
                upstream.filter(integer -> integer % 2 == 0)
                .map(String::valueOf);
    }
}
