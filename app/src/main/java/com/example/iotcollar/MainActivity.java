package com.example.iotcollar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DatabaseReference mDatabase;
    private TextView mDogTempTextView;
    private TextView mDoorOcTextView;
    private TextView mDistanceTextView;
    private TextView mEnviTHTextView;
    private TextView mLocationTextView;
    private TextView mMessageTextView;
    private Button mLocateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        mDogTempTextView = findViewById(R.id.dog_temp_textview);
        mDoorOcTextView = findViewById(R.id.door_oc_textview);
        mDistanceTextView = findViewById(R.id.distance_textview);
        mEnviTHTextView = findViewById(R.id.envi_t_h_textview);
        mLocationTextView = findViewById(R.id.location_textview);
        mMessageTextView = findViewById(R.id.message_textview);
        mLocateButton = findViewById(R.id.locate_button);

        // Set button click listener
        mLocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mLocationButton clicked");

                // Retrieve data from Firebase Realtime Database
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String dogTemp = snapshot.child("DOG_TEMP").getValue(String.class);
                        String doorOc = snapshot.child("DOOR_OC").getValue(String.class);
                        String distance = snapshot.child("Distance").getValue(String.class);
                        String enviTH = snapshot.child("ENVI_T_H").getValue(String.class);
                        String location = snapshot.child("Location").getValue(String.class);
                        String message = snapshot.child("Message").getValue(String.class);

                        // Update UI elements
                        mDogTempTextView.setText("Dog Temperature: " + dogTemp);
                        mDoorOcTextView.setText("Door Status: " + doorOc);
                        mDistanceTextView.setText("Distance: " + distance);
                        mEnviTHTextView.setText("Environment Temperature and Humidity: " + enviTH);
                        mLocationTextView.setText("Location: " + location);
                        mMessageTextView.setText("Message: " + message);

                        mLocationTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d(TAG, "mLocationTextView clicked");
                                Log.d(TAG, "location: " + location);
                                // Launch Google Maps intent with location as query parameter
                                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location);
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                Log.d(TAG, "gmmIntentUri: " + gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getPackageManager()) == null) {
                                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mapIntent);
                                    Log.d(TAG, "Starting Google Maps intent");
                                }
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }
}
