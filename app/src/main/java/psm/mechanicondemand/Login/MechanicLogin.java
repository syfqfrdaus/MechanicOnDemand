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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import psm.mechanicondemand.R;
import psm.mechanicondemand.Mechanic.MechanicRequest;

public class MechanicLogin extends AppCompatActivity {

    EditText mechanicEmail;
    EditText mechanicPassword;
    Button btnMechLogin;
    Button btnMechRegister;

    FirebaseAuth mAuth;

    int loginAttempts = 0; // Initialize the login attempts counter

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MechanicRequest.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_login);

        mAuth = FirebaseAuth.getInstance();

        mechanicEmail = findViewById(R.id.mechanicEmail);
        mechanicPassword = findViewById(R.id.mechanicPassword);
        btnMechLogin = findViewById(R.id.btnMechanicLogin);
        btnMechRegister = findViewById(R.id.btnMechanicRegister);

        btnMechRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MechanicRegister.class);
                startActivity(intent);
            }
        });

        btnMechLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = mechanicEmail.getText().toString();
                password = mechanicPassword.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MechanicLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(MechanicLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Reset the login attempts counter on successful login
                                    loginAttempts = 0;

                                    Toast.makeText(MechanicLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MechanicRequest.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MechanicLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    // Increment the login attempts counter
                                    loginAttempts++;

                                    // Check if login attempts exceed the limit (e.g., 3)
                                    if (loginAttempts >= 4) {
                                        Toast.makeText(MechanicLogin.this, "Login attempts exceeded. Closing app.", Toast.LENGTH_SHORT).show();
                                        finishAffinity(); // Close the app
                                    }

                                }
                            }
                        });
            }
        });

    }
}