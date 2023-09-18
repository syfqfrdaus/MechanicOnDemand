package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.Login.UserRegister2;
import psm.mechanicondemand.MapFragment2;
import psm.mechanicondemand.Mechanic.RequestComplete;
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
    Button btnMArrived;

    double latitude;
    double longitude;

    String MechID;
    String MechName;
    String MechPhone;
    String MechExp;
    String User;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserWaitingBinding = activityUserWaitingBinding.inflate(getLayoutInflater());
        setContentView(activityUserWaitingBinding.getRoot());

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String userID = intent.getStringExtra("UserID");

        MName = findViewById(R.id.MechName);
        MPhone = findViewById(R.id.MechPhone);
        MExp = findViewById(R.id.MechExp);
        MID = findViewById(R.id.MechID);
        UID = findViewById(R.id.UserID);
        btnMcall = findViewById(R.id.btnCallMech);
        btnMArrived = findViewById(R.id.btnMechArrived);
        Lat = findViewById(R.id.lat);

        UID.setText(userID);

        //Display the position of mechanic and user in map and get mech id
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
                        Toast.makeText(UserWaiting.this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Fetch the data for the mechanic
        // Reference to the mechanics collection
        CollectionReference mechanicsCollectionRef = db.collection("mechanics");

        // Query the mechanics collection to get the mechanic document with the matching MechanicID
        Query query = mechanicsCollectionRef.whereEqualTo("MechanicID", MechID);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // If there is a matching document, it should be unique based on the MechanicID
                            DocumentSnapshot mechanicDocument = queryDocumentSnapshots.getDocuments().get(0);

                            // Get the mechanic's data
                            MechName = mechanicDocument.getString("Name");
                            MechPhone = mechanicDocument.getString("PhoneNo");
                            MechExp = mechanicDocument.getString("YearExp");

                            //Display data
                            MName.setText("Mechanic Name : "+ MechName);
                            MPhone.setText("Phone Number: " + MechPhone);
                            MExp.setText("Year of Experience: " + MechExp);
                        } else {
                            // No matching document found
                            // Handle the case where no mechanic with the given MechanicID exists
                            Toast.makeText(UserWaiting.this, "No mechanic found with the provided ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(UserWaiting.this, "Error fetching mechanic data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        btnMcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an intent with the ACTION_DIAL action and the phone number
                Intent intent3 = new Intent(Intent.ACTION_DIAL);
                intent3.setData(Uri.parse("tel:" + MechPhone));

                // Start the dialer activity
                startActivity(intent3);

            }
        });


        btnMArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to another activity here
                Intent intent2 = new Intent(getApplicationContext(), UserComplete.class);
                intent2.putExtra("UserID",userID);
                intent2.putExtra("MechID",MechID);
                intent2.putExtra("MechName",MechName);
                intent2.putExtra("MechPhone",MechPhone);
                startActivity(intent2);
                finish();
            }
        });

    }
}