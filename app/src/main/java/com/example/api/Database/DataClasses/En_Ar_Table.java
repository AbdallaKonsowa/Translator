package com.example.api.Database.DataClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "en_ar")
public class En_Ar_Table {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String local_Lan,local_text,target_Lan,target_text;
}
