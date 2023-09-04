package psm.mechanicondemand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import psm.mechanicondemand.Login.StartPage;
import psm.mechanicondemand.Mechanic.MechanicHistory;
import psm.mechanicondemand.Mechanic.MechanicProfile;
import psm.mechanicondemand.Mechanic.MechanicRequest;
import psm.mechanicondemand.User.UserRequest;

public class DrawerMechanic extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout2;

    @Override
    public void setContentView(View view) {
        drawerLayout2 = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_mechanic,null);
        FrameLayout container = drawerLayout2.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout2);

        Toolbar toolbar = drawerLayout2.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout2.findViewById(R.id.navigation_view2);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout2,toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout2.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout2.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_mechanicRequest:
                startActivity(new Intent(this, MechanicRequest.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_mechanicProfile:
                startActivity(new Intent(this, MechanicProfile.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_mechanicHistory:
                startActivity(new Intent(this, MechanicHistory.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_mechanicLogout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), StartPage.class);
                startActivity(intent);
                finish();
                break;
        }

        return false;
    }
}