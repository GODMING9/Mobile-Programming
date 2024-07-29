package com.example.coursediary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.coursediary.databinding.ActivitySeongsuBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class SeongsuActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location pipe, hangang, fruit;
    LatLng p, h, f;
    Polyline polyline1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seongsu);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
                .title("뚝섬 한강공원"));
        googleMap.addMarker(new MarkerOptions()
                .position(f)
                .title("Rafre fruit"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(f, 13));
    }

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
}