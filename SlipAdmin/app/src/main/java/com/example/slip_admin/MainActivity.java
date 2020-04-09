package com.example.slip_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.myapplication;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";
    public static final String Email = "Email";
    public static final String address = "Address";
    DatabaseReference databaseArtists;
    ListView listviewArtists;
    List<Artist> artistList;
    DatabaseReference databaseReference;
    private StorageReference mStorageRef;


    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter ;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseArtists = FirebaseDatabase.getInstance().getReference("Complaints");
        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_LONG);
        System.out.println("Hello");
        // Assign id to RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

// Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        listviewArtists = findViewById(R.id.listViewComplaints);
        artistList = new ArrayList<>();


        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(MainActivity.this);

// Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images From Firebase.");
        progressDialog.show();
        Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_LONG);

        // Adding Add Value Event Listener to databaseReference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //postSnapshot.getValue().toString();

                    Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_LONG);
                    System.out.println(postSnapshot.getValue().toString());
                    //Toast.makeText(MainActivity.this,postSnapshot.getValue().toString(),Toast.LENGTH_LONG);
                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(postSnapshot.getValue().toString());




                    list.add(imageUploadInfo);
                }

                adapter = new RecyclerViewAdapter(getApplicationContext(), list);

                recyclerView.setAdapter(adapter);

                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                progressDialog.dismiss();

            }
        });

// Showing progress dialog.
        progressDialog.show();
        listviewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);
                Intent intent = new Intent(getApplicationContext(),AddTrackActivity.class);
                intent.putExtra(ARTIST_ID, artist.getArtistId());
                intent.putExtra(ARTIST_NAME, artist.getArtistName());
                intent.putExtra(Email,artist.getEmail());
                intent.putExtra(address,artist.getAddress());
                startActivity(intent);
                //Toast.makeText(this,artist.getArtistName().toString(),Toast.LENGTH_LONG).show();

            }
        });
        listviewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistList.get(position);

                showUpdateDeleteDialog(artist.getArtistId(),artist.getArtistName());

                return false;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                artistList.clear();
                for(DataSnapshot artistSnapshot: dataSnapshot.getChildren() ){
                    Artist artist = artistSnapshot.getValue(Artist.class);

                    artistList.add(artist);

                }
                ArtistList adapter = new ArtistList(MainActivity.this,artistList);
                listviewArtists.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showUpdateDeleteDialog(final String artistId, String artistName){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView =  inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editText);
        final Spinner spinnerGeners = (Spinner) dialogView.findViewById(R.id.spinner);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonMain);

        dialogBuilder.setTitle("Updating Artist    "+artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String name = editTextName.getText().toString().trim();
                String name = "Akku";
                String genre = spinnerGeners.getSelectedItem().toString();
                if(!TextUtils.isEmpty(name)){
                    updateArtist(artistId,name,genre);
                    alertDialog.dismiss();
                }

            }
        });

    }



    private boolean updateArtist(String id, String name,String genre){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artists").child(id);
        String Email = null;
        Artist artist = new Artist(id,name,Email);
        databaseReference.setValue(artist);
        Toast.makeText(this,"Artist Updated Successfully", Toast.LENGTH_LONG).show();
        return true;
    }




}
