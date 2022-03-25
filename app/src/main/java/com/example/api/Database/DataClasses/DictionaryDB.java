package com.example.api.Database.DataClasses;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {En_Ar_Table.class}, version = 1)
public abstract class DictionaryDB extends RoomDatabase {

    public abstract DictionaryDAO DictionaryDAO();

    private static DictionaryDB ourInstance;

    public static DictionaryDB getInstance(Context context) {

        if (ourInstance == null) {

            ourInstance = Room.databaseBuilder(context,

                    DictionaryDB.class, "Dictionary.db")
                    .createFromAsset("dataBase/Dictionary.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return ourInstance;

    }
}
