package psm.mechanicondemand.Mechanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import psm.mechanicondemand.DrawerMechanic;
import psm.mechanicondemand.R;
import psm.mechanicondemand.User.UserProfile;
import psm.mechanicondemand.databinding.ActivityMechanicProfileBinding;

public class MechanicProfile extends DrawerMechanic {

    ActivityMechanicProfileBinding activityMechanicProfileBinding;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    TextView MName;
    TextView MPhone;
    TextView MEmail;
    TextView MExp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMechanicProfileBinding = activityMechanicProfileBinding.inflate(getLayoutInflater());
        setContentView(activityMechanicProfileBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        MName = findViewById(R.id.MekaName);
        MPhone = findViewById(R.id.MekaPhone);
        MEmail = findViewById(R.id.MekaEmail);
        MExp = findViewById(R.id.MekaExp);

        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("mechanics").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null && documentSnapshot.exists()){
                                String fullname = documentSnapshot.getString("Name");
                                String phoneNo = documentSnapshot.getString("PhoneNo");
                                String email = documentSnapshot.getString("Email");
                                String year = documentSnapshot.getString("YearExp");

                                MName.setText("Fullname: " + fullname);
                                MPhone.setText("Phone No: " + phoneNo);
                                MEmail.setText("Email: " + email);
                                MExp.setText("Years of Experience : "+year);
                            }
                        }else {
                            Toast.makeText(MechanicProfile.this, "Error in Fetching Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}