package com.reactive.java.domain;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.reactive.java.entities.JobSuggestion;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface JobSuggestionsDao {

    @Query("select * from JobSuggestion")
    Flowable<List<JobSuggestion>> queryAll();

    @Query("select * from JobSuggestion where suggestion LIKE :contains")
    Flowable<JobSuggestion> query(String contains);

}
