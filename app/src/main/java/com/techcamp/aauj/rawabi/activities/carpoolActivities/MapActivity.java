package com.techcamp.aauj.rawabi.activities.carpoolActivities;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.techcamp.aauj.rawabi.R;
import com.techcamp.aauj.rawabi.utils.DateUtil;
import com.techcamp.aauj.rawabi.utils.MapUtil;

import java.util.Calendar;
import java.util.Date;

public abstract class MapActivity extends AppCompatActivity implements OnMapReadyCallback,TimePickerDialog.OnTimeSetListener{
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    private static final String TAG = "tag";
    private static final float DEFAULT_ZOOM = 15;
    protected GoogleMap mMap;
    private boolean getLocation = false;

    protected ProgressBar mProgressBarLoading;
    private LatLng mLatLngCenter;
    protected Marker mMarkerCenter;
    protected Marker mMarkerFrom,mMarkerTo;

    private boolean mLocationPermissionGranted;
    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        // Construct a GeoDataClient.
        mProgressBarLoading = findViewById(R.id.progressBar);
        startLoading(false);
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
//        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (savedInstanceState != null) {
//            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setupMap();

        enableGetLocation();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mapGoTo(place.getLatLng());
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    protected abstract int getLayout();


    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMyLocationEnable();



        // setup center marker
        mMarkerCenter = mMap.addMarker(new MarkerOptions().position(googleMap.getCameraPosition().target));
        mMarkerCenter.setVisible(false);

        getDeviceLocation();

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mLatLngCenter = mMap.getCameraPosition().target;
                mMarkerCenter.setPosition(mLatLngCenter);
            }
        });


    }
    private void setMyLocationEnable() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }
        updateLocationUI();
    }
    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void getDeviceLocation() {

    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mLatLngCenter = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
                            mMarkerCenter.setPosition(mLatLngCenter );
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }catch (Exception e){e.printStackTrace();}
    }
    private void updateLocationUI() {
        getLocationPermission();
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected void pressFromMode(View view){
        if(mMarkerFrom == null){
            mMarkerFrom = mMap.addMarker(new MarkerOptions().position(mMarkerCenter.getPosition())
                    .icon(MapUtil.getMarkerIcon("#63d25d"))
            );
        }else{
            mMarkerFrom.setPosition(mMarkerCenter.getPosition());
        }
        ((Button)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp,0,0,0);
    }
    protected void pressToMode(View view){
        if(mMarkerTo == null){
            mMarkerTo = mMap.addMarker(new MarkerOptions().position(mMarkerCenter.getPosition())
                .icon(MapUtil.getMarkerIcon("#475862"))
            );
        }else{
            mMarkerTo.setPosition(mMarkerCenter.getPosition());
        }
        ((Button)view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp,0,0,0);
    }
    protected void pressTime(View view){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this
        ,hour,minute,true);

        timePickerDialog.setTitle("Set Going Time");
        timePickerDialog.show();
    }
    private void enableGetLocation(){
        getLocation = true;
        showCenterMarker(true);
    }

    private void showCenterMarker(boolean b) {
        findViewById(R.id.imgCenterMarker).setVisibility(b?View.VISIBLE:View.GONE);
    }
    public void findPlace(View view) {


        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
//                    .setCountry("PS")
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (Exception e){}
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mapGoTo(place.getLatLng());
                Log.i("tag", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void mapGoTo(LatLng latLng) {
        if(mMap == null)
            return;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Button btnSetTime = ((Button)findViewById(R.id.btnSetTime));

        btnSetTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_24dp,0,0,0);
        Date date = new Date();
        date.setHours(i);
        date.setMinutes(i1);

        String time = DateUtil.formatDateToTime(date.getTime());

        btnSetTime.setText(time);


    }
    protected void drawMarkers(){
        if(mMap == null)
            return;
        if(mMarkerFrom != null){
            mMarkerFrom.remove();
            mMarkerFrom = mMap.addMarker(new MarkerOptions().position(mMarkerFrom.getPosition())
                .icon(MapUtil.getMarkerIcon("#63d25d"))
            );
        }
        if(mMarkerTo != null){
            mMarkerTo.remove();
            mMarkerTo = mMap.addMarker(new MarkerOptions().position(mMarkerTo.getPosition())
                    .icon(MapUtil.getMarkerIcon("#475862")));
        }


    }

    void startLoading(boolean b){
        mProgressBarLoading.setVisibility(b?View.VISIBLE:View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.carpool_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_profile) {
            startProfileActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void startProfileActivity() {
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);

    }
}
