package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.Login.UserLogin;
import psm.mechanicondemand.Login.UserRegister2;
import psm.mechanicondemand.R;
import psm.mechanicondemand.Vehicle;
import psm.mechanicondemand.VehicleAdapter;
import psm.mechanicondemand.databinding.ActivityUserVehicleBinding;

public class UserVehicle extends DrawerUser {

    ActivityUserVehicleBinding activityUserVehicleBinding;

    RecyclerView recyclerView;
    ArrayList<Vehicle> vehicleArrayList;
    VehicleAdapter vehicleAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserVehicleBinding = activityUserVehicleBinding.inflate(getLayoutInflater());
        setContentView(activityUserVehicleBinding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        add = findViewById(R.id.addVehicle);

        recyclerView = findViewById(R.id.recycleVehicle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        vehicleArrayList = new ArrayList<Vehicle>();
        vehicleAdapter = new VehicleAdapter(UserVehicle.this,vehicleArrayList);

        recyclerView.setAdapter(vehicleAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister2.class);
                startActivity(intent);
            }
        });

        String userId = mAuth.getCurrentUser().getUid();

        // Create a reference to the "vehicles" collection
        CollectionReference vehiclesCollection = db.collection("vehicles");

        // Create a reference to the specific user's document
        DocumentReference userRef = vehiclesCollection.document(userId);

        // Create a reference to the "VehiCount" subcollection under the user's document
        CollectionReference veCountCollection = userRef.collection("VehiCount");

        // Query all documents in the "VehiCount" subcollection
        veCountCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // Loop through the documents in the "VehiCount" subcollection
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert the document data to a Vehicle object
                        Vehicle vehicle = document.toObject(Vehicle.class);
                        vehicleArrayList.add(vehicle);
                    }
                    // Notify the adapter that data has changed
                    vehicleAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                    Toast.makeText(this, "Failed to fetch vehicle data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}