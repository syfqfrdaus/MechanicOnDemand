package psm.mechanicondemand.Mechanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.MapFragment;
import psm.mechanicondemand.R;
import psm.mechanicondemand.Request;
import psm.mechanicondemand.RequestAdapter;
import psm.mechanicondemand.User.UserRequest;
import psm.mechanicondemand.User.UserVehicle;
import psm.mechanicondemand.Vehicle;
import psm.mechanicondemand.VehicleAdapter;
import psm.mechanicondemand.databinding.ActivityMechanicRequestBinding;

public class MechanicRequest extends DrawerMechanic {

    ActivityMechanicRequestBinding activityMechanicRequestBinding;

    RecyclerView recycleRequest;
    ArrayList<Request> requestArrayList;
    RequestAdapter requestAdapter;
    FirebaseFirestore db;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    double mlatitude = 0;
    double mlongitude = 0;
    double requestLatitude = 0;
    double requestLongitude = 0;
    double distance = 0;

    EditText mechanicLocation;
    TextView viewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicRequestBinding = activityMechanicRequestBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicRequestBinding.getRoot());

        mechanicLocation = findViewById(R.id.mechanicAddress);
        viewDetail = findViewById(R.id.viewDetails);

        recycleRequest = findViewById(R.id.recycleRequest);
        recycleRequest.setHasFixedSize(true);
        recycleRequest.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        requestArrayList = new ArrayList<Request>();
        requestAdapter = new RequestAdapter(MechanicRequest.this, requestArrayList);

        recycleRequest.setAdapter(requestAdapter);

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_map2, fragment).commit();

        // Get the current location from the MapFragment
        ((MapFragment) fragment).setOnLocationChangeListener(new MapFragment.OnLocationChangeListener() {
            @Override
            public void onLocationChange(Location location, String address) {
                if (location != null) {
                    mlatitude = location.getLatitude();
                    mlongitude = location.getLongitude();

                    String LaLo = "Latitude: " + mlatitude + "\nLongitude: " + mlongitude;
                    mechanicLocation.setText(LaLo + "\n" + address);
                } else {
                    mechanicLocation.setText("Location not available");
                }
            }

        });

        FillRecyclerView();
    }

    public void FillRecyclerView() {
        db.collection("request")
                .whereEqualTo("Status", "Active") // Add a query to filter documents with Status = "Active"
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("Firestore Error", e.getMessage());
                            return;
                        }

                        if (querySnapshot != null) {
                            int iterationCount = 1;
                            requestArrayList.clear(); // Clear the existing data

                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String documentId = document.getId(); // Get the document ID

                                Request request = document.toObject(Request.class);
                                request.setDocumentId(documentId); // Set the document ID in the Request object

                                // Calculate the distance between the current request and the mechanic's location
                                requestLatitude = request.getLatitude();
                                requestLongitude = request.getLongitude();
                                calculateDistance(mlatitude, mlongitude, requestLatitude, requestLongitude);

                                // Set the distance in the request object
                                request.setDistance(distance);

                                //Toast.makeText(MechanicRequest.this, "Distance for iteration " + iterationCount + ": " + distance, Toast.LENGTH_SHORT).show();

                                requestArrayList.add(request);

                                iterationCount++; // Increment the counter variable
                            }

                            requestAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // in kilometers

        double dLat = 0;
        double dLon = 0;

        dLat = Math.toRadians(lat2 - lat1);
        dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        distance = earthRadius * c;
        return distance;
    }

}