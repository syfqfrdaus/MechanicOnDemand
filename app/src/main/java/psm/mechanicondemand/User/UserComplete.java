package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.Mechanic.MechanicRepairing;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserCompleteBinding;

public class UserComplete extends DrawerUser {

    ActivityUserCompleteBinding activityUserCompleteBinding;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complete);
        activityUserCompleteBinding = activityUserCompleteBinding.inflate(getLayoutInflater());
        setContentView(activityUserCompleteBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String mechID = intent.getStringExtra("MechID");
        String mechName = intent.getStringExtra("MechName");
        String mechPhone = intent.getStringExtra("MechPhone");

        activityUserCompleteBinding.UMechName.setText("Mechanic Name : " + mechName);
        activityUserCompleteBinding.UMechPhone.setText("Mechanic Phone : " + mechPhone);

        String userId = mAuth.getCurrentUser().getUid();

        // Get the user name from user table
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // The document exists, you can access its data
                    String userName = documentSnapshot.getString("Name");

                    activityUserCompleteBinding.UuserName.setText("User Name: " + userName);

                } else {
                    // Handle the case where the user document does not exist
                    Toast.makeText(UserComplete.this, "Data Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors while fetching the user document
                Toast.makeText(UserComplete.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Get the data from the request table
        // Reference to the 'request' document for the current user
        DocumentReference requestRef = db.collection("request").document(userId);

        requestRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // The document exists, you can access its data
                    String requestDetails = documentSnapshot.getString("RequestDetails");
                    String serviceType = documentSnapshot.getString("ServiceType");
                    String vehicleModel = documentSnapshot.getString("VehicleModel");
                    double fee = documentSnapshot.getDouble("Fee");

                    activityUserCompleteBinding.UVehiName.setText("Vehicle Name: " + vehicleModel);
                    activityUserCompleteBinding.UService.setText("Service: " + serviceType);
                    activityUserCompleteBinding.URemarks.setText("Remarks: " + requestDetails);
                    activityUserCompleteBinding.UPrice.setText("Price: " + fee);
                } else {
                    // Handle the case where the request document does not exist
                    Toast.makeText(UserComplete.this, "Data Does Not Exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors while fetching the request document
                Toast.makeText(UserComplete.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        activityUserCompleteBinding.btnUComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogconfirm();

            }
        });

    }

    public void dialogconfirm() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        dialogBuilder.setView(dialogView);

        AlertDialog dialog = dialogBuilder.create();

        Button btnYes = dialogView.findViewById(R.id.UCYes);
        Button btnNo = dialogView.findViewById(R.id.UCNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserComplete.this, "Request Completed", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog

                Intent intent2 = new Intent(getApplicationContext(), UserRequest.class);
                startActivity(intent2);
                finish();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserComplete.this, "In Repair", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        dialog.show();
    }
}