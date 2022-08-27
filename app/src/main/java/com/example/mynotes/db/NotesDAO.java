package com.example.mynotes.db;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mynotes.Model.Notes;

import java.util.List;

@Dao
public interface NotesDAO {
 //if a thing is already present replace it;
    @Insert(onConflict = REPLACE)
   void insert(Notes notes);

    //fetch All the data from notes table or arrange in descending order;
    @Query( "Select * from notes order by  ID DESC" )
    List<Notes> getAll();


    //update the id title note in room database;
    @Query( "Update notes Set title=:title,notes=:note Where ID=:id" )
    void update(int id,String title,String note);


    //Delete the notes
    @Delete
    void delete(Notes notes);


    @Query( "UPDATE notes set  pin = :ispin where  ID=:id" )
    void  pinned(int id,boolean ispin);
}
