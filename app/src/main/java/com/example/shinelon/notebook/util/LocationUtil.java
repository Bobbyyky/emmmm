package com.example.shinelon.notebook.util;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Shinelon on 2019/3/9.
 */

public class LocationUtil implements LocationListener{

    private Util util;
    private LocationManager locationManager;
    private AppCompatActivity activity;
    private Handler handler;
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private Fragment fragment;

    public LocationUtil(AppCompatActivity activity,Handler handler){
        util = new Util();
        this.activity = activity;
        this.handler = handler;
    }

    public void removeLocationUpdates(){
        if (locationManager != null){
            boolean locationPermissionFlag = util.checkPermission(locationPermission,activity);
            if (!locationPermissionFlag){
                locationManager.removeUpdates(this);
                locationManager = null;
            }
        }
    }

    public void getLocation(Fragment fragment){
        this.fragment = fragment;
        boolean locationPermissionFlag = util.checkPermission(locationPermission,activity);
        if (locationPermissionFlag){
            fragment.requestPermissions(locationPermission,Configure.LOCATION_PERMISSION_CODE);
        }else {
            locationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
            String provider;
            List<String> providerList = locationManager.getProviders(true);
            if (providerList.contains(LocationManager.GPS_PROVIDER)){
                provider = LocationManager.GPS_PROVIDER;
            }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
                provider = LocationManager.NETWORK_PROVIDER;
            }else {
                Toast.makeText(activity,"请打开网络",Toast.LENGTH_LONG).show();
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider,2000,10,this);
            if (location != null){
                getLocation(location);
            }

        }
    }

    private void getLocation(Location location){
        Geocoder geocoder = new Geocoder(activity);
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),location.getLongitude(),1);
            Address address = addresses.get(0);
            String result = address.getAddressLine(1) + address.getFeatureName();
            Message message = handler.obtainMessage();
            message.what = Configure.LOCATION_MESSAGE_CODE;
            message.obj = result;
            handler.sendMessage(message);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation(fragment);
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
}
