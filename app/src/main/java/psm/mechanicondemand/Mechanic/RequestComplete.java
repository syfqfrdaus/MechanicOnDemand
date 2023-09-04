package psm.mechanicondemand.Mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityMechanicRepairingBinding;
import psm.mechanicondemand.databinding.ActivityRequestCompleteBinding;

public class RequestComplete extends DrawerMechanic {

    ActivityRequestCompleteBinding activityRequestCompleteBinding;

    Button bComplete;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRequestCompleteBinding = activityRequestCompleteBinding.inflate(getLayoutInflater());
        setContentView(activityRequestCompleteBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");
        String userName = intent.getStringExtra("userName");
        String VehicleName = intent.getStringExtra("VehicleName");
        String Address = intent.getStringExtra("Address");
        String Service = intent.getStringExtra("Service");
        double Price = intent.getDoubleExtra("Price", 0);

        activityRequestCompleteBinding.CCUserName.setText("User Name : " + userName);
        activityRequestCompleteBinding.CCUserVehicle.setText("Vehicle Model :" + VehicleName);
        activityRequestCompleteBinding.CCAddress.setText("Address : " + Address);
        activityRequestCompleteBinding.CCService.setText("Service : " + Service);
        activityRequestCompleteBinding.CCTotalFee.setText("Fee : " + Price);

        bComplete = findViewById(R.id.btnComplete);

        String mechanicID = mAuth.getCurrentUser().getUid();

        bComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a Map to hold the data
                Map<String, Object> historyData = new HashMap<>();
                historyData.put("MechanicID", mechanicID);
                historyData.put("UserID", userID);
                historyData.put("userName", userName);
                historyData.put("VehicleName", VehicleName);
                historyData.put("Address", Address);
                historyData.put("Service", Service);
                historyData.put("Price", Price);

                // Reference to the "historyCounter" document
                DocumentReference counterRef = db.collection("counters").document("historyCounter");

                // Increment the historyCounter and get the new ID in a transaction
                db.runTransaction(new Transaction.Function<Long>() {
                    @Override
                    public Long apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot counterSnapshot = transaction.get(counterRef);
                        Long currentCounter = 0L; // Default value if the document doesn't exist

                        if (counterSnapshot.exists()) {
                            currentCounter = counterSnapshot.getLong("value");
                        }

                        // Increment the counter
                        long newCounter = currentCounter + 1;

                        // Update the counter document with the new value
                        transaction.set(counterRef, new HashMap<String, Object>() {{
                            put("value", newCounter);
                        }});

                        // Return the new ID
                        return newCounter;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Long>() {
                    @Override
                    public void onSuccess(Long newId) {
                        // Now, use 'newId' as the ID for the new document in the "history" collection
                        String documentPath = "history/" + newId;

                        // Add the data to Firestore with the new auto-incremented ID
                        db.document(documentPath).set(historyData)
                                .addOnSuccessListener(aVoid -> {
                                    // Data added successfully
                                    Toast.makeText(RequestComplete.this, "Data added successfully", Toast.LENGTH_SHORT).show();

                                })
                                .addOnFailureListener(e -> {
                                    // Handle any errors here
                                    Toast.makeText(RequestComplete.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors here
                        Toast.makeText(RequestComplete.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                //Not tested yet
                Intent intent2 = new Intent(getApplicationContext(), MechanicRequest.class);
                startActivity(intent2);
                finish();
            }
        });


    }
}