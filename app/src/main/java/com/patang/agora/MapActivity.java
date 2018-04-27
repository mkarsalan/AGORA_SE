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

    @Override
    public void onBackPressed(){

    }

    public void interpolatePoints(LatLng p1, LatLng p2, LatLng p3, int color1, int color2, int color3){

        int max_latitude = Math.max(Math.max((int) (p1.latitude * 10000), (int) (p2.latitude * 10000)), (int) (p3.latitude * 10000));
        int min_latitude = Math.min(Math.min((int) (p1.latitude * 10000), (int) (p2.latitude * 10000)), (int) (p3.latitude * 10000));
        int max_longitude = Math.max(Math.max((int) (p1.longitude * 10000), (int) (p2.longitude * 10000)), (int) (p3.longitude * 10000));
        int min_longitude = Math.min(Math.min((int) (p1.longitude * 10000), (int) (p2.longitude * 10000)), (int) (p3.longitude * 10000));

        double x1 = ((p1.longitude * 10000) - min_longitude) / 10;
        double x2 = ((p2.longitude * 10000) - min_longitude) / 10;
        double x3 = ((p3.longitude * 10000) - min_longitude) / 10;
        double y1 = (max_latitude - (p1.latitude * 10000)) / 10;
        double y2 = (max_latitude - (p2.latitude * 10000)) / 10;
        double y3 = (max_latitude - (p3.latitude * 10000)) / 10;

        int heightBitmap = (max_latitude - min_latitude) / 10;
        int widthBitmap = (max_longitude - min_longitude) / 10;

        Bitmap myBitmap = Bitmap.createBitmap(widthBitmap, heightBitmap, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < widthBitmap; x++) {
            for (int y = 0; y < heightBitmap; y++) {
                double weight1 = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
                double weight2 = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
                double weight3 = 1 - weight1 - weight2;

                int A1 = (int)((double)((color1 >> 24) & 0xff) * weight1);
                int R1 = (int)((double)((color1 >> 16) & 0xff) * weight1);
                int G1 = (int)((double)((color1 >>  8) & 0xff) * weight1);
                int B1 = (int)((double)((color1      ) & 0xff) * weight1);

                int A2 = (int)((double)((color2 >> 24) & 0xff) * weight2);
                int R2 = (int)((double)((color2 >> 16) & 0xff) * weight2);
                int G2 = (int)((double)((color2 >>  8) & 0xff) * weight2);
                int B2 = (int)((double)((color2      ) & 0xff) * weight2);

                int A3 = (int)((double)((color3 >> 24) & 0xff) * weight3);
                int R3 = (int)((double)((color3 >> 16) & 0xff) * weight3);
                int G3 = (int)((double)((color3 >>  8) & 0xff) * weight3);
                int B3 = (int)((double)((color3      ) & 0xff) * weight3);

                int A = A1 + A2 + A3;
                int R = R1 + R2 + R3;
                int B = B1 + B2 + B3;
                int G = G1 + G2 + G3;

                int color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);

                if ((weight1 >= 0 && weight1 <= 1) && (weight2 >= 0 && weight2 <= 1) && (weight3 >= 0 && weight3 <= 1)) {
                    //myBitmap.setPixel(x, y, (int)((double)color1*weight1) + (int)((double)color2*weight2) + (int)((double)color3*weight3) );
                    myBitmap.setPixel(x, y, color );
                    //myBitmap.setPixel(x, y, Barycentric(x, y, (int)x1, (int)y1, (int)x2, (int)y2, (int)x3, (int)y3, color1, color2, color3) );
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

    public int tempToColor(double temp, int alpha){
        int min = -10;
        int max = 60;
        max = max - min;

        int c = (int)( ((temp - min)*255) / (double)max );
        int colour  = Color.argb(alpha,c,c,c);


        return colour;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        googleMap.setMapStyle(style);

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng p15 = new LatLng(31.5204, 74.3587);
        mMap.addMarker(new MarkerOptions().position(p15).title("p15"));

//        addHeatMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p15,12.0f));


//        1.	LUMS 31.4704 74.4108
//        2.	UET  31.5799 74.3561
//        3.	Royal plam 31.5578 74.3624
//        4.	Jinnah hospital 31.4845 74.2974
//        5.	Thokar 31.4711 74.24192
//        6.	faisal town 31.476 74.3045
//        7.	sozo water park 33.8898 73.4318
//        8.	Wagah 31.604948 74.572325
//        9.	Shadbagh 31.6001 74.3397
//        10.	kot lak pat 31.4626 74.3309
//        11.	p15 forte 31.5879 74.3151
//        12.	Data Darbar 31.579 74.3058
//        13.	Garrison gold club 31.5402 74.3954
//        14.	Model Town 31.4777 74.3294
//        15.	Ichra 31.5351 74.3206

        LatLng p1 = new LatLng(31.4704, 74.4108);
        LatLng p2 = new LatLng(31.5799, 74.3561);
        LatLng p3 = new LatLng(31.5578, 74.3624);
        LatLng p11 = new LatLng(31.4845, 74.2974);
        LatLng p10 = new LatLng(31.4711, 74.24192);
        LatLng p5 = new LatLng(31.476, 74.3045);
//        LatLng p15 = new LatLng(33.8898, 73.4318);
        LatLng p13 = new LatLng(31.604948, 74.572325);
        LatLng p8 = new LatLng(31.6001, 74.3397);
        LatLng p4 = new LatLng(31.4626, 74.3309);
        LatLng p6 = new LatLng(31.5879, 74.3151);
        LatLng p14 = new LatLng(31.579, 74.3058);
        LatLng p7 = new LatLng(31.5402, 74.3954);
        LatLng p9 = new LatLng(31.4777, 74.3294);
        LatLng p12 = new LatLng(31.5351, 74.3206);


        int color1  = Color.argb(255,255,0,0);
        int color2  = Color.argb(255,255,0,0);
        int color3  = Color.argb(255,255,0,0);
        int color4  = Color.argb(255,255,0,0);
        int color5  = Color.argb(255,255,0,0);
        int color6  = Color.argb(255,255,0,0);
        int color7  = Color.argb(255,255,0,0);
        int color8  = Color.argb(255,255,0,0);
        int color9  = Color.argb(255,255,0,0);
        int color10  = Color.argb(255,255,0,0);
        int color11  = Color.argb(255,255,0,0);
        int color12  = Color.argb(255,0,255,0);
        int color13  = Color.argb(255,0,255,0);
        int color14  = Color.argb(255,255,0,0);
        int color15  = Color.argb(255,0,255,0);
        int color16  = Color.argb(255,255,255,0);

//        double longitude


        mMap.addMarker(new MarkerOptions().position(p1).title("Pos: 1, Lat: " + p1.latitude + ", Lng: " + p1.longitude));
        mMap.addMarker(new MarkerOptions().position(p2).title("Pos: 2, Lat: " + p2.latitude + ", Lng: " + p2.longitude));
        mMap.addMarker(new MarkerOptions().position(p3).title("Pos: 3, Lat: " + p3.latitude + ", Lng: " + p3.longitude));
        mMap.addMarker(new MarkerOptions().position(p11).title("Pos: 11, Lat: " + p11.latitude + ", Lng: " + p11.longitude));
        mMap.addMarker(new MarkerOptions().position(p10).title("Pos: 10, Lat: " + p10.latitude + ", Lng: " + p10.longitude));
        mMap.addMarker(new MarkerOptions().position(p5).title("Pos: 5, Lat: " + p5.latitude + ", Lng: " + p5.longitude));
        mMap.addMarker(new MarkerOptions().position(p15).title("Pos: 15, Lat: " + p15.latitude + ", Lng: " + p15.longitude));
        mMap.addMarker(new MarkerOptions().position(p13).title("Pos: 13, Lat: " + p13.latitude + ", Lng: " + p13.longitude));
        mMap.addMarker(new MarkerOptions().position(p8).title("Pos: 8, Lat: " + p8.latitude + ", Lng: " + p8.longitude));
        mMap.addMarker(new MarkerOptions().position(p4).title("Pos: 4, Lat: " + p4.latitude + ", Lng: " + p4.longitude));
        mMap.addMarker(new MarkerOptions().position(p6).title("Pos: 6, Lat: " + p6.latitude + ", Lng: " + p6.longitude));
        mMap.addMarker(new MarkerOptions().position(p14).title("Pos: 14, Lat: " + p14.latitude + ", Lng: " + p14.longitude));
        mMap.addMarker(new MarkerOptions().position(p7).title("Pos: 7, Lat: " + p7.latitude + ", Lng: " + p7.longitude));
        mMap.addMarker(new MarkerOptions().position(p9).title("Pos: 9, Lat: " + p9.latitude + ", Lng: " + p9.longitude));
        mMap.addMarker(new MarkerOptions().position(p12).title("Pos: 12, Lat: " + p12.latitude + ", Lng: " + p12.longitude));

//
//        interpolatePoints(p1,p2,p3,tempToColor(40, 255),tempToColor(60, 255),tempToColor(-9, 255));
//        interpolatePoints(p5,p2,p3,color6,color2,color3);
//        interpolatePoints(p1,p10,p3,color1,color5,color3);

        interpolatePoints(p10,p11,p5,color10,color11,color5);
        interpolatePoints(p14,p6,p12,color14,color6,color12);
        interpolatePoints(p12,p6,p8,color12,color6,color8);
        interpolatePoints(p8,p12,p2,color8,color12,color2);
        interpolatePoints(p3,p12,p3,color3,color12,color3);
        interpolatePoints(p3,p12,p15,color3,color12,color15);
        interpolatePoints(p15,p3,p7,color15,color3,color7);
        interpolatePoints(p15,p7,p1,color15,color7,color1);
        interpolatePoints(p1,p15,p9,color1,color15,color9);
        interpolatePoints(p9,p4,p1,color9,color4,color1);
        interpolatePoints(p5,p9,p4,color5,color9,color4);
        interpolatePoints(p11,p5,p15,color11,color5,color15);
        interpolatePoints(p15,p11,p12,color15,color11,color12);
        interpolatePoints(p12,p3,p2,color12,color3,color2);
        interpolatePoints(p10,p5,p4,color10,color5,color4);
        interpolatePoints(p15,p5,p9,color15,color5,color9);
        interpolatePoints(p11,p12,p10,color11,color12,color10);
        interpolatePoints(p10,p12,p14,color10,color12,color14);



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
