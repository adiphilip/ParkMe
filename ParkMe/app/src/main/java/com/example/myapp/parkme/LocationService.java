package com.example.myapp.parkme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;


public class LocationService {
    private Context context;
    private LocationManager lm;
    private MyLocationListener myListener;


    public LocationService(Context context) {
        this.context = context;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        myListener = new MyLocationListener();
        lm.requestLocationUpdates(lm.GPS_PROVIDER, 3*60*1000, 1000, myListener);
    }
    public String getLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "32.0281749,34.8825533";
        }
        Location myLocation =  myListener.getMyCurrentLocation();
        Double myLatitude = myLocation.getLatitude();
        Double myLongitude = myLocation.getLongitude();
        return "" + myLatitude + "," + myLongitude;
    }
    public void updateLocation(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        myListener = new MyLocationListener();
        lm.requestLocationUpdates(lm.GPS_PROVIDER, 3*60*1000, 1000, myListener);
    }

    public class MyLocationListener implements LocationListener
    {
        private Location myCurrentLocation = new Location(LocationManager.GPS_PROVIDER);
        public Location getMyCurrentLocation(){
            return myCurrentLocation;
        }
        @Override
        public void onLocationChanged(Location location) {
            myCurrentLocation.set(location);

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
}

