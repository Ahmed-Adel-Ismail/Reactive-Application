package com.reactive.app

import io.reactivex.Observable
import io.reactivex.functions.Consumer


fun main(args: Array<String>) {

    Observable.fromArray(1, 2, 3, 4, 5, 6)
            .filter { it % 2 == 0 }
            .subscribe { System.out.println(it) }


}