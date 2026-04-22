
package com.nissan.carlauncher;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class TrafficLightService extends Service {
    
    private static final String TAG = "TrafficLightService";
    
    private WindowManager mWindowManager;
    private View mTrafficLightView;
    private ImageView mLightImageView;
    private TextView mCountdownTextView;
    
    private Handler mHandler;
    
    // 红绿灯状态
    private static final int STATE_RED = 0;
    private static final int STATE_YELLOW = 1;
    private static final int STATE_GREEN = 2;
    
    private int mCurrentState = STATE_RED;
    private int mCountdown = 5;
    private boolean mIsShowing = false;
    
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        initViews();
        startTrafficLightLoop();
    }
    
    private void initViews() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        mTrafficLightView = inflater.inflate(R.layout.view_traffic_light, null);
        mLightImageView = (ImageView) mTrafficLightView.findViewById(R.id.traffic_light);
        mCountdownTextView = (TextView) mTrafficLightView.findViewById(R.id.countdown_text);
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.x = 20;
        params.y = 100;
        
        mWindowManager.addView(mTrafficLightView, params);
        mTrafficLightView.setVisibility(View.GONE);
    }
    
    private void startTrafficLightLoop() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                updateTrafficLight();
                mHandler.postDelayed(this, 1000);
            }
        });
    }
    
    private void updateTrafficLight() {
        mCountdown--;
        
        if (mCountdown &lt;= 0) {
            // 切换红绿灯状态
            switch (mCurrentState) {
                case STATE_RED:
                    mCurrentState = STATE_GREEN;
                    mCountdown = 10;
                    break;
                case STATE_GREEN:
                    mCurrentState = STATE_YELLOW;
                    mCountdown = 3;
                    break;
                case STATE_YELLOW:
                    mCurrentState = STATE_RED;
                    mCountdown = 8;
                    break;
            }
        }
        
        updateUI();
    }
    
    private void updateUI() {
        if (mLightImageView != null &amp;&amp; mCountdownTextView != null) {
            // 更新灯光颜色
            switch (mCurrentState) {
                case STATE_RED:
                    mLightImageView.setImageResource(R.drawable.traffic_light_red);
                    mCountdownTextView.setTextColor(0xFFFF0000);
                    break;
                case STATE_YELLOW:
                    mLightImageView.setImageResource(R.drawable.traffic_light_yellow);
                    mCountdownTextView.setTextColor(0xFFFFFF00);
                    break;
                case STATE_GREEN:
                    mLightImageView.setImageResource(R.drawable.traffic_light_green);
                    mCountdownTextView.setTextColor(0xFF00FF00);
                    break;
            }
            
            mCountdownTextView.setText(String.valueOf(mCountdown));
        }
    }
    
    public void show() {
        if (!mIsShowing &amp;&amp; mTrafficLightView != null) {
            mTrafficLightView.setVisibility(View.VISIBLE);
            mIsShowing = true;
        }
    }
    
    public void hide() {
        if (mIsShowing &amp;&amp; mTrafficLightView != null) {
            mTrafficLightView.setVisibility(View.GONE);
            mIsShowing = false;
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTrafficLightView != null) {
            mWindowManager.removeView(mTrafficLightView);
        }
    }
}
