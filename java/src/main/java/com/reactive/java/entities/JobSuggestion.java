package com.reactive.java.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class JobSuggestion {

    @PrimaryKey
    private final String uuid;
    private final String suggestion;
    private final String parent_uuid;

    public JobSuggestion(String uuid, String suggestion, String parent_uuid) {
        this.uuid = uuid;
        this.suggestion = suggestion;
        this.parent_uuid = parent_uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public String getParent_uuid() {
        return parent_uuid;
    }
}
