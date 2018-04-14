package com.patang.agora;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Heatmap extends AppCompatActivity {


    private static final String TAG = "Heatmap";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        if (isServiesOK()) {
            init();
        }
    }

    private void init(){
        Button moveToMap = (Button) findViewById(R.id.moveToMap);
        moveToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Heatmap.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }



//        AIzaSyCI_PKfCqvEwXWkVhQe6CCs1RRI1Uqj5cs

    public boolean isServiesOK(){
        Log.d(TAG, "isServersOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Heatmap.this);
        if (available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Heatmap.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        } else{
            Toast.makeText(this,"You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return  false;
    }


}
