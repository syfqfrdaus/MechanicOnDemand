package psm.mechanicondemand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    Context context;
    ArrayList<Vehicle> vehicleArrayList;

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
            Update = itemView.findViewById(R.id.btnUpdateVehi);
            Delete = itemView.findViewById(R.id.btnDeleteVehi);

        }
    }
}
