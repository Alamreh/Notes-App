package com.example.mynotes;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mynotes.Adapter.NotesAdapter;
import com.example.mynotes.Model.Notes;
import com.example.mynotes.db.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Filter;

public class MainActivity extends AppCompatActivity  implements  PopupMenu.OnMenuItemClickListener {

    RecyclerView  recyclerView;
    NotesAdapter adapter;
    List<Notes> list=new ArrayList<>();
    SearchView searchView;

    Notes  selectednotes;

    //create a object of roomdb
    RoomDB roomDB;
    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        recyclerView=findViewById( R.id.recycler_view );
        fab_add=findViewById( R.id.add_button );

        roomDB=RoomDB.getInstance( this );
        searchView=findViewById( R.id.searchView );

        list=roomDB.notesDAO().getAll();

        updateRecycler(list);

        fab_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
                 startActivityForResult(intent,101);
            }
        } );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return  true;
            }
        } );

    }

    private void filter(String newText) {
        List<Notes> newlist=new ArrayList<>();
        //run the loop and check the entered data and data present in are equal add to the newlist;
        for(Notes singleNote: list){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase( ))
            || singleNote.getNotes().toLowerCase().contains( newText.toLowerCase() )){
                newlist.add(singleNote);
            }
        }
        adapter.filterList( newlist );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==101){
            if(resultCode== Activity.RESULT_OK){
                Notes new_notes=(Notes)data.getSerializableExtra(  "note");

                //Add new Notes to add database;

                roomDB.notesDAO().insert( new_notes );

                list.clear();
                list.addAll( roomDB.notesDAO().getAll() );
                adapter.notifyDataSetChanged();

            }
        }
        else if(requestCode==102){
            if(resultCode==Activity.RESULT_OK){
                Notes new_notes=(Notes) data.getSerializableExtra( "note" );
                roomDB.notesDAO().update( new_notes.getID(),new_notes.getTitle(),new_notes. getNotes() );
                list.clear();
                list.addAll(  roomDB.notesDAO().getAll());
                adapter.notifyDataSetChanged();

            }
        }
    }

    private void updateRecycler(List<Notes> list){
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new StaggeredGridLayoutManager( 2, LinearLayout.VERTICAL ) );
        adapter =new NotesAdapter( list,MainActivity.this, notesClickListener);
        recyclerView.setAdapter( adapter);
    }

    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {

           Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
           intent.putExtra( "old_note",notes );
           startActivityForResult( intent,102 );
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
             selectednotes=new Notes();
             selectednotes=notes;
             showPopUp(cardView);


        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu=new PopupMenu( this,cardView );
        popupMenu.setOnMenuItemClickListener( this );
        popupMenu.inflate( R.menu.popup_menu );
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.pin:
                if(selectednotes.isPinned())
                {
                    roomDB.notesDAO().pinned( selectednotes.getID(),false );
                    Toast.makeText( this,"Unpinned successfully",Toast.LENGTH_SHORT ).show();
                }
                else{
                    roomDB.notesDAO().pinned( selectednotes.getID(),true );
                    Toast.makeText( this,"pin successfully",Toast.LENGTH_SHORT ).show();

                }
                list.clear();
                list.addAll(  roomDB.notesDAO().getAll() );
                adapter.notifyDataSetChanged();
                 return  true;

            case  R.id.delete:
                roomDB.notesDAO().delete( selectednotes );
                list.remove( selectednotes );
                adapter.notifyDataSetChanged();
                Toast.makeText( this,"Note Deleted",Toast.LENGTH_SHORT ).show();
                return  true;

            default:
                return  false;

        }

    }
}