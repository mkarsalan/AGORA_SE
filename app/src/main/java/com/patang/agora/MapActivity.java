package com.patang.agora;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.HeatmapTileProvider;


import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


import android.*;
import android.Manifest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        googleMap.setMapStyle(style);

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng lahore = new LatLng(31.5204, 74.3587);
        mMap.addMarker(new MarkerOptions().position(lahore).title("Lahore"));

//        addHeatMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lahore,12.0f));

        // overlay attempt

//        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {

                LatLng mLatLngNetwork = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
                LatLng topLeft = visibleRegion.farLeft;
                LatLng bottomLeft = visibleRegion.nearLeft;
                LatLng topRight = visibleRegion.farRight;
                LatLng bottomRight = visibleRegion.nearRight;
                LatLngBounds latLngBounds = new LatLngBounds.Builder().include(mLatLngNetwork).include(topLeft).include(bottomLeft).include(topRight).include(bottomRight).build();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.inter);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions().image(bitmapDescriptor).positionFromBounds(latLngBounds)
                        .transparency(0.68f);
                mMap.addGroundOverlay(groundOverlayOptions);

//            }
//        });

    }




    private void addHeatMap() {
//        List<LatLng> list = null;

        ArrayList<WeightedLatLng> list = new ArrayList<WeightedLatLng>();
        for (double i = 0; i < 10; i++) {
            double lat = 31.5204 + (i * 0.01);
            double lng = 74.3587 + (i * 0.01);
            LatLng a = new LatLng(lat,lng);
            WeightedLatLng b = new WeightedLatLng(a, 0.1);
            list.add(b);

            mProvider = new HeatmapTileProvider.Builder().weightedData(list).radius((int)(50-(i * 0.10))).build();
            mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
//        mProvider = new HeatmapTileProvider.Builder().weightedData(list).build();


        // Add a tile overlay to the map, using the heat map tile provider.
    }













}


















//
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onMapReady: map is ready");
//        mMap = googleMap;
//
//        if (mLocationPermissionsGranted) {
//            getDeviceLocation();
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//        }
//    }
//
//    private static final String TAG = "MapActivity";
//
//    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
//    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
//    private static final float DEFAULT_ZOOM = 15f;
//
//    //vars
//    private Boolean mLocationPermissionsGranted = false;
//    private GoogleMap mMap;
//    private FusedLocationProviderClient mFusedLocationProviderClient;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//
//        getLocationPermission();
//    }
//
//    private void getDeviceLocation(){
//        Log.d(TAG, "getDeviceLocation: getting the devices current location");
//
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        try{
//            if(mLocationPermissionsGranted){
//
//                final Task location = mFusedLocationProviderClient.getLastLocation();
//                location.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if(task.isSuccessful()){
//                            Log.d(TAG, "onComplete: found location!");
//                            Location currentLocation = (Location) task.getResult();
//
//                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
//                                    DEFAULT_ZOOM);
//
//                        }else{
//                            Log.d(TAG, "onComplete: current location is null");
//                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        }catch (SecurityException e){
//            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
//        }
//    }
//
//    private void moveCamera(LatLng latLng, float zoom){
//        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
//    }
//
//    private void initMap(){
//        Log.d(TAG, "initMap: initializing map");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(MapActivity.this);
//    }
//
//    private void getLocationPermission(){
//        Log.d(TAG, "getLocationPermission: getting location permissions");
//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//                mLocationPermissionsGranted = true;
//                initMap();
//            }else{
//                ActivityCompat.requestPermissions(this,
//                        permissions,
//                        LOCATION_PERMISSION_REQUEST_CODE);
//            }
//        }else{
//            ActivityCompat.requestPermissions(this,
//                    permissions,
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult: called.");
//        mLocationPermissionsGranted = false;
//
//        switch(requestCode){
//            case LOCATION_PERMISSION_REQUEST_CODE:{
//                if(grantResults.length > 0){
//                    for(int i = 0; i < grantResults.length; i++){
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                            mLocationPermissionsGranted = false;
//                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
//                            return;
//                        }
//                    }
//                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
//                    mLocationPermissionsGranted = true;
//                    //initialize our map
//                    initMap();
//                }
//            }
//        }
//    }
//
//
//}
