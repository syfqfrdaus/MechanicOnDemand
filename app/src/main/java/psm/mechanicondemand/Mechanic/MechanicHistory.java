package psm.mechanicondemand.Mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityMechanicHistoryBinding;

public class MechanicHistory extends DrawerMechanic {

    ActivityMechanicHistoryBinding activityMechanicHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicHistoryBinding = activityMechanicHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicHistoryBinding.getRoot());
    }
}