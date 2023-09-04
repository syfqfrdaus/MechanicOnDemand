package psm.mechanicondemand.Mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.MapFragment;
import psm.mechanicondemand.MapFragment2;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityRequestProcessBinding;

public class RequestProcess extends DrawerMechanic {

    ActivityRequestProcessBinding activityRequestProcessBinding;

    FirebaseFirestore db;

    double latitude;
    double longitude;

    String UID;
    String PhoneNo;
    String Plate;
    String Name;

    TextView CName;
    TextView CAddress;
    TextView CVehicle;
    TextView CPlate;
    TextView CPhone;

    Button btnCall;
    Button btnNavigate;
    Button btnArrived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestProcessBinding = activityRequestProcessBinding.inflate(getLayoutInflater());
        setContentView(activityRequestProcessBinding.getRoot());

        CName = findViewById(R.id.CustName);
        CAddress = findViewById(R.id.custAddress);
        CVehicle = findViewById(R.id.CustVehicle);
        CPlate = findViewById(R.id.CustPlate);
        CPhone = findViewById(R.id.CustPhone);
        btnCall = findViewById(R.id.btnCall);
        btnNavigate = findViewById(R.id.btnNavigate);
        btnArrived = findViewById(R.id.btnArrived);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");
        String userName = intent.getStringExtra("UserName");
        String userPhone = intent.getStringExtra("UserPhone");
        String vehiclePlate = intent.getStringExtra("VehiclePlate");
        String vehicleModel = intent.getStringExtra("VehicleModel");

        UID = userID;
        Name = userName;
        PhoneNo = userPhone;
        Plate = vehiclePlate;

        CName.setText("Name : "+userName);
        CVehicle.setText("Vehicle : "+vehicleModel);
        CPlate.setText("Vehicle Plate : "+vehiclePlate);
        CPhone.setText("Phone No : "+userPhone);



        Fragment fragment = new MapFragment2();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_map3, fragment).commit();

        //Fetch data of Latitude and Longitude and address
        db.collection("request")
                .document(UID) // Replace UID with the appropriate document ID
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            latitude = documentSnapshot.getDouble("Latitude");
                            longitude = documentSnapshot.getDouble("Longitude");
                            String Address = documentSnapshot.getString("Address");

                            CAddress.setText(Address);

                            // Create a Bundle to store the Latitude and Longitude values
                            Bundle args = new Bundle();
                            args.putDouble("Latitude", latitude);
                            args.putDouble("Longitude", longitude);

                            // Set the arguments on the fragment
                            fragment.setArguments(args);

                        } else {
                            Toast.makeText(RequestProcess.this, "Error Getting Request Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while fetching the document
                        Log.e("RequestProcess", "Error retrieving document: " + e.getMessage());
                    }
                });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent with the ACTION_DIAL action and the phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + PhoneNo));

                // Start the dialer activity
                startActivity(intent);

            }
        });

        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a Uri with the destination coordinates
                String uri = "google.navigation:q=" + latitude + "," + longitude;

                // Create an intent with the Uri
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                // Set the package for the intent (optional)
                intent.setPackage("com.google.android.apps.maps");

                // Check if there is an activity that can handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Open Google Maps with the navigation intent
                    startActivity(intent);
                } else {
                    // Google Maps is not installed on the device
                    Toast.makeText(RequestProcess.this, "Google Maps is not installed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("request")
                        .document(UID)
                        .update("Status", "Arrived")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RequestProcess.this, "Mechanic Has Arrived", Toast.LENGTH_SHORT).show();
                                Intent intent2 = new Intent(RequestProcess.this, MechanicRepairing.class);
                                intent2.putExtra("UserID", UID);
                                intent2.putExtra("UserName", Name);
                                intent2.putExtra("VPlate", Plate);
                                intent2.putExtra("UserPhone",PhoneNo);
                                startActivity(intent2);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error updating NewFee value
                                Log.e("Firestore Error", e.getMessage());
                                Toast.makeText(RequestProcess.this, "Error Updating Database", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}