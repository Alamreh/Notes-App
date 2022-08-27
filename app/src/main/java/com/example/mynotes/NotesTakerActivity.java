package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mynotes.Model.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText title_edit, description_edit;
    ImageView saveButton;
    Notes notes;
    boolean isOldNotes=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notes_taker );

        description_edit=findViewById( R.id.edit_description );
        title_edit=findViewById( R.id.edit_title );
        saveButton =findViewById( R.id.save_button );

        notes=new Notes();
        try {
            //for change the old note set the title and description with the old notes;
            notes = (Notes) getIntent().getSerializableExtra( "old_note" );
            title_edit.setText( notes.getTitle() );
            description_edit.setText( notes.getNotes(  ) );

            isOldNotes=true;



        }catch (Exception e){

            e.printStackTrace();
        }


        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=title_edit.getText().toString();
                String description=description_edit.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText( NotesTakerActivity.this,"Please enter description",Toast.LENGTH_SHORT).show();
                    return;
                }

                //set date and tine using date fromat
                SimpleDateFormat dateformat=new SimpleDateFormat("EEE,d  mm yyyy HH:mm a");
                Date date=new Date();


                //set the data
                if(isOldNotes==false) {
                    notes = new Notes();

                }
                    notes.setTitle( title );
                    notes.setNotes( description );
                    notes.setDate( dateformat.format( date ) );

                    Intent intent = new Intent();
                    intent.putExtra( "note", notes );
                    setResult( Activity.RESULT_OK, intent );
                    finish();

            }
        } );

    }
}