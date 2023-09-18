package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.Login.UserRegister2;
import psm.mechanicondemand.MapFragment;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserRequestBinding;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRequest extends DrawerUser {

    ActivityUserRequestBinding activityUserRequestBinding;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore firestore;

    String selectedVehicleModel;
    String selectedService;
    double latitude;
    double longitude;

    Button btnRequest;
    EditText formAddress;
    EditText detail;
    Spinner spinVehicle;
    Spinner spinService;
    Button buttonRequest;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    int fee = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserRequestBinding = ActivityUserRequestBinding.inflate(getLayoutInflater());
        setContentView(activityUserRequestBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid();

        btnRequest = findViewById(R.id.btnGetRequest);
        formAddress = findViewById(R.id.formAddress);
        detail = findViewById(R.id.requestDetail);
        spinVehicle = findViewById(R.id.spinVehicle);
        spinService = findViewById(R.id.spinService);
        buttonRequest = findViewById(R.id.btnGetRequest);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_map, fragment).commit();

        // Get the current location from the MapFragment
        ((MapFragment) fragment).setOnLocationChangeListener(new MapFragment.OnLocationChangeListener() {
            @Override
            public void onLocationChange(Location location, String address) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    String LaLo = "Latitude: " + latitude + "\nLongitude: " + longitude;
                    formAddress.setText(LaLo + "\n" + address);
                } else {
                    formAddress.setText("Location not available");
                }
            }
        });

        // Retrieve the vehicle models from Firestore and populate the Spinner
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("vehicles")
                .document(userId) // Reference the user's document
                .collection("VehiCount") // Reference the "VehiCount" subcollection
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<String> vehicleModels = new ArrayList<>();

                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            String vehicleModel = document.getString("VehicleModel");
                            if (vehicleModel != null) {
                                vehicleModels.add(vehicleModel);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserRequest.this,
                                android.R.layout.simple_spinner_item, vehicleModels);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinVehicle.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle any errors that occurred while retrieving the vehicle models
                        String errorMessage = e.getMessage(); // Get the error message
                        // Create and show a Toast message with the error message
                        Toast.makeText(UserRequest.this, "Error fetching data: " + errorMessage, Toast.LENGTH_SHORT).show();

                    }
                });

        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedVehicleModel = parent.getItemAtPosition(position).toString();
                Toast.makeText(UserRequest.this, "Value selected are " + selectedVehicleModel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserRequest.this, "Please Select Vehicle", Toast.LENGTH_SHORT).show();
            }
        });

        // Create an ArrayList to store the service names (e.g., "Puncture", "Battery (Car)", etc.)
        List<String> serviceNames = new ArrayList<>();

        // Populate the Spinner with service names from Firestore
        db.collection("service")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            // Get the service name (e.g., "Puncture")
                            String serviceName = document.getId();
                            serviceNames.add(serviceName);
                        }

                        // Create an ArrayAdapter to populate the Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(UserRequest.this,
                                android.R.layout.simple_spinner_item, serviceNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // Set the adapter to the Spinner
                        spinService.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while retrieving the service names
                        Toast.makeText(UserRequest.this, "Error fetching service names: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // Set an OnItemSelectedListener for the Spinner
        spinService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected service name
                selectedService = parent.getItemAtPosition(position).toString();

                // Use the selected service name to fetch the corresponding price from Firestore
                db.collection("service")
                        .document(selectedService)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // Get the "Price" field from the selected service document
                                Double price = documentSnapshot.getDouble("Price");
                                if (price != null) {
                                    fee = price.intValue(); // Set the fee variable to the fetched price
                                    Toast.makeText(UserRequest.this, "Service Selected: " + selectedService + "\nPrice: " + fee, Toast.LENGTH_SHORT).show();
                                } else {
                                    // Handle the case where the price is not available
                                    Toast.makeText(UserRequest.this, "Price not available for " + selectedService, Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle any errors that occurred while fetching the price
                                Toast.makeText(UserRequest.this, "Error fetching price: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UserRequest.this, "Please Select Service", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullAddress, vehicleM, serviceT, RDetails;
                double Lati, Longi;

                FullAddress = formAddress.getText().toString();
                vehicleM = selectedVehicleModel;
                serviceT = selectedService;
                RDetails = detail.getText().toString();
                Lati = latitude;
                Longi = longitude;

                if (RDetails.isEmpty()) {
                    Toast.makeText(UserRequest.this, "Please fill details", Toast.LENGTH_LONG).show();
                } else {
                    // Create a new document reference with the user ID
                    DocumentReference userRef = db.collection("request").document(userId);

                    // Create a user object with the desired details
                    Map<String, Object> user = new HashMap<>();
                    user.put("Address", FullAddress);
                    user.put("VehicleModel", vehicleM);
                    user.put("ServiceType", serviceT);
                    user.put("RequestDetails", RDetails);
                    user.put("Latitude", Lati);
                    user.put("Longitude", Longi);
                    user.put("Status", "Active");
                    user.put("Fee", fee);
                    user.put("NewFee", fee);
                    user.put("NegoStatus", "Idle");

                    // Set the user object in the Firestore document
                    userRef.set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UserRequest.this, "Request added to Firestore", Toast.LENGTH_SHORT).show();
                                    // You can perform additional actions here if needed
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserRequest.this, "Failed to add request to Firestore", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Intent intent = new Intent(getApplicationContext(), FindingMechanic.class);
                    startActivity(intent);
                    finish();


                    Toast.makeText(UserRequest.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}