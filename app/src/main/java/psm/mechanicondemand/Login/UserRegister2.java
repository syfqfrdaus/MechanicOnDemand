package psm.mechanicondemand.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

                    // Create a reference to the "vehicles" collection
                    CollectionReference vehiclesCollection = db.collection("vehicles");

                    // Create a new document reference under the user's ID
                    DocumentReference userRef = vehiclesCollection.document(userId);

                    // Get the current vehicle count for the user
                    userRef.collection("VehiCount").get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                int vehicleCount = queryDocumentSnapshots.size() + 1;

                                // Create a new document reference under "VehiCount" with the incremented count
                                DocumentReference vehicleCountRef = userRef.collection("VehiCount").document(String.valueOf(vehicleCount));

                                // Create a user object with the desired details
                                Map<String, Object> user = new HashMap<>();
                                user.put("VehicleType", type);
                                user.put("VehicleBrand", brand);
                                user.put("VehicleModel", model);
                                user.put("VehiclePlate", plate);

                                // Set the user object in the Firestore document
                                vehicleCountRef.set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UserRegister2.this, "User details added to Firestore", Toast.LENGTH_SHORT).show();
                                                // You can perform additional actions here if needed
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(UserRegister2.this, "Failed to add user details to Firestore", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                Intent intent = new Intent(getApplicationContext(), UserRequest.class);
                                startActivity(intent);
                                finish();

                                Toast.makeText(UserRegister2.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserRegister2.this, "Failed to get vehicle count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}