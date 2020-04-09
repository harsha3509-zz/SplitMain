package com.example.slip_admin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.zip.Inflater;

public class ArtistList extends ArrayAdapter {
    private Activity context;
    private List<Artist> artistList;

    public ArtistList(Activity context,List<Artist> artistList){
        super(context,R.layout.list_layout,artistList);
        this.context = context;
        this.artistList = artistList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewRating);
        TextView email = (TextView)listViewItem.findViewById(R.id.email);
        TextView address = (TextView)listViewItem.findViewById(R.id.address);

        Artist artist = artistList.get(position);
        textViewName.setText(artist.getArtistName());
        textViewGenre.setText("Ticket ID "+artist.getArtistId());

        address.setText("Address: "+artist.getAddress());
        email.setText(artist.getEmail());


        return listViewItem;


    }
}
