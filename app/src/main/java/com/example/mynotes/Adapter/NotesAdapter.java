package com.example.mynotes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.Model.Notes;
import com.example.mynotes.NotesClickListener;
import com.example.mynotes.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

public class NotesAdapter  extends RecyclerView.Adapter<NotesViewHolder> {

    List<Notes>  listarr;
    Context context;
    NotesClickListener notesClickListener;

    public NotesAdapter(List<Notes> notesarr, Context context, NotesClickListener notesClickListener) {
         listarr = notesarr;
        this.context = context;
        this.notesClickListener = notesClickListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new NotesViewHolder( LayoutInflater.from( context ).inflate( R.layout.notes_list,parent,false ) );
    }



    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
         holder.text_title.setText( listarr.get( position ).getTitle() );
         holder.text_title.setSelected( true );

         holder.description.setText(listarr.get( position ).getNotes());
         holder.date.setText( listarr.get( position ).getDate() );
         //use for horizontal scrolling
         holder.date.setSelected( true );

         if(listarr.get( position ).isPinned()){
             holder.pin.setImageResource( R.drawable.pin );

         }
         else{
             holder.pin.setImageResource( 0);
         }
         //add color to notes container
        int color_code=getRandomColor();
         holder.notesConatainer.setCardBackgroundColor(holder.itemView.getResources().getColor( color_code ));

         holder.notesConatainer.setOnClickListener( new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 notesClickListener.onClick( listarr.get(holder.getAdapterPosition()) );
             }
         } );


         holder.notesConatainer.setOnLongClickListener( new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 notesClickListener.onLongClick( listarr.get(holder.getAdapterPosition()),holder.notesConatainer );
                 return true;
             }
         } );
    }

    //create a list of color to show every time we refresh the app color is changed
    private int getRandomColor(){
        List<Integer>  colorcode = new ArrayList<>();
        colorcode.add( R.color.color1 );
        colorcode.add( R.color.color2 );
        colorcode.add( R.color.color3 );
        colorcode.add( R.color.color4 );
        colorcode.add( R.color.color5 );
        //pick a random color everytime

        Random random=new Random();
        int radom_color =random.nextInt(colorcode.size());
        return  colorcode.get( radom_color );
    }


    @Override
    public int getItemCount() {
        return  listarr.size();
    }

    //implement a method filtered list to filter the list according to the serachbar
    public  void filterList(List<Notes> filteredlist){
        listarr=filteredlist;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder{


    CardView notesConatainer;
    TextView text_title,description,date;
    ImageView pin;
    public NotesViewHolder(@NonNull View itemView) {
        super( itemView );
        notesConatainer=itemView.findViewById( R.id.notes_container );
        text_title=itemView.findViewById( R.id.title );
        description=itemView.findViewById( R.id.notes_descciption );
        date=itemView.findViewById( R.id.notes_date );
        pin=itemView.findViewById( R.id.pin );

    }


}
