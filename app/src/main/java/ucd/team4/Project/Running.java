package ucd.team4.Project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Running extends FragmentActivity implements OnMapReadyCallback, LocationListener,SensorEventListener, StepListener {

    private static final int MY_PERMISSIONS_REQUEST_GET_LOCATION = 1;
    public static GoogleMap mMap;
    public static LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    public static ArrayList<LatLng> points; //added
    public static Polyline line; //added
    public static boolean WORKOUT_STARTED=false;
    private TextView workoutStarted;
    private LatLng origin;
    private LatLng destination;
    public static double totalDistance = 0;
    public static TextView distanceTravelled;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private final String TEXT_NUM_STEPS = "Number of Steps: ";
    public int numSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate: ");
        super.onCreate(savedInstanceState);
        if(isAppInstalled("com.spotify.music")) {
            setContentView(R.layout.activity_running_with_spotify);
            Button buttonOne = (Button) findViewById(R.id.play);
            buttonOne.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                    if(isAppInstalled("com.spotify.music")) {
                        Intent intent=new Intent("com.spotify.mobile.android.ui.widget.PLAY");
                        intent.setPackage("com.spotify.music");
                        sendBroadcast(intent);
//                    System.out.println("lizards");
//                    intent.setData(Uri.parse("spotify:user:spotify:playlist:37i9dQZF1DX1gcrZ1xC96D"));
                    }else{
                        Intent intent=new Intent("com.spotify.mobile.android.ui.widget.NEXT");
                        intent.setPackage("com.spotify.music");
                        sendBroadcast(intent);
//                    intent.setData(Uri.parse("market://details?id=com.spotify.music&hl=en"));
                    }
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                }
            });
            Button buttonTwo = (Button) findViewById(R.id.next);
            buttonTwo.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                }
            });
            Button buttonThree = (Button) findViewById(R.id.previous);
            buttonThree.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                }
            });
        }else{
            setContentView(R.layout.activity_running);
        }
        Button buttonFour = (Button) findViewById(R.id.button4);

        points = new ArrayList<LatLng>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_LOCATION);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        distanceTravelled = (TextView) findViewById(R.id.distanceTravelled);
        workoutStarted = (Button) findViewById(R.id.button1);
        workoutStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WORKOUT_STARTED){
                    RunHistory.noHistory=false;
                    WORKOUT_STARTED=false;
                    stopPedometer();
                    ContentValues values= new ContentValues();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
                    String time=Calendar.HOUR_OF_DAY+":"+Calendar.MINUTE;
                    String formattedDate = df.format(c.getTime());
                    System.out.println("datered"+formattedDate);

                    values.put("date", formattedDate);
                    values.put("time", time);
                    values.put("distance", totalDistance);
                    values.put("calories", 400);
                    values.put("steps", 1000);
                    long done=MainActivity.dbWritable.insert("runHistory", null, values);
                    System.out.println("datered"+done);

                    workoutStarted.setText("Start Workout");
                }
                else{
                    WORKOUT_STARTED=true;
                    startPedometer();
                    workoutStarted.setText("Stop workout");
                }

            }
        });

    }
    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        }
        else {
            return false;
        }
    }
//    private void getRoute(){
//        DateTime now = new DateTime();
//        DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
//                .mode(TravelMode.DRIVING).origin()
//                .destination(destination).departureTime(now)
//                        .await();
//    }
//    private GeoApiContext getGeoContext(){
//        GeoApiContext geoApiContext = new GeoApiContext();
//        return geoApiContext.setQueryRateLimit(3).setApiKey(getString(R.string.google_maps_key2)).setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS).setWriteTimeout(1, TimeUnit.SECONDS);
//    }

    public  void onLocationChanged(Location location){
        if(WORKOUT_STARTED) {
            Toast.makeText(this, "Permission (already) Granted1!", Toast.LENGTH_SHORT).show();
            LatLng coord = new LatLng(location.getLatitude(), location.getLongitude());

            points.add(coord);

            mMap.clear();
            //boolean isOnRoute = PolyUtil.isLocationOnPath(coord, points, false, 10.0f);
            //if(isOnRoute){
            PolylineOptions options = new PolylineOptions().width(20).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            int lastPoint = points.size() - 1;
            int penulPoint = points.size() - 2;
            if (points.size() > 1) {
                double distance = GetDistanceFromLatLonInKm(points.get(lastPoint).latitude, points.get(lastPoint).longitude, points.get(penulPoint).latitude, points.get(penulPoint).longitude);
                Toast.makeText(getApplicationContext(), "distance changed " + distance, Toast.LENGTH_SHORT).show();
                totalDistance += distance;
                Toast.makeText(getApplicationContext(), "Total Distance " + totalDistance, Toast.LENGTH_SHORT).show();
                distanceTravelled.setText("travelled " + totalDistance);
            }

            mMap.addMarker(new MarkerOptions().position(coord).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
            line = mMap.addPolyline(options);
        }
    }

    public static double GetDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        // Distance in km
        return d;
    }

    private static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public  void onStatusChanged(String provider, int status, Bundle extras){

    }
    @Override
    public  void onProviderEnabled(String s){

    }
    @Override
    public  void onProviderDisabled(String s){

    }
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
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
        mMap = googleMap;

        Log.d("map ready", "onMapReady: "+mMap.isMyLocationEnabled());
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?


            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        }


        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        showLocationPermission();
                        if (location != null) {
                            Log.d("success", "success");
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            LatLng latLng = new LatLng(lat,lon);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10.0f));
                        }
                        else{
                            LatLng latLng = new LatLng(54.653,-8.156);
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Null Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }
                    }
                });

    }

    private void showLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_GET_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_GET_LOCATION);
            }
        } else {
            Toast.makeText(this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
    public void startPedometer(){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        numSteps = 0;
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);

    }
    public void stopPedometer(){
        sensorManager.unregisterListener(this);
    }
    @Override
    public void step(long timeNs) {
        numSteps++;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }



}