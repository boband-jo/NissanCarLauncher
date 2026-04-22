
package com.nissan.carlauncher;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CarDataService extends Service {
    
    private static final String TAG = "CarDataService";
    
    // 车机数据监听器接口
    public interface CarDataListener {
        void onSpeedChanged(float speed);
        void onFuelLevelChanged(float level);
        void onTemperatureChanged(float temperature);
        void onLocationChanged(Location location);
    }
    
    private final IBinder mBinder = new LocalBinder();
    private List<CarDataListener> mListeners = new ArrayList<>();
    
    private LocationManager mLocationManager;
    private Handler mHandler;
    
    // 模拟车机数据
    private float mCurrentSpeed = 0;
    private float mFuelLevel = 75.0f;
    private float mInsideTemperature = 22.5f;
    private Location mLastLocation;
    
    private boolean mIsRunning = false;
    
    public class LocalBinder extends Binder {
        CarDataService getService() {
            return CarDataService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        initLocationManager();
        startDataSimulation();
    }
    
    private void initLocationManager() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, // 1秒更新一次
                    10, // 10米更新一次
                    mLocationListener);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission denied", e);
        }
    }
    
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            notifyLocationChanged(location);
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
    
    private void startDataSimulation() {
        mIsRunning = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mIsRunning) return;
                
                // 模拟速度变化
                mCurrentSpeed = (float) (Math.random() * 100);
                notifySpeedChanged(mCurrentSpeed);
                
                // 模拟油耗变化
                mFuelLevel -= (float) (Math.random() * 0.1);
                if (mFuelLevel < 0) mFuelLevel = 100;
                notifyFuelLevelChanged(mFuelLevel);
                
                // 模拟温度变化
                mInsideTemperature = 20 + (float) (Math.random() * 10);
                notifyTemperatureChanged(mInsideTemperature);
                
                mHandler.postDelayed(this, 2000);
            }
        });
    }
    
    public void addListener(CarDataListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }
    
    public void removeListener(CarDataListener listener) {
        mListeners.remove(listener);
    }
    
    private void notifySpeedChanged(float speed) {
        for (CarDataListener listener : mListeners) {
            listener.onSpeedChanged(speed);
        }
    }
    
    private void notifyFuelLevelChanged(float level) {
        for (CarDataListener listener : mListeners) {
            listener.onFuelLevelChanged(level);
        }
    }
    
    private void notifyTemperatureChanged(float temperature) {
        for (CarDataListener listener : mListeners) {
            listener.onTemperatureChanged(temperature);
        }
    }
    
    private void notifyLocationChanged(Location location) {
        for (CarDataListener listener : mListeners) {
            listener.onLocationChanged(location);
        }
    }
    
    // 公共方法获取车机数据
    public float getCurrentSpeed() {
        return mCurrentSpeed;
    }
    
    public float getFuelLevel() {
        return mFuelLevel;
    }
    
    public float getInsideTemperature() {
        return mInsideTemperature;
    }
    
    public Location getLastLocation() {
        return mLastLocation;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRunning = false;
        try {
            mLocationManager.removeUpdates(mLocationListener);
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission denied", e);
        }
    }
}
