package psm.mechanicondemand.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.History;
import psm.mechanicondemand.HistoryAdapter;
import psm.mechanicondemand.Mechanic.MechanicHistory;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserHistoryBinding;
import psm.mechanicondemand.databinding.ActivityUserRequestBinding;

public class UserHistory extends DrawerUser {

    ActivityUserHistoryBinding activityUserHistoryBinding;

    RecyclerView recyclerView;
    ArrayList<History> historyArrayList;
    HistoryAdapter historyAdapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserHistoryBinding = activityUserHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityUserHistoryBinding.getRoot());

        recyclerView = findViewById(R.id.recycleUHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        historyArrayList = new ArrayList<History>();
        historyAdapter = new HistoryAdapter(UserHistory.this,historyArrayList);

        recyclerView.setAdapter(historyAdapter);

        // Fetch data based on MechanicID
        String userID = mAuth.getCurrentUser().getUid();
        fetchHistoryData(userID);

    }

    private void fetchHistoryData(String userID) {

        db.collection("history")
                .whereEqualTo("UserID", userID)
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