package com.reactive.appone.components;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.AsyncSubject;

public class AsyncSubjectSample {


    private AsyncSubject<Boolean> retrieveBoolean(){
        AsyncSubject<Boolean> result = AsyncSubject.create();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result.onNext(true);
                result.onComplete();
            }
        });
        return result;
    }



}
