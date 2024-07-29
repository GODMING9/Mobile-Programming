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
import com.example.coursediary.databinding.ActivityDaejeonBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class DaejeonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location pearl, sung, hanbat;
    LatLng pe, su, ha;
    Polyline polyline2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daejeon);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // [polyline 2 : 대전]
        pearl = getLocationFromAddress(getApplicationContext(), "대전 Pearl House");
        sung = getLocationFromAddress(getApplicationContext(), "성심당 본점");
        hanbat = getLocationFromAddress(getApplicationContext(), "Hanbat Arboretum");
        polyline2 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        pe = new LatLng(pearl.getLatitude(), pearl.getLongitude()),
                        su = new LatLng(sung.getLatitude(), sung.getLongitude()),
                        ha = new LatLng(hanbat.getLatitude(), hanbat.getLongitude()) ));
        googleMap.addMarker(new MarkerOptions()
                .position(pe)
                .title("대전 Pearl House"));
        googleMap.addMarker(new MarkerOptions()
                .position(su)
                .title("성심당 본점"));
        googleMap.addMarker(new MarkerOptions()
                .position(ha)
                .title("한밭수목원"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pe, 13));
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