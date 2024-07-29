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
import com.example.coursediary.databinding.ActivityIkseonBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class IkseonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location ikseon, nature, cheong;
    LatLng ik, na, ch;
    Polyline polyline3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ikseon);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // [polyline 3 : 익선동]
        ikseon = getLocationFromAddress(getApplicationContext(), "익선취향");
        nature = getLocationFromAddress(getApplicationContext(), "소하염전");
        cheong = getLocationFromAddress(getApplicationContext(), "Cheonggyecheon");
        polyline3 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        ik = new LatLng(ikseon.getLatitude(), ikseon.getLongitude()),
                        na = new LatLng(nature.getLatitude(), nature.getLongitude()),
                        ch = new LatLng(cheong.getLatitude(), cheong.getLongitude()) ));
        googleMap.addMarker(new MarkerOptions()
                .position(ik)
                .title("익선취향"));
        googleMap.addMarker(new MarkerOptions()
                .position(na)
                .title("소하염전"));
        googleMap.addMarker(new MarkerOptions()
                .position(ch)
                .title("청계천"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(na, 15));
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