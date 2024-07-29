package com.example.coursediary;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The "My
 * Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link permission#ACCESS_FINE_LOCATION} and {@link
 * permission#ACCESS_COARSE_LOCATION} are requested at run time. If either
 * permission is not granted, the Activity is finished with an error message.
 */
// [START maps_android_sample_my_location]
public class MapActivity extends AppCompatActivity
        implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in {@link
     * #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    private GoogleMap map;
    Button btnLoc, btnList;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        editText = findViewById(R.id.editText);
        btnLoc = findViewById(R.id.button3);
        btnList = findViewById(R.id.button4);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() > 0) {
                    Location location = getLocationFromAddress(getApplicationContext(), editText.getText().toString());

                    showCurrentLocation(location);
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    Location pipe, hangang, fruit, pearl, sung, hanbat, ikseon, nature, cheong;
    LatLng p, h, f, pe, su, ha, ik, na, ch;
    Polyline polyline1, polyline2, polyline3;
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();

        // [polyline 1 : 성수]
        pipe = getLocationFromAddress(getApplicationContext(), "메종 파이프그라운드");
        hangang = getLocationFromAddress(getApplicationContext(), "뚝섬 한강공원");
        fruit = getLocationFromAddress(getApplicationContext(), "Rafre fruit");
        polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        p = new LatLng(pipe.getLatitude(), pipe.getLongitude()),
                        f = new LatLng(fruit.getLatitude(), fruit.getLongitude()),
                        h = new LatLng(hangang.getLatitude(), hangang.getLongitude()) ));
        googleMap.addMarker(new MarkerOptions()
                .position(p)
                .title("Maison Pipe Ground"));
        googleMap.addMarker(new MarkerOptions()
                .position(h)
                .title("Ttukseom Hangang Park"));
        googleMap.addMarker(new MarkerOptions()
                .position(f)
                .title("Rafre fruit"));

        // [polyline 2 : 대전]
        pearl = getLocationFromAddress(getApplicationContext(), "대전 Pearl House");
        sung = getLocationFromAddress(getApplicationContext(), "대전 성심당");
        hanbat = getLocationFromAddress(getApplicationContext(), "Hanbat Arboretum");
        polyline2 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        pe = new LatLng(pearl.getLatitude(), pearl.getLongitude()),
                        su = new LatLng(sung.getLatitude(), sung.getLongitude()),
                        ha = new LatLng(hanbat.getLatitude(), hanbat.getLongitude()) ));
        googleMap.addMarker(new MarkerOptions()
                .position(pe)
                .title("Pearl House"));
        googleMap.addMarker(new MarkerOptions()
                .position(su)
                .title("Sungsimdang"));
        googleMap.addMarker(new MarkerOptions()
                .position(ha)
                .title("Hanbat Arboretum"));

        // [polyline 3 : 익선동]
        ikseon = getLocationFromAddress(getApplicationContext(), "익선취향");
        nature = getLocationFromAddress(getApplicationContext(), "익선동 자연도 소금빵");
        cheong = getLocationFromAddress(getApplicationContext(), "Cheonggyecheon");
        polyline3 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        ik = new LatLng(ikseon.getLatitude(), ikseon.getLongitude()),
                        na = new LatLng(nature.getLatitude(), nature.getLongitude()),
                        ch = new LatLng(cheong.getLatitude(), cheong.getLongitude()) ));
        googleMap.addMarker(new MarkerOptions()
                .position(ik)
                .title("Ikseonhyang"));
        googleMap.addMarker(new MarkerOptions()
                .position(na)
                .title("Nature Salt Bread"));
        googleMap.addMarker(new MarkerOptions()
                .position(ch)
                .title("Cheonggyecheon"));
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        // 2. Otherwise, request location permissions from the user.
        PermissionUtils.requestLocationPermissions(this, LOCATION_PERMISSION_REQUEST_CODE, true);
        // [END maps_check_location_permission]
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                permission.ACCESS_FINE_LOCATION) || PermissionUtils
                .isPermissionGranted(permissions, grantResults,
                        permission.ACCESS_COARSE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");
        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if((addresses == null) || (addresses.size() == 0)) {
                return null;
            }
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;
    }

    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitutde : " + curPoint.latitude
                + "\nLongitude : " + curPoint.longitude;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        //화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));


        map.addMarker(new MarkerOptions()
                .position(curPoint)
                .title("Marker in Search location"));
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}
// [END maps_android_sample_my_location]