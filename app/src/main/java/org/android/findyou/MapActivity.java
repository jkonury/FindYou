package org.android.findyou;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.android.findyou.util.BackPressCloseHandler;


public class MapActivity extends FragmentActivity implements LocationListener, TextView.OnEditorActionListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private InputMethodManager inputMethodManager;
    private EditText editText;
    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        //locationManager.requestLocationUpdates(provider, 3000, 5, this);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        backPressCloseHandler = new BackPressCloseHandler(this);

        setUpMapIfNeeded();

        moveCurrentLocation();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void moveCurrentLocation() {
        //Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("findyou", "isGPSEnabled : " + isGPSEnabled);
        Log.d("findyou", "isNetworkEnabled : " + isNetworkEnabled);

        Location currentLocation;
        LatLng latlng;

        if (isNetworkEnabled) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, this);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, this);
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (currentLocation != null) {
            latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            latlng = new LatLng(37.48121,126.952712);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
        mMap.addMarker(new MarkerOptions().position(latlng).title("현재 위치"));
    }

    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onLocationChanged(Location location) {

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

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_NULL) {
            // 키보드 내리기
            inputMethodManager.hideSoftInputFromInputMethod(editText.getWindowToken(), 0);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
