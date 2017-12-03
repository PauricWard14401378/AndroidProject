package ucd.team4.Project;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by pauric on 02/12/2017.
 */

public class LocationService extends Service{
    public LocationListener location= new LocationListener() {
        public  void onLocationChanged(Location location){
            //Toast.makeText(this, "Permission (already) Granted1!", Toast.LENGTH_SHORT).show();
            LatLng coord=new LatLng(location.getLatitude(), location.getLongitude());
            if(Running.WORKOUT_STARTED){
                Running.points.add(coord);

            }
            Running.mMap.clear();
            //boolean isOnRoute = PolyUtil.isLocationOnPath(coord, Running.points, false, 10.0f);
            //if(isOnRoute){
            PolylineOptions options = new PolylineOptions().width(20).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < Running.points.size(); i++) {
                LatLng point = Running.points.get(i);
                options.add(point);
            }
            int lastPoint = Running.points.size()-1;
            int penulPoint = Running.points.size()-2;
            if(Running.points.size()>1){
                double distance = Running.GetDistanceFromLatLonInKm(Running.points.get(lastPoint).latitude,Running.points.get(lastPoint).longitude,Running.points.get(penulPoint).latitude,Running.points.get(penulPoint).longitude);
                Toast.makeText(getApplicationContext(),"distance changed "+distance,Toast.LENGTH_SHORT).show();
                Running.totalDistance+=distance;
                Toast.makeText(getApplicationContext(),"Total Distance "+Running.totalDistance,Toast.LENGTH_SHORT).show();
                Running.distanceTravelled.setText("travelled "+ Running.totalDistance);
            }

            Running.mMap.addMarker(new MarkerOptions().position(coord).title("Marker in Sydney"));
            Running.mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            Running.line = Running.mMap.addPolyline(options);

        }
        @Override
        public  void onStatusChanged(String provider, int status, Bundle extras){

        }
        @Override
        public  void onProviderEnabled(String s){

        }
        @Override
        public  void onProviderDisabled(String s){

        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // This won't be a bound service, so simply return null
        return null;
    }

    @Override
    public void onCreate() {

        // This will be called when your Service is created for the first time
        // Just do any operations you need in this method.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    1);
            return;
        }
        Running.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, location);
        Running.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, location);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
