package com.samkeet.iins;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by leelash on 04-05-2017.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "GPSTRACKING";
    private static final int REQUEST_PERMISSION_CODE_START = 101;
    private static final int REQUEST_PERMISSION_CODE = 202;
    public TextView mLatitudeText, mLongitudeText, mAccuracyText;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 1;
    private static final float LOCATION_DISTANCE = 1f;

    public double lat;
    public double lon;
    public double acc;

    public Location mLastLocation;

    public LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    public GoogleMap mMap;
    public Marker now;

    public Marker atmMarker, bankMarker, frontGateMarker, parkingMarker, cricketMarker, basketballCourtMarker, cafemainMarker, cafeBMarker;
    public Marker juiceCentreMarker, mainBlockMarker, libraryMarker, seminarHallMarker, archBlockMarker, itBlockMarker, browsingCentreMarker;
    public Marker foodCouretMarker, xeroxMarker, boysMessSouthMarker, boysMessNorthMarker, girlsMessMarker, laundryMarker, tfcMarker, nandaniMarker, bakeryMarker;
    public Marker salonMarker, gymMarker, batmentonMarker, openAmphiMarker, scienceBlockMarker, adminParkingMarker, adminBlockMarker, backGate;
    public Marker boysHostel1Marker, boysHostel2Marker, boysHostel3Marker, boysHostel41Marker, boysHostel42Marker, boysHostel43Marker, girlsHostel1Marker;
    public Marker girlsHostel2Marker, guestHouseMarker, facultyGuestHouseMarker;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLatitudeText = (TextView) view.findViewById(R.id.lat);
        mLongitudeText = (TextView) view.findViewById(R.id.log);
        mAccuracyText = (TextView) view.findViewById(R.id.acc);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE_START);
            return;
        }
        startTracking();
    }

    public void startTracking() {

        int off = 0;
        try {
            off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (off == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
            builder.setTitle("Please Enable GPS");
            builder.setMessage("We need you to enable GPS for high accuracy in tracking your location.");
            String positiveText = "Enable";
            builder.setPositiveButton(positiveText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                        }
                    });
            String negativeText = "Exit";
            builder.setNegativeButton(negativeText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();

        }
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private Location isBetterLocation(Location oldLocation, Location newLocation) {


        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == REQUEST_PERMISSION_CODE_START) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startTracking();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity().getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getActivity().getApplicationContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getActivity().getApplicationContext(), R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        if (Constants.LocationsData.lat != 0) {
            now = mMap.addMarker(new MarkerOptions().position(new LatLng(Constants.LocationsData.lat, Constants.LocationsData.lon)).zIndex(10f).draggable(false).title("Me"));
        }else {
            now = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).zIndex(10f).draggable(false).title("Me"));
        }
    }

    private class LocationListener implements android.location.LocationListener {

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {


            //mLastLocation = isBetterLocation(mLastLocation, location);
            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                acc = location.getAccuracy();

                mLongitudeText.setText(String.valueOf(lon));
                mLatitudeText.setText(String.valueOf(lat));
                mAccuracyText.setText(String.valueOf(acc));

                Constants.LocationsData.lat = lat;
                Constants.LocationsData.lon = lon;
                Constants.LocationsData.acc = acc;
                if (now != null) {
                    now.remove();

                }
                now = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).draggable(false).title("My Location"));
                // Showing the current location in Google Map
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));

                // Zoom in the Google Map
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

}
