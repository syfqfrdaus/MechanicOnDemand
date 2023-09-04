package psm.mechanicondemand.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import psm.mechanicondemand.MainActivity;
import psm.mechanicondemand.R;

public class StartPage extends AppCompatActivity {

    Button user;
    Button mechanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        user = findViewById(R.id.btnstartUser);
        mechanic = findViewById(R.id.btnstartMech);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                startActivity(intent);

            }
        });

        mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MechanicLogin.class);
                startActivity(intent);


            }
        });
    }
}