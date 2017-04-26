package com.example.administrator.yamba;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Polonsky on 4/26/2017.
 */

public class LocationActivity extends Activity {
    private static final String TAG=LocationActivity.class.getSimpleName();
    LocationManager locationManager;
    Geocoder geocoder;
    LocationListener locationListener;
    TextView textviewLocationInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.location);
        this.textviewLocationInfo=(TextView)findViewById(R.id.textViewLocationInfo);
        this.locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        this.geocoder=new Geocoder(this);
        this.locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude,latitude;
                longitude=location.getLongitude();
                latitude=location.getLatitude();
                String str=String.format("Latitude: %f\nLongitude: %f\nAlt: %f\nBearing: %f",
                        latitude,
                        longitude,
                        location.getAltitude(),
                        location.getBearing());
                LocationActivity.this.textviewLocationInfo.setText(str);
                try {
                    List<Address> addresses=LocationActivity.this.geocoder.getFromLocation(latitude, longitude, 5);
                    for(Address address:addresses)
                    {
                        LocationActivity.this.textviewLocationInfo.append("\nLocation:");
                        for(int i=0;i<=address.getMaxAddressLineIndex();i++)
                        {
                            LocationActivity.this.textviewLocationInfo.append("\n"+address.getAddressLine(i));
                        }
                    }
                }catch(IOException e)
                {
                    Log.i(TAG,"Couldn't cat Geocoder data.");
                    Toast.makeText(LocationActivity.this,"Couldn't cat Geocoder data.",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        try {
            Location lastKnownLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastKnownLocation!=null)
                this.locationListener.onLocationChanged(lastKnownLocation);
        }catch (SecurityException e)
        {
            Toast.makeText(this,"Permission denied. Unable to access location. ",Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Permission denied.");
            throw e;
        }
        Log.i(TAG,"onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //super.onRestart(); //???????
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this.locationListener);
        }catch(SecurityException e)
        {
            Toast.makeText(this,"Permission denied. Unable to access location. ",Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Permission denied.");
        }
        Log.i(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this.locationListener);
        Log.i(TAG,"onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.textviewLocationInfo=null;
        this.locationListener=null;
        this.geocoder=null;
        this.locationManager=null;
        Log.i(TAG,"onDestroy");
    }
}
