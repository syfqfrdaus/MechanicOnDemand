package psm.mechanicondemand;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MapFragment2 extends Fragment {

    String completeAddress;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    private OnLocationChangeListener onLocationChangeListener;

    private Timer timer;
    private TimerTask timerTask;

    LatLng latLng;

    public interface OnLocationChangeListener {
        void onLocationChange(Location location, String address);

        //void onLocationChange(Location location);
    }

    public void setOnLocationChangeListener(OnLocationChangeListener listener) {
        onLocationChangeListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map2, container, false);


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP2);
        fusedLocationProviderClient = (FusedLocationProviderClient) LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());

        Dexter.withContext(getActivity().getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // Start the timer to refresh the marker every 10 seconds
                        timer = new Timer();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                // Refresh the marker
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getMechanicLocation();
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, 0, 10000);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // Handle permission denied
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        return view;
    }

    public void getMechanicLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Handle location permission not granted
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {

                        refreshMarker(googleMap, location);

                    }
                });
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void refreshMarker(GoogleMap googleMap, Location location) {
        if (location != null && googleMap != null) {

            // Clear the map before adding the new marker
            googleMap.clear();

            // Add the new marker
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location :"+latLng.latitude + " , " + latLng.longitude);
            googleMap.addMarker(markerOptions);

            // Retrieve the arguments from RequestProcess into fragment
            Bundle args = getArguments();
            LatLng mechanicLatLng = null;
            if (args != null) {
                double latitude = args.getDouble("Latitude");
                double longitude = args.getDouble("Longitude");

                // Use the latitude and longitude values as needed
                mechanicLatLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions2 = new MarkerOptions().position(mechanicLatLng).title("Request Location"+latitude + "," + longitude);
                googleMap.addMarker(markerOptions2);
            }

            // Create a LatLngBounds object to include both markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(latLng);
            builder.include(mechanicLatLng);
            LatLngBounds bounds = builder.build();

            // Set padding to ensure both markers are visible on the map
            int padding = 100;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            // Move and animate the camera to show both markers
            googleMap.moveCamera(cameraUpdate);

        }
    }
}