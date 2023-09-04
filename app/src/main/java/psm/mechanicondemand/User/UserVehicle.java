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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        vehicleArrayList = new ArrayList<Vehicle>();
        vehicleAdapter = new VehicleAdapter(UserVehicle.this,vehicleArrayList);

        recyclerView.setAdapter(vehicleAdapter);


        FillRecyclerView();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister2.class);
                startActivity(intent);
            }
        });
    }

    private void FillRecyclerView() {

        String userId = mAuth.getCurrentUser().getUid();

        /**db.collection("vehicles")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                vehicleArrayList.add(dc.getDocument().toObject(Vehicle.class));
                            }
                            vehicleAdapter.notifyDataSetChanged();
                        }

                    }
                });**/

        db.collection("vehicles")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Vehicle vehicle = document.toObject(Vehicle.class);
                                // Use the vehicle object as needed

                                // Add the vehicle to your vehicleArrayList
                                vehicleArrayList.add(vehicle);
                                vehicleAdapter.notifyDataSetChanged();
                            } else {
                                Log.d("Firestore", "No such document");
                            }
                        } else {
                            Log.e("Firestore Error", task.getException().getMessage());
                        }
                    }
                });

    }
}