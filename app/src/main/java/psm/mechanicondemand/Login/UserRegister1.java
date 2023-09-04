package psm.mechanicondemand.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import psm.mechanicondemand.R;

public class UserRegister1 extends AppCompatActivity {

    EditText uFullname;
    EditText uPhoneNo;
    EditText uEmail;
    EditText uPassword;
    Button uRegister;

    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register1);

        mAuth = FirebaseAuth.getInstance();

        uFullname = findViewById(R.id.RegUserName);
        uPhoneNo = findViewById(R.id.RegUserPhoneNo);
        uEmail = findViewById(R.id.RegUserEmail);
        uPassword = findViewById(R.id.RegUserPassword);
        uRegister = findViewById(R.id.btnRegUser);

        uRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName, uPhone, Useremail, uPass;
                uName = uFullname.getText().toString();
                uPhone = uPhoneNo.getText().toString();
                Useremail = uEmail.getText().toString();
                uPass = uPassword.getText().toString();

                if (TextUtils.isEmpty(Useremail)){
                    Toast.makeText(UserRegister1.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(uPass)){
                    Toast.makeText(UserRegister1.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(Useremail, uPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(UserRegister1.this, "Account Created", Toast.LENGTH_SHORT).show();

                                    // Get the user ID of the newly created user
                                    String userId = mAuth.getCurrentUser().getUid();

                                    // Create a new document reference with the user ID
                                    DocumentReference userRef = db.collection("users").document(userId);

                                    // Create a user object with the desired details
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Name", uName);
                                    user.put("PhoneNo", uPhone);
                                    user.put("Email", Useremail);

                                    // Set the user object in the Firestore document
                                    userRef.set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(UserRegister1.this, "User details added to Firestore", Toast.LENGTH_SHORT).show();
                                                    // You can perform additional actions here if needed
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(UserRegister1.this, "Failed to add user details to Firestore", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(getApplicationContext(), UserRegister2.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(UserRegister1.this, "Registration failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}