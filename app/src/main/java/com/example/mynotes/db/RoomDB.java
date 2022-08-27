package com.example.mynotes.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mynotes.Model.Notes;

//In databsase first defined the entity and version
@Database( entities = Notes.class,version = 1,exportSchema = false)
public  abstract class RoomDB extends RoomDatabase {
    private static  RoomDB database;
    private static String Database_name="NoteApp";


    public synchronized static RoomDB getInstance(Context context){
         if(database==null){
              database= Room.databaseBuilder( context.getApplicationContext(),
                      RoomDB.class, Database_name). allowMainThreadQueries()
                      .fallbackToDestructiveMigration().build();

         }
         return database;
    }

    //Create instance of the DAO

    public abstract NotesDAO notesDAO();

}
