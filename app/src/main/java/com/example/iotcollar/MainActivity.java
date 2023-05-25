package com.example.iotcollar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;
    private TextView dogTempTV;
    private TextView doorOCTV;
    private TextView distanceTV;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;

    // variable for Text view.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("Data");

        // initializing our object class variable.
        //Initiazlize
        dogTempTV = findViewById(R.id.idTVDogTemp);
        doorOCTV = findViewById(R.id.idTVDoorOC);
        distanceTV = findViewById(R.id.idTVDistance);

        // calling method
        // for getting data.
        getdata();
    }

    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dogTemp = snapshot.child("DOG_TEMP").getValue(String.class);
                String doorOC = snapshot.child("DOOR_OC").getValue(String.class);
                String distance = snapshot.child("Distance").getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.
                dogTempTV.setText(dogTemp);
                doorOCTV.setText(doorOC);
                distanceTV.setText(distance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
