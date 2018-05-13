package com.reactive.appone.domain;

public class AuthenticationRepository {

    public boolean requestLogin(String userName, String password){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

}
