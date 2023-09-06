package psm.mechanicondemand.Mechanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.History;
import psm.mechanicondemand.HistoryAdapter;
import psm.mechanicondemand.MainActivity;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityMechanicHistoryBinding;

public class MechanicHistory extends DrawerMechanic {

    ActivityMechanicHistoryBinding activityMechanicHistoryBinding;

    RecyclerView recyclerView;
    ArrayList<History> historyArrayList;
    HistoryAdapter historyAdapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicHistoryBinding = activityMechanicHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicHistoryBinding.getRoot());

        recyclerView = findViewById(R.id.recycleMHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        historyArrayList = new ArrayList<History>();
        historyAdapter = new HistoryAdapter(MechanicHistory.this,historyArrayList);

        recyclerView.setAdapter(historyAdapter);

        // Fetch data based on MechanicID
        String mechanicID = mAuth.getCurrentUser().getUid();
        fetchHistoryDataForMechanic(mechanicID);

    }

    private void fetchHistoryDataForMechanic(String mechanicID) {

        db.collection("history")
                .whereEqualTo("MechanicID", mechanicID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convert Firestore document to a History object
                        History history = document.toObject(History.class);
                        historyArrayList.add(history);
                    }

                    // Notify the adapter that data has changed
                    historyAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle any errors here
                    Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}