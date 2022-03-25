package com.example.api.Database.DataClasses;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictionaryDAO {

    @Query("SELECT * from en_ar ")
    List<En_Ar_Table> SelectAll();

    @Query("select id FROM en_ar WHERE local_Lan=:local_Lan and local_text=:local_text and target_Lan=:target_Lan limit 1")
    int exists(String local_Lan,String local_text,String target_Lan);

    @Insert
    long insert(En_Ar_Table Table_Row);
}
