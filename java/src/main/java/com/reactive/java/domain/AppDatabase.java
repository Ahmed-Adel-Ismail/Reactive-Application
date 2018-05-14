package com.reactive.java.domain;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.reactive.java.App;
import com.reactive.java.entities.JobSuggestion;

@Database(entities = {JobSuggestion.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String NAME = "AppDatabase.db";
    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null) {
            instance = initialize(App.getInstance());
        }
        return instance;
    }

    private static AppDatabase initialize(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, NAME)
                .build();
    }

    public abstract JobSuggestionsDao getJobSuggestions();

}
