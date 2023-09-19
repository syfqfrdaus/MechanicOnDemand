package psm.mechanicondemand;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import psm.mechanicondemand.Mechanic.RequestDetails;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    Context context;
    ArrayList<Request> requestArrayList;

    public RequestAdapter(Context context, ArrayList<Request> requestArrayList) {
        this.context = context;
        this.requestArrayList = requestArrayList;
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recycle_request, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.MyViewHolder holder, int position) {

        Request request = requestArrayList.get(holder.getAdapterPosition());

        holder.Address.setText(request.Address);
        holder.Vehicle.setText(request.VehicleModel);
        holder.Service.setText(request.ServiceType);
        holder.Rid.setText(request.getDocumentId());

        int fee = request.Fee;
        holder.Price.setText(String.valueOf(fee));

        // Calculate the distance in kilometers with one decimal point
        double distanceInKilometers = request.getDistance() / 1000.0;
        String formattedDistance = String.format("%.2f km", distanceInKilometers);

        holder.Distance.setText(formattedDistance); // Set the formatted distance value

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Request selectedRequest = requestArrayList.get(clickedPosition);
                    // Open RequestDetails activity and pass the selected request
                    Intent intent = new Intent(context, RequestDetails.class);
                    intent.putExtra("request", selectedRequest);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Address, Vehicle, Service, Price, Distance, Rid, vDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.AreaName);
            Vehicle = itemView.findViewById(R.id.RVehicle);
            Service = itemView.findViewById(R.id.RService);
            Price = itemView.findViewById(R.id.Rprice);
            Distance = itemView.findViewById(R.id.distance);
            Rid = itemView.findViewById(R.id.RequestID);
            vDetails = itemView.findViewById(R.id.viewDetails);

        }
    }
}
