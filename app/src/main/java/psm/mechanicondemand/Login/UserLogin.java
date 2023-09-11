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
import psm.mechanicondemand.User.UserRequest;

public class UserLogin extends AppCompatActivity {

    EditText userID;
    EditText userPass;
    Button btnuLogin;
    Button btnuRegister;

    FirebaseAuth mAuth;

    int loginAttempts = 0; // Initialize the login attempts counter

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), UserRequest.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        mAuth = FirebaseAuth.getInstance();

        userID = findViewById(R.id.userEmail);
        userPass = findViewById(R.id.userPassword);
        btnuLogin = findViewById(R.id.btnUserLogin);
        btnuRegister = findViewById(R.id.btnUserRegister);

        btnuRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister1.class);
                startActivity(intent);
            }
        });

        btnuLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = userID.getText().toString();
                password = userPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(UserLogin.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(UserLogin.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Reset the login attempts counter on successful login
                                    loginAttempts = 0;

                                    Toast.makeText(UserLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), UserRequest.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(UserLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    // Increment the login attempts counter
                                    loginAttempts++;

                                    // Check if login attempts exceed the limit (e.g., 3)
                                    if (loginAttempts >= 4) {
                                        Toast.makeText(UserLogin.this, "Login attempts exceeded. Closing app.", Toast.LENGTH_SHORT).show();
                                        finishAffinity(); // Close the app
                                    }
                                }
                            }
                        });
            }
        });
    }
}