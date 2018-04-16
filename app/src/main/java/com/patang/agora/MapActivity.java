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

    public void interpolatePoints(LatLng Pos1, LatLng Pos2, LatLng Pos3, double Intensity1, double Intensity2, double Intensity3){

        int max_latitude = Math.max(Math.max((int) (Pos1.latitude * 10000), (int) (Pos2.latitude * 10000)), (int) (Pos3.latitude * 10000));
        int min_latitude = Math.min(Math.min((int) (Pos1.latitude * 10000), (int) (Pos2.latitude * 10000)), (int) (Pos3.latitude * 10000));
        int max_longitude = Math.max(Math.max((int) (Pos1.longitude * 10000), (int) (Pos2.longitude * 10000)), (int) (Pos3.longitude * 10000));
        int min_longitude = Math.min(Math.min((int) (Pos1.longitude * 10000), (int) (Pos2.longitude * 10000)), (int) (Pos3.longitude * 10000));

        double x1 = ((Pos1.longitude * 10000) - min_longitude) / 10;
        double x2 = ((Pos2.longitude * 10000) - min_longitude) / 10;
        double x3 = ((Pos3.longitude * 10000) - min_longitude) / 10;
        double y1 = (max_latitude - (Pos1.latitude * 10000)) / 10;
        double y2 = (max_latitude - (Pos2.latitude * 10000)) / 10;
        double y3 = (max_latitude - (Pos3.latitude * 10000)) / 10;

        int heightBitmap = (max_latitude - min_latitude) / 10;
        int widthBitmap = (max_longitude - min_longitude) / 10;

        Bitmap myBitmap = Bitmap.createBitmap(widthBitmap, heightBitmap, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < widthBitmap; x++) {
            for (int y = 0; y < heightBitmap; y++) {
                double weight1 = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
                double weight2 = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
                double weight3 = 1 - weight1 - weight2;

                if ((weight1 >= 0 && weight1 <= 1) && (weight2 >= 0 && weight2 <= 1) && (weight3 >= 0 && weight3 <= 1)) {
                    myBitmap.setPixel(x, y, Color.argb(255, (int) (weight1 * 255), (int) (weight2 * 255), (int) (weight3 * 255)));
                } else {
//                        myBitmap.setPixel(x, y, Color.argb(255, 255,255,255));
                }
            }
        }
        LatLngBounds PosBounds = new LatLngBounds(
                new LatLng(min_latitude * 0.0001, min_longitude * 0.0001),       // South west corner
                new LatLng(max_latitude * 0.0001, max_longitude * 0.0001));      // North east corner

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(myBitmap);
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions().image(bitmapDescriptor).positionFromBounds(PosBounds)
                .transparency(0.5f);

        mMap.addGroundOverlay(groundOverlayOptions);
    }

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


        double[] N = {31.4704,31.5799,31.4798,31.5525,31.4488,31.5879};
        double[] E = {74.4108,74.3561,74.2802,74.3381,74.2701,74.3151};
        double[] value = {0.23,0.34,0.45,0.10,0.60,0.90};
        double[] col = {0.23,0.34,0.45,0.10,0.60,0.90};


        LatLng Pos1 = new LatLng(31.4704, 74.4108);
        LatLng Pos2 = new LatLng(31.5799, 74.3561);
        LatLng Pos3 = new LatLng(31.4798, 74.2802);
        LatLng Pos4 = new LatLng(31.5525, 74.3381);
        LatLng Pos5 = new LatLng(31.4488, 74.2701);
        LatLng Pos6 = new LatLng(31.5879, 74.3151);

        double Intensity1 = 0.5;
        double Intensity2 = 0.5;
        double Intensity3 = 0.5;
        double Intensity4 = 0.5;
        double Intensity5 = 0.5;
        double Intensity6 = 0.5;


        mMap.addMarker(new MarkerOptions().position(Pos1).title("Pos: 1, Lat: " + Pos1.latitude + ", Lng: " + Pos1.longitude));
        mMap.addMarker(new MarkerOptions().position(Pos2).title("Pos: 2, Lat: " + Pos2.latitude + ", Lng: " + Pos2.longitude));
        mMap.addMarker(new MarkerOptions().position(Pos3).title("Pos: 3, Lat: " + Pos3.latitude + ", Lng: " + Pos3.longitude));
        mMap.addMarker(new MarkerOptions().position(Pos4).title("Pos: 4, Lat: " + Pos4.latitude + ", Lng: " + Pos4.longitude));
        mMap.addMarker(new MarkerOptions().position(Pos5).title("Pos: 5, Lat: " + Pos5.latitude + ", Lng: " + Pos5.longitude));
        mMap.addMarker(new MarkerOptions().position(Pos6).title("Pos: 6, Lat: " + Pos6.latitude + ", Lng: " + Pos6.longitude));

        interpolatePoints(Pos1,Pos2,Pos3,Intensity1,Intensity2,Intensity3);
        interpolatePoints(Pos6,Pos2,Pos3,Intensity6,Intensity2,Intensity3);
        interpolatePoints(Pos1,Pos5,Pos3,Intensity1,Intensity5,Intensity3);


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
