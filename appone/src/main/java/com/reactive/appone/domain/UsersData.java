package com.reactive.appone.domain;

import com.reactive.appone.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UsersData {

    private final List<User> users = new ArrayList<>();

    public UsersData() {
        users.add(new User("A",10));
        users.add(new User("B",20));
        users.add(new User("C",30));
        users.add(new User("D",40));
        users.add(new User("E",50));
        users.add(new User("F",60));
        users.add(new User("G",70));
        users.add(new User("H",80));
        users.add(new User("I",90));
        users.add(new User("J",100));
        users.add(new User("K",110));
        users.add(new User("L",120));
        users.add(new User("M",130));
        users.add(new User("N",140));
   }

    public List<User> getUsers() {
        return users;
    }
}
