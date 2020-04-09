package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



import java.io.IOException;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity {
    Button get_place,Submit,Upload, sub;
    private TextView textView;
    ImageView img;
    public String LatLong;
    public String address;
    EditText email;
    EditText name;
    StorageReference mStorageRef;
    private StorageTask uploadTask;
    public Uri imguri;
    private Uri filePath;
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    DatabaseReference databaseArtists;
    ListView listviewArtists;
    List<Artist> artistList;
    String add;
    String Latlon;





    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;

    int PLACE_PICKER_REQUEST = 1;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseArtists = FirebaseDatabase.getInstance().getReference("Complaints");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        textView = (TextView)findViewById(R.id.textView);
        get_place = (Button)findViewById(R.id.location);
        Submit = (Button)findViewById(R.id.submit);
        Upload = (Button) findViewById(R.id.ImageUpload);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        img  = (ImageView)findViewById(R.id.imageView);
        final String[] Error = new String[1];
        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(uploadTask != null && uploadTask.isInProgress()){
                        Toast.makeText(HomeActivity.this,"Uploading in Progress",Toast.LENGTH_LONG).show();
                    }
                    else{
                    FileUploader();}
                    addArtist();
                }

                catch (Exception e){
                    Error[0] = e.toString();
                }


                Intent homeIntent = new Intent(HomeActivity.this,MainActivity.class);

                startActivity(homeIntent);

            }
        });

        get_place  = findViewById(R.id.location);
        get_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(HomeActivity.this,MapsActivity.class);


                startActivity(homeIntent);

                finish();


            }
        });
        Intent homeIntent = getIntent();
        address = homeIntent.getStringExtra("address");
        LatLong = homeIntent.getStringExtra("Coordinates");
        add = address;
        Latlon = LatLong;
        textView.setText("Address: "+address);

    }


    private void addArtist(){
        String Email = email.getText().toString().trim();
        String Name = name.getText().toString().trim();

        if(!TextUtils.isEmpty(Email)&&!TextUtils.isEmpty(Name)) {
            String id = databaseArtists.push().getKey();

            Artist artist = new Artist(id,Name,Email,add,Latlon);
            databaseArtists.child(id). setValue(artist);
            //Toast.makeText(this,"Name ,Email, address and Coordinates added", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Enter Name, Email, address and Location",Toast.LENGTH_LONG).show();
        }
    }
    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void FileUploader(){

        /***

        StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
        uploadTask=Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(HomeActivity.this,"image Uploaded Successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

         ***/
        try{

            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

                //Toast.makeText(HomeActivity.this, "Here", Toast.LENGTH_SHORT).show();
                //StorageReference ref = storageReference.child(System.currentTimeMillis()+".jpg");

                ref.putFile(filePath)

                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Request Submitted", Toast.LENGTH_SHORT).show();
                                Toast.makeText(HomeActivity.this, "Submit Another Request " , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");



                            }
                        });




            } else {
                Toast.makeText(HomeActivity.this, "File Path Empty ", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(HomeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    private void FileChooser(){
        /***
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
        ***/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);



    }
    @Override
    /***
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && requestCode == RESULT_OK && data!= null && data.getData()!=null){
            img.setImageURI(imguri);
        }
    }
     ***/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }




    /**
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stringBuilder = new StringBuilder();

                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                stringBuilder.append("LATITUDE :");
                stringBuilder.append(latitude);
                stringBuilder.append("\n");
                stringBuilder.append("LONGITUDE :");
                stringBuilder.append(longitude);
                Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show();


            }
        }
    }
    ***/

}




