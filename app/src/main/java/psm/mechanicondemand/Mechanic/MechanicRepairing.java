package psm.mechanicondemand.Mechanic;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityMechanicRepairingBinding;

public class MechanicRepairing extends DrawerMechanic {

    ActivityMechanicRepairingBinding activityMechanicRepairingBinding;

    FirebaseFirestore db;

    TextView RepAddress;
    TextView RepVName;
    TextView RepService;
    TextView RepRemarks;
    TextView RepRName;
    TextView RepRPhone;
    TextView RepVPlate;
    TextView RepPrice;

    Button BtnAdjFee;
    Button BtnRepComplete;

    String Address;
    String VehicleName;
    String Service;
    double Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicRepairingBinding = activityMechanicRepairingBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicRepairingBinding.getRoot());

        db = FirebaseFirestore.getInstance();

        RepAddress = findViewById(R.id.RepAddress);//Done
        RepVName = findViewById(R.id.RepVName);//Done
        RepService = findViewById(R.id.RepService);//Done
        RepRemarks = findViewById(R.id.RepRemarks);//Done
        RepRName = findViewById(R.id.RepRName);//Done
        RepRPhone = findViewById(R.id.RepRPhone);//Done
        RepVPlate = findViewById(R.id.RepVPlate);//Done
        RepPrice = findViewById(R.id.RepPrice);//Done
        BtnAdjFee = findViewById(R.id.btnAdjustFee);//Done
        BtnRepComplete = findViewById(R.id.btnRepComplete);//NEED TO COMPLETE

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");
        String userName = intent.getStringExtra("UserName");
        String userPhone = intent.getStringExtra("UserPhone");
        String Plate = intent.getStringExtra("VPlate");

        RepRPhone.setText("Phone No : "+userPhone);
        RepVPlate.setText("Vehicle Plate : "+Plate);
        RepRName.setText("Name : "+userName);



        //Fetch data of Latitude and Longitude and address
        db.collection("request")
                .document(userID) // Replace UID with the appropriate document ID
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Address = documentSnapshot.getString("Address");
                            VehicleName = documentSnapshot.getString("VehicleModel");
                            Service = documentSnapshot.getString("ServiceType");
                            String Details = documentSnapshot.getString("RequestDetails");
                            Price = documentSnapshot.getDouble("Fee");

                            String priceText = String.valueOf(Price);

                            RepAddress.setText(Address);
                            RepVName.setText("Vehicle : "+VehicleName);
                            RepService.setText("Service : "+Service);
                            RepRemarks.setText("Remarks : "+Details);
                            RepPrice.setText("Fee : "+priceText);

                        } else {
                            Toast.makeText(MechanicRepairing.this, "Error Getting Request Location", Toast.LENGTH_SHORT).show();
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

        BtnAdjFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adjustNewFee(userID);
            }
        });

        BtnRepComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(), RequestComplete.class);
                intent3.putExtra("UserID",userID);
                intent3.putExtra("userName",userName);
                intent3.putExtra("VehicleName",VehicleName);
                intent3.putExtra("Address",Address);
                intent3.putExtra(" Service",Service);
                intent3.putExtra("Price",Price);
                startActivity(intent3);
                finish();
            }
        });

    }

    public void adjustNewFee(String userID){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_adjfee, null);
        dialogBuilder.setView(dialogView);

        AlertDialog dialog = dialogBuilder.create(); // Declare the dialog as a final variable

        Button btnFinalFee = dialogView.findViewById(R.id.btnFinalFee);
        EditText finalfee = dialogView.findViewById(R.id.finalFee);

        // Set click listeners for the buttons in the dialog
        btnFinalFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feeInput = finalfee.getText().toString();
                if (!feeInput.isEmpty()) {
                    int enteredFee = Integer.parseInt(feeInput);

                    db.collection("request")
                            .document(userID)
                            .update("Fee", enteredFee)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MechanicRepairing.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss(); // Dismiss the dialog

                                    Intent intent2 = new Intent(getApplicationContext(), MechanicRepairing.class);
                                    intent2.putExtra("UserID",userID);
                                    startActivity(intent2);
                                    finish();
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
                    Toast.makeText(MechanicRepairing.this, "Please enter a fee value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}