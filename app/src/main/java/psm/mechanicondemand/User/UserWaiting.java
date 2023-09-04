package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.MapFragment2;
import psm.mechanicondemand.Mechanic.RequestProcess;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserWaitingBinding;

public class UserWaiting extends DrawerUser {

    ActivityUserWaitingBinding activityUserWaitingBinding;

    FirebaseFirestore db;

    TextView MName;
    TextView MPhone;
    TextView MExp;
    TextView MID;
    TextView UID;
    TextView Lat;
    Button btnMcall;

    double latitude;
    double longitude;

    String MechID;
    String Name;
    String PhoneNo;
    String Exp;
    String User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserWaitingBinding = activityUserWaitingBinding.inflate(getLayoutInflater());
        setContentView(activityUserWaitingBinding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");

        User = userID;

        MName = findViewById(R.id.MechName);
        MPhone = findViewById(R.id.MechPhone);
        MExp = findViewById(R.id.MechExp);
        MID = findViewById(R.id.MechID);
        UID = findViewById(R.id.UserID);
        btnMcall = findViewById(R.id.btnCallMech);
        Lat = findViewById(R.id.lat);

        UID.setText(userID);

        Fragment fragment = new MapFragment2();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_map4, fragment).commit();

        db.collection("request")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            latitude = documentSnapshot.getDouble("Latitude");
                            longitude = documentSnapshot.getDouble("Longitude");

                            Map<String, Object> requestData = documentSnapshot.getData();
                            if (requestData != null && requestData.containsKey("MechanicID")) {
                                MechID = documentSnapshot.getString("MechanicID");
                                MID.setText(MechID);
                            } else {
                                MID.setText("MechanicID not available");
                            }

                            String LatText = String.valueOf(latitude);
                            String LongText = String.valueOf(longitude);
                            Lat.setText(LatText + "," + LongText);

                            Bundle args = new Bundle();
                            args.putDouble("Latitude", latitude);
                            args.putDouble("Longitude", longitude);
                            fragment.setArguments(args);
                        } else {
                            Toast.makeText(UserWaiting.this, "Error Getting Request Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("RequestProcess", "Error retrieving document: " + e.getMessage());
                    }
                });

        /**db.collection("request")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            latitude = documentSnapshot.getDouble("Latitude");
                            longitude = documentSnapshot.getDouble("Longitude");

                            // Check if the MechanicID field exists and is not null
                            if (documentSnapshot.contains("MechanicID") && documentSnapshot.get("MechanicID") != null) {
                                MechID = documentSnapshot.getString("MechanicID");
                                MID.setText(MechID);
                            } else {
                                // MechanicID field is missing or null
                                MID.setText("MechanicID not available");
                            }

                            String LatText = String.valueOf(latitude);
                            String LongText = String.valueOf(longitude);
                            Lat.setText(LatText + "," + LongText);

                            // Create a Bundle to store the Latitude and Longitude values
                            Bundle args = new Bundle();
                            args.putDouble("Latitude", latitude);
                            args.putDouble("Longitude", longitude);

                            // Set the arguments on the fragment
                            fragment.setArguments(args);
                        } else {
                            Toast.makeText(UserWaiting.this, "Error Getting Request Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while fetching the document
                        Log.e("RequestProcess", "Error retrieving document: " + e.getMessage());
                    }
                });**/

    }
}