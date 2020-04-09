package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_pick;
    TextView address;
    TextView LL;
    private List<Place.Field> fields;
    final int PLACE_PICKER_REQUESTCODE = 1;
    String name;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        btn_pick = findViewById(R.id.btn_pick);
        fields = Arrays.asList(Place.Field.NAME,Place.Field.LAT_LNG);
        // Add an import statement for the client library.



        Places.initialize(getApplicationContext(), "AIzaSyAAEuMvvM82VDIZwKI6qixfU9J1qQavSuQ");

        final PlacesClient placesClient = Places.createClient(this);
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(MapsActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUESTCODE);



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                final Place place = Autocomplete.getPlaceFromIntent(data);

                    if (place != null) {
                        name = place.getName();
                        latLng = place.getLatLng();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        mMap.animateCamera(update);
                        address = findViewById(R.id.address);
                        //address.setText("Address  " +place.getName());
                        LL = findViewById(R.id.latlong);
                        //LL.setText("Coordinates "+place.getLatLng().toString());

                        LL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent homeIntent = new Intent(MapsActivity.this,HomeActivity.class);
                                homeIntent.putExtra("address", name.toString());
                                homeIntent.putExtra("Coordinates", latLng.toString());
                                startActivity(homeIntent);
                            }
                        });
                        address.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent homeIntent = new Intent(MapsActivity.this,HomeActivity.class);
                                homeIntent.putExtra("address", place.getName().toString());
                                homeIntent.putExtra("Coordinates", place.getLatLng().toString());
                                startActivity(homeIntent);

                            }
                        });

                        Toast.makeText(this,  "Address is "+place.getName(), Toast.LENGTH_SHORT).show();
                    }
                    // reverse geoCoding to get Street Address, city,state and postal code

                }
            }
        }
        @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
