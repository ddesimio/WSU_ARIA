package mwdindustries.aria.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import mwdindustries.aria.ARView;

/**
 * Created by Phil on 3/19/2015.
 */
public class AdvancedLocationService extends Service implements LocationListener, SensorEventListener{
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // every 5 meters will automatically trigger an update
    private final long MIN_TIME_BETWEEN_UPDATES = 750; // every 750 milliseconds will trigger an update
    private final long MIN_TIME_BETWEEN_HIDDEN_UPDATES = 1500; // every 1.5 seconds will trigger an update

    private double latitudeOffset = 0;
    private double longitudeOffset = 0;
    private LocationManager mLocationManager;
    private Location mLocation;
    private double latitude = 181;
    private double longitude = -181;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float orientation[];

    //flags
    private boolean onPause = false;

    /**
     * Constructor for the service. Must be passed the context for the thread or communication cannot occur between the two.
     * @param mContext the Context of the thread which is calling it. Simply put "this" to pass the pointer to the thread.
     */
    public AdvancedLocationService(Context mContext) {
        orientation = new float[3];
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            mSensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                if(!onPause){
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(mLocation != null){
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                } else {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BETWEEN_HIDDEN_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if(mLocationManager != null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if(mLocation != null){
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * This will decrease the frequency in which the updating of the GPS location will occur. This will reduce in power consumption but will not allow the location to be lost.
     */
    public void goToBackground(){
        if(!onPause) {
            onPause = true;
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_HIDDEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    /**
     * This will increase the frequency in which the updating of the GPS location will occur
     */
    public void goToForeground(){
        if(onPause) {
            onPause = false;
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    /**
     * Accessor for the latitude according to the GPS on the phone.
     * @return latitude of current location
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * Accessor for the longitude according to the GPS on the phone.
     * @return longitude of current location
     */
    public double getLongitude(){
        return this.longitude;
    }

    /**
     * Accessor for the orientation vector for the phone
     * @return float for the rotation of the phone facing east
     */
    public float getOrientation() {
        return orientation[0] + (float)Math.toRadians(90);
    }

    public float bearingTo(AdvancedLocation location){
        return (bearingTo(location.getLocation()) - (float)Math.toDegrees(this.getOrientation()))%360;
    }

    public float bearingTo(Location location){
        return mLocation.bearingTo(location);
    }

    public float distanceTo(AdvancedLocation location){
        return distanceTo(location.getLocation());
    }

    public float distanceTo(Location location){
        return mLocation.distanceTo(location);
    }

    public float magneticNorth() {
        Location l = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        l.setLongitude(0);
        l.setLatitude(0);
        return mLocation.bearingTo(l);
    }

    /**
     * Called when the location has changed.
     * <p/>
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        latitude = mLocation.getLatitude() + latitudeOffset;
        longitude = mLocation.getLongitude() + longitudeOffset;
    }

    private float[] mAccel;
    private float[] mMagnet;
    /**
     * Called when sensor values have changed.
     * <p>See {@link android.hardware.SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link android.hardware.SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link android.hardware.SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mAccel = event.values;
        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            mMagnet = event.values;
        }
        if(mAccel != null && mMagnet != null){
            float R[] = new float[9];
            if(SensorManager.getRotationMatrix(R, null, mAccel, mMagnet)){
                orientation = SensorManager.getOrientation(R, orientation);
            }
        }
    }

    /**
     * Calibration method based on the assumption that all coordinates are correct relative to each other
     */
    public void calibrate() {
        double lat = 0, lon = 0;
        for(int i=0; i<30; i++){
            lat += mLocation.getLongitude();
            lon += mLocation.getLatitude();
        }
        latitudeOffset = (lat / 30d) - 39.779752d;
        longitudeOffset = (lon / 30d) + 84.063405d;
    }

    public double getAccuracy() {
        return mLocation.getAccuracy();
    }
    // ignored listener events
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override
    public IBinder onBind(Intent intent) {return null;}
}