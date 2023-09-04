package psm.mechanicondemand.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityUserRequestBinding;

public class UserHistory extends DrawerUser {

    ActivityUserRequestBinding activityUserRequestBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserRequestBinding = activityUserRequestBinding.inflate(getLayoutInflater());
        setContentView(activityUserRequestBinding.getRoot());
    }
}