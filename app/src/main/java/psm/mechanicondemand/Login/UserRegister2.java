package psm.mechanicondemand.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import psm.mechanicondemand.Mechanic.MechanicRequest;
import psm.mechanicondemand.R;
import psm.mechanicondemand.User.UserRequest;

public class UserRegister2 extends AppCompatActivity {

    EditText vName;
    EditText vBrand;
    EditText vModel;
    EditText vPlate;
    Button btnVAdd;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register2);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        vName = findViewById(R.id.VehicleName);
        vBrand = findViewById(R.id.VehicleBrand);
        vModel = findViewById(R.id.VehicleModel);
        vPlate = findViewById(R.id.VehiclePlate);
        btnVAdd = findViewById(R.id.btnAddVehicle);

        btnVAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type, brand, model, plate;
                type = vName.getText().toString();
                brand = vBrand.getText().toString();
                model = vModel.getText().toString();
                plate = vPlate.getText().toString();

                if (type.isEmpty() || brand.isEmpty() || model.isEmpty() || plate.isEmpty()) {
                    Toast.makeText(UserRegister2.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    // Get the user ID of the currently authenticated user
                    String userId = mAuth.getCurrentUser().getUid();

                    // Get a reference to the 'vehicles' collection for the user
                    CollectionReference userVehiclesRef = FirebaseFirestore.getInstance()
                            .collection("vehicles")
                            .document(userId)
                            .collection("VehiCount");

                    // Get the current 'VCount' value and increment it by 1
                    DocumentReference userDocRef = FirebaseFirestore.getInstance()
                            .collection("vehicles")
                            .document(userId);

                    // Check if the user document exists
                    userDocRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        // User document exists; proceed with updating 'VCount'
                                        long currentCount = documentSnapshot.getLong("VCount");
                                        long newCount = currentCount + 1;

                                        // Update the 'VCount' field with the new value
                                        userDocRef.update("VCount", newCount)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Now that 'VCount' is updated, use it as the new document ID
                                                        String newVehicleId = String.valueOf(newCount);

                                                        // Create a new vehicle document with the new ID
                                                        DocumentReference newVehicleDoc = userVehiclesRef.document(newVehicleId);

                                                        // Set the vehicle data as fields in the new document
                                                        Map<String, Object> vehicleData = new HashMap<>();
                                                        vehicleData.put("VehicleBrand", brand);
                                                        vehicleData.put("VehicleModel", model);
                                                        vehicleData.put("VehiclePlate", plate);
                                                        vehicleData.put("VehicleType", type);

                                                        newVehicleDoc.set(vehicleData)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        // Vehicle added successfully
                                                                        Toast.makeText(UserRegister2.this, "Vehicle added successfully!", Toast.LENGTH_LONG).show();

                                                                        // Navigate to another activity here
                                                                        Intent intent = new Intent(getApplicationContext(), UserRequest.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Handle errors
                                                                        Toast.makeText(UserRegister2.this, "Failed to add vehicle: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle errors
                                                        Toast.makeText(UserRegister2.this, "Failed to update VCount: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        // User document does not exist; create it with 'VCount' set to 1
                                        Map<String, Object> initialUserData = new HashMap<>();
                                        initialUserData.put("VCount", 1);

                                        userDocRef.set(initialUserData)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // User document created successfully; now proceed with adding the first vehicle
                                                        String newVehicleId = "1"; // Initialize with 1 as the document ID for the first vehicle

                                                        // Create a new vehicle document with the new ID
                                                        DocumentReference newVehicleDoc = userVehiclesRef.document(newVehicleId);

                                                        // Set the vehicle data as fields in the new document
                                                        Map<String, Object> vehicleData = new HashMap<>();
                                                        vehicleData.put("VehicleBrand", brand);
                                                        vehicleData.put("VehicleModel", model);
                                                        vehicleData.put("VehiclePlate", plate);
                                                        vehicleData.put("VehicleType", type);

                                                        newVehicleDoc.set(vehicleData)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        // First vehicle added successfully
                                                                        Toast.makeText(UserRegister2.this, "First vehicle added successfully!", Toast.LENGTH_LONG).show();

                                                                        // Navigate to another activity here
                                                                        Intent intent = new Intent(getApplicationContext(), UserRequest.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Handle errors
                                                                        Toast.makeText(UserRegister2.this, "Failed to add first vehicle: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle errors
                                                        Toast.makeText(UserRegister2.this, "Failed to create user document: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
}