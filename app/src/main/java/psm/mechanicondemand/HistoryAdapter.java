package psm.mechanicondemand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<History> historyArrayList;

    public HistoryAdapter(Context context, ArrayList<History> historyArrayList) {
        this.context = context;
        this.historyArrayList = historyArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recycle_history,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

        History history = historyArrayList.get(position);

        holder.Date.setText(history.Date);
        holder.Time.setText("Time: " + history.Time);
        holder.Location.setText("Address: " + history.Address);
        holder.UName.setText("User ID: " + history.UserID);
        holder.MName.setText("Mechanic ID: " + history.MechanicID);
        holder.Vehicle.setText("Vehicle Model: " + history.VehicleName);
        holder.Service.setText("Service: " + history.Service);
        //holder.Fee.setText(Double.toString(history.Price));
        holder.Fee.setText("Fee: " + Double.toString(history.Price));

    }

    @Override
    public int getItemCount() {

        return historyArrayList.size();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Date, Time, Location, UName, MName, Vehicle, Service, Fee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.HistDate);
            Time = itemView.findViewById(R.id.HistTime);
            Location = itemView.findViewById(R.id.HistLocation);
            UName = itemView.findViewById(R.id.HistUName);
            MName = itemView.findViewById(R.id.HistMName);
            Vehicle = itemView.findViewById(R.id.HistVehicle);
            Service = itemView.findViewById(R.id.HistService);
            Fee = itemView.findViewById(R.id.HistFee);

        }
    }

}
