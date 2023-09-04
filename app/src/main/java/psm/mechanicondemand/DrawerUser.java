package psm.mechanicondemand;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import psm.mechanicondemand.Login.StartPage;
import psm.mechanicondemand.User.UserHistory;
import psm.mechanicondemand.User.UserProfile;
import psm.mechanicondemand.User.UserRequest;
import psm.mechanicondemand.User.UserTroubleshoot;
import psm.mechanicondemand.User.UserVehicle;

public class DrawerUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_user,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.nav_userRequest:
                startActivity(new Intent(this, UserRequest.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_userProfile:
                Intent intent2 = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent2);
                break;

            case R.id.nav_userVehicle:
                startActivity(new Intent(this, UserVehicle.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_userTroubleshoot:
                Intent intent3 = new Intent(getApplicationContext(), UserTroubleshoot.class);
                startActivity(intent3);
                break;

            case R.id.nav_userHistory:
                startActivity(new Intent(this, UserHistory.class));
                overridePendingTransition(0, 0);
                break;

            case R.id.nav_userLogout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), StartPage.class);
                startActivity(intent);
                finish();
                break;
        }

        return false;
    }
}