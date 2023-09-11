package psm.mechanicondemand.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import psm.mechanicondemand.Mechanic.MechanicRequest;
import psm.mechanicondemand.R;

public class MechanicRegister extends AppCompatActivity {

    EditText MechanicName;
    EditText MechanicPhoneNo;
    EditText MechanicEmail;
    EditText MechanicPassword;
    EditText MechYrsExp;
    Button btnRegMechanic;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_register);

        mAuth = FirebaseAuth.getInstance();

        MechanicName = findViewById(R.id.RegMechName);
        MechanicPhoneNo = findViewById(R.id.RegMechPhoneNo);
        MechanicEmail = findViewById(R.id.RegMechEmail);
        MechanicPassword = findViewById(R.id.RegMechPassword);
        MechYrsExp = findViewById(R.id.MechYrsExp);
        btnRegMechanic = findViewById(R.id.btnRegMechanic);

        btnRegMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, phoneNo, email, password, yearExp;
                name = MechanicName.getText().toString();
                phoneNo = MechanicPhoneNo.getText().toString();
                email = MechanicEmail.getText().toString();
                password = MechanicPassword.getText().toString();
                yearExp = MechYrsExp.getText().toString();

                if (name.isEmpty() || phoneNo.isEmpty() || email.isEmpty() || password.isEmpty() || yearExp.isEmpty()) {
                    Toast.makeText(MechanicRegister.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MechanicRegister.this, "Account Created", Toast.LENGTH_SHORT).show();

                                        // Get the user ID of the newly created user
                                        String userId = mAuth.getCurrentUser().getUid();

                                        // Create a new document reference with the user ID
                                        DocumentReference userRef = db.collection("mechanics").document(userId);

                                        // Create a user object with the desired details
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Name", name);
                                        user.put("PhoneNo", phoneNo);
                                        user.put("Email", email);
                                        user.put("YearExp", yearExp);

                                        // Set the user object in the Firestore document
                                        userRef.set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MechanicRegister.this, "User details added to Firestore", Toast.LENGTH_SHORT).show();
                                                        // You can perform additional actions here if needed
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(MechanicRegister.this, "Failed to add user details to Firestore", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        Intent intent = new Intent(getApplicationContext(), MechanicRequest.class);
                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MechanicRegister.this, "Registration failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                
            }
        });

    }
}