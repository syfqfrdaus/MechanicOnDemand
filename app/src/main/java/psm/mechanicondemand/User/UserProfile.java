package psm.mechanicondemand.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import psm.mechanicondemand.DrawerUser;
import psm.mechanicondemand.R;
import psm.mechanicondemand.databinding.ActivityDrawerUserBinding;
import psm.mechanicondemand.databinding.ActivityUserProfileBinding;

public class UserProfile extends DrawerUser {

    ActivityUserProfileBinding activityUserProfileBinding;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore firestore;

    Button btnUpdate;
    TextView Fullname;
    TextView phoneno;
    TextView Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = activityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Fullname = findViewById(R.id.userFullname);
        phoneno = findViewById(R.id.userPhoneNo);
        Email = findViewById(R.id.userEmail);
        //btnUpdate = findViewById(R.id.btnUpdateUserProfile);

        fetchUserDetails();

    }

    private void fetchUserDetails() {
        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("users").document(userId)
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

                                Fullname.setText("Fullname: " + fullname);
                                phoneno.setText("Phone No: " + phoneNo);
                                Email.setText("Email: " + email);
                            }
                        }else {
                            Toast.makeText(UserProfile.this, "Error in Fetching Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}