package psm.mechanicondemand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    Context context;
    ArrayList<Vehicle> vehicleArrayList;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public VehicleAdapter(Context context, ArrayList<Vehicle> vehicleArrayList) {
        this.context = context;
        this.vehicleArrayList = vehicleArrayList;
    }

    @NonNull
    @Override
    public VehicleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recycle_vehicle_list, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.MyViewHolder holder, int position) {

        Vehicle vehicle = vehicleArrayList.get(position);

        holder.Type.setText(vehicle.VehicleType);
        holder.Brand.setText(vehicle.VehicleBrand);
        holder.Model.setText(vehicle.VehicleModel);
        holder.Plate.setText(vehicle.VehiclePlate);

        // Set an OnClickListener for the "Delete" button
        final int deletePosition = position; // Store the position as a final variable

        // Set an OnClickListener for the "Delete" button
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to delete the vehicle based on position
                deleteVehicle(deletePosition);
            }
        });

    }

    @Override
    public int getItemCount() {

        return vehicleArrayList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Type, Brand, Model, Plate;
        Button Update, Delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Type = itemView.findViewById(R.id.type);
            Brand = itemView.findViewById(R.id.brand);
            Model = itemView.findViewById(R.id.model);
            Plate = itemView.findViewById(R.id.plate);
            //Update = itemView.findViewById(R.id.btnUpdateVehi);
            Delete = itemView.findViewById(R.id.btnDeleteVehi);

        }
    }

    private void deleteVehicle(int position) {
        String userId = mAuth.getCurrentUser().getUid();

        // Reference to the "vehicles" collection
        CollectionReference vehiclesCollection = db.collection("vehicles");

        // Reference to the specific document (based on numerical index) within the "VehiCount" subcollection
        DocumentReference documentReference = vehiclesCollection
                .document(userId) // Assuming userId is the current user's ID
                .collection("VehiCount")
                .document(String.valueOf(position + 1)); // Convert position to the corresponding index

        documentReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully deleted
                        Toast.makeText(context, "Vehicle deleted", Toast.LENGTH_SHORT).show();

                        // Remove the item from the ArrayList
                        vehicleArrayList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                        Toast.makeText(context, "Error deleting vehicle: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
