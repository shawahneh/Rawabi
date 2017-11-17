package com.techcamp.aauj.rawabi.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techcamp.aauj.rawabi.Beans.Journey;
import com.techcamp.aauj.rawabi.Beans.User;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.fragments.DriverFragments.DriverDetailDialogFragment;
import com.techcamp.aauj.rawabi.fragments.UserTypeFragment;

import java.util.Date;

public class CarpoolActivity extends AppCompatActivity  implements OnMapReadyCallback,
        UserTypeFragment.IUserTypeFragmenetListener{

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpool);

// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(savedInstanceState == null){
            setFragment(new UserTypeFragment(),"tag");
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
    }
    private void setFragment(android.support.v4.app.Fragment fragment, String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,fragment,tag)
                .commit();
    }

    @Override
    public void onTypeClick(int type) {
        if(type == 0){
            Journey journey = new Journey();
            User user = new User();
            user.setFullname("ALa");
            user.setPhone("059");

            journey.setUser(user);
            journey.setEndLocationX(-34);
            journey.setEndlocationY(151);
            journey.setGoingDate(new Date());

            DriverDetailDialogFragment fragment = DriverDetailDialogFragment.newInstance(journey);
            fragment.show(getFragmentManager(),"tag");
        }else{

        }
    }
}
