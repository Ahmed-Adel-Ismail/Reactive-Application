package com.reactive.app

import android.app.Activity
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ListAdapter
import io.reactivex.Observable

class MainActivity : Activity() {

    val autoComplete: AutoCompleteTextView by lazy { AutoCompleteTextView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Observable.just(1)
    }
}




