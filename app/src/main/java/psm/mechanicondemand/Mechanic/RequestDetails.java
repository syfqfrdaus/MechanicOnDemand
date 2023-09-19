package psm.mechanicondemand.Mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.Map;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.R;
import psm.mechanicondemand.Request;
import psm.mechanicondemand.User.FindingMechanic;
import psm.mechanicondemand.databinding.ActivityRequestDetailsBinding;

public class RequestDetails extends DrawerMechanic {

    ActivityRequestDetailsBinding activityRequestDetailsBinding;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    Request selectedRequest;

    Button btnNego;
    Button btnAcceptR;

    String UserID;
    String userName;
    String userPhone = "";
    String vehiclePlate;
    String VModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        activityRequestDetailsBinding = activityRequestDetailsBinding.inflate(getLayoutInflater());
        setContentView(activityRequestDetailsBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnNego = findViewById(R.id.btnNegotiate);
        btnAcceptR = findViewById(R.id.btnAcceptRequest);
        TextView address = findViewById(R.id.addressDetails);
        TextView serviceType = findViewById(R.id.DetailsServiceType);
        TextView DetailsVehicle = findViewById(R.id.DetailsVehicleName);
        TextView Details = findViewById(R.id.DetailsRemarks);
        TextView RDistance = findViewById(R.id.ReqDistance);
        TextView requesterName = findViewById(R.id.RequesterName);
        TextView requesterPhone = findViewById(R.id.RequesterPhone);
        TextView offeredPrice = findViewById(R.id.OfferedPrice);

        // Retrieve the selected request from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("request")) {
            selectedRequest = (Request) intent.getSerializableExtra("request");
        }

        // Display the details of the selected request
        if (selectedRequest != null) {

            double distanceInKilometers = selectedRequest.getDistance() / 1000.0;
            String formattedDistance = String.format("%.2f km", distanceInKilometers);
            RDistance.setText(formattedDistance);

            // Set other TextViews or views with the corresponding details from selectedRequest
            address.setText(selectedRequest.getAddress());
            serviceType.setText(selectedRequest.getServiceType());
            DetailsVehicle.setText(selectedRequest.getVehicleModel());
            Details.setText(selectedRequest.getRequestDetails());

            int fee = selectedRequest.getFee();
            offeredPrice.setText(String.valueOf(fee));

            //Fetch the document ID which is the same as user ID.
            UserID = selectedRequest.getDocumentId();

            // Fetch the user details from Firestore based on the document ID
            db.collection("users")
                    .document(UserID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                userName = documentSnapshot.getString("Name");
                                userPhone = documentSnapshot.getString("PhoneNo");

                                requesterName.setText(userName);
                                requesterPhone.setText(userPhone);
                            } else {
                                // User document does not exist
                                requesterName.setText("N/A");
                                requesterPhone.setText("N/A");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error fetching user details
                            Log.e("Firestore Error", e.getMessage());
                        }
                    });

            VModel = selectedRequest.getVehicleModel();

            // Reference to the "vehicles" collection for the specific user
            DocumentReference userVehiclesRef = db.collection("vehicles").document(UserID);


            //Find ways to get vehicle plate no



        }

        btnNego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialognegoPrice();
            }
        });

        btnAcceptR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the Status field in the Request document
                db.collection("request")
                        .document(UserID)
                        .update("NegoStatus", "Accepted")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(RequestDetails.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to update the Status field
                                // Handle the error
                                Log.e("Firestore Error", e.getMessage());
                            }
                        });
            }
        });

        // Fetch the user details from Firestore based on the document ID
        db.collection("request")
                .document(UserID)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        String negoStatus = documentSnapshot.getString("NegoStatus");

                        if (negoStatus != null && negoStatus.equals("Declined")) {
                            Toast.makeText(RequestDetails.this, "User Has Declined Your Fee", Toast.LENGTH_SHORT).show();
                        } else if (negoStatus.equals("Accepted")) {
                            Toast.makeText(RequestDetails.this, "User Has Accepted Your Fee", Toast.LENGTH_SHORT).show();
                            String mechanicID = mAuth.getCurrentUser().getUid();

                            // Update the document with the mechanicID
                            DocumentReference requestRef = FirebaseFirestore.getInstance().collection("request")
                                    .document(UserID);
                            requestRef.update("MechanicID", mechanicID)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Create an intent to start the new activity
                                            Intent intent2 = new Intent(RequestDetails.this, RequestProcess.class);
                                            intent2.putExtra("UserID", UserID);
                                            intent2.putExtra("UserName", userName);
                                            intent2.putExtra("UserPhone", userPhone);
                                            intent2.putExtra("VehicleModel", selectedRequest.getVehicleModel());
                                            startActivity(intent2);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure
                                            Toast.makeText(RequestDetails.this, "Failed to update mechanic ID", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }
                });

    }

    private void dialognegoPrice() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_mnego, null);
        dialogBuilder.setView(dialogView);

        AlertDialog dialog = dialogBuilder.create(); // Declare the dialog as a final variable

        Button btnNewFee = dialogView.findViewById(R.id.btnNewFee);
        EditText newFee = dialogView.findViewById(R.id.newFee);

        // Set click listeners for the buttons in the dialog
        btnNewFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feeInput = newFee.getText().toString();
                if (!feeInput.isEmpty()) {
                    int enteredFee = Integer.parseInt(feeInput);

                    db.collection("request")
                            .document(UserID)
                            .update("NewFee", enteredFee, "NegoStatus", "Pending")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RequestDetails.this, "Successfully Sent To User", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss(); // Dismiss the dialog
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error updating NewFee value
                                    Log.e("Firestore Error", e.getMessage());
                                    // You can display an error message or handle the error in any desired way
                                }
                            });
                } else {
                    Toast.makeText(RequestDetails.this, "Please enter a fee value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}