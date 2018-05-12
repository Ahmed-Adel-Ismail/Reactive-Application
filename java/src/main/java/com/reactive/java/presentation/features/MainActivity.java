package com.reactive.java.presentation.features;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.reactive.java.R;
import com.reactive.java.entities.JobSuggestion;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable disposables;
    private final Observable<EditText> jobName;
    private final Observable<Button> insert;
    private final Observable<TextView> jobsCount;

    public MainActivity() {
        disposables = new CompositeDisposable();
        jobName = Observable.defer(() -> Observable.just((EditText) findViewById(R.id.job_name)));
        insert = Observable.defer(() -> Observable.just((Button) findViewById(R.id.insert_job_name)));
        jobsCount = Observable.defer(() -> Observable.just((TextView) findViewById(R.id.jobs_count)));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        disposables.add(bindJobName(viewModel.jobSuggestionName));
        disposables.add(bindInsert(viewModel));
        disposables.add(bindJobsCount(viewModel.jobSuggestions));
    }

    private Disposable bindJobName(BehaviorSubject<String> jobSuggestionName) {
        return jobName.flatMap(RxTextView::textChangeEvents)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(String::valueOf)
                .subscribe(jobSuggestionName::onNext);
    }

    private Disposable bindInsert(MainViewModel viewModel) {
        return insert.flatMap(RxView::clicks)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapMaybe(object -> viewModel.insertJobSuggestionName())
                .subscribe(disposables::add);
    }

    private Disposable bindJobsCount(BehaviorSubject<List<JobSuggestion>> jobSuggestions) {
        return Observable.combineLatest(
                jobsCountView(),
                jobSuggestionsSize(jobSuggestions),
                this::jobsCountUpdater)
                .subscribe();


    }

    private Observable<Consumer<? super CharSequence>> jobsCountView() {
        return jobsCount
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(RxTextView::text);

    }

    private Observable<String> jobSuggestionsSize(BehaviorSubject<List<JobSuggestion>> jobSuggestions) {
        return jobSuggestions.share()
                .map(List::size)
                .map(String::valueOf)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Boolean jobsCountUpdater(Consumer<? super CharSequence> textChanger, String jobSuggestion) throws Exception {
        textChanger.accept(jobSuggestion);
        return true;
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
