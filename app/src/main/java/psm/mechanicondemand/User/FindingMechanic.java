package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityFindingMechanicBinding;

public class FindingMechanic extends DrawerUser {

    ActivityFindingMechanicBinding activityFindingMechanicBinding;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ListenerRegistration listenerRegistration; // ListenerRegistration to track and remove the listener

    Button btnAccept;
    Button btnDecline;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFindingMechanicBinding = activityFindingMechanicBinding.inflate(getLayoutInflater());
        setContentView(activityFindingMechanicBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        // Listen for changes in the request document
        listenerRegistration = db.collection("request")
                .document(userId)
                .addSnapshotListener((documentSnapshot, error) -> {
                    if (error != null) {
                        // Handle the error
                        return;
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // Check if the "NegoStatus" field exists in the document
                        if (documentSnapshot.contains("NegoStatus")) {

                            // Get the value of NegoStatus from the document
                            String negoStatus = documentSnapshot.getString("NegoStatus");

                            // Check if the NegoStatus is equal to "Pending"
                            if (negoStatus != null && negoStatus.equals("Pending")) {

                                // Check if the "NewFee" field exists in the document
                                if (documentSnapshot.contains("NewFee")) {

                                    // Get the updated value of NewFee from the document
                                    int newFee = documentSnapshot.getLong("NewFee").intValue();

                                    // Call the method to display the dialog with the updated NewFee value
                                    getNewFee(newFee);
                                }
                            } else if (negoStatus != null && negoStatus.equals("Accepted")) {
                                // NegoStatus is "Accepted", perform the operation
                                performOperation();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove the listener when the activity is destroyed
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    private void getNewFee(int newFee) {
        // Implement the logic to display the dialog with the new fee value
        // You can access the EditText view and set its text to the newFee value
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_unego, null);
        dialogBuilder.setView(dialogView);

        AlertDialog dialog = dialogBuilder.create();

        //Display the proposed fee
        TextView newFeeTextView = dialogView.findViewById(R.id.ProposedFee);
        newFeeTextView.setText(String.valueOf(newFee));

        btnAccept = dialogView.findViewById(R.id.btnAcceptNego);
        btnDecline = dialogView.findViewById(R.id.btnDeclineNego);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the Status field in the Request document
                db.collection("request")
                        .document(userId)
                        .update("Fee", newFee, "NegoStatus", "Accepted")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(FindingMechanic.this, "New Fee Accepted", Toast.LENGTH_SHORT).show();

                                performOperation();
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

                // Close the dialog or perform any other actions
                dialog.dismiss();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch the Fee value from the database
                db.collection("request")
                        .document(userId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Get the current value of Fee
                                    int fee = documentSnapshot.getLong("Fee").intValue();

                                    // Update the NewFee field in the Request document
                                    db.collection("request")
                                            .document(userId)
                                            .update("NewFee", fee, "NegoStatus", "Active")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(FindingMechanic.this, "New Fee Declined", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Failed to update the NewFee field
                                                    // Handle the error
                                                    Log.e("Firestore Error", e.getMessage());
                                                }
                                            });
                                } else {
                                    // Document doesn't exist, handle the error
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to fetch the Fee value, handle the error
                                Log.e("Firestore Error", e.getMessage());
                            }
                        });

                // Close the dialog or perform any other actions
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void performOperation() {
        // Implement the logic for the operation when NegoStatus is "Accepted"
        // This method will be called when NegoStatus is "Accepted"
        Intent intent = new Intent(FindingMechanic.this, UserWaiting.class);
        intent.putExtra("UserID", userId);
        startActivity(intent);
    }
}