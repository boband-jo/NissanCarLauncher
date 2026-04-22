
package com.nissan.carlauncher;

import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothLyricsService extends Service {
    
    private static final String TAG = "BluetoothLyricsService";
    
    private WindowManager mWindowManager;
    private View mLyricsView;
    private TextView mLyricsTextView;
    
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothA2dp mBluetoothA2dp;
    private boolean mIsA2dpConnected = false;
    
    private AudioManager mAudioManager;
    
    // 模拟歌词数据
    private List&lt;String&gt; mLyricsList;
    private int mCurrentLyricIndex = 0;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        initViews();
        initBluetooth();
        initAudioManager();
        initLyricsData();
    }
    
    private void initViews() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        mLyricsView = inflater.inflate(R.layout.view_lyrics, null);
        mLyricsTextView = (TextView) mLyricsView.findViewById(R.id.lyrics_text);
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | 
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                android.graphics.PixelFormat.TRANSLUCENT);
        
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.y = 100;
        
        mWindowManager.addView(mLyricsView, params);
        mLyricsView.setVisibility(View.GONE);
    }
    
    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.A2DP);
            
            IntentFilter filter = new IntentFilter();
            filter.addAction(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(mBluetoothReceiver, filter);
        }
    }
    
    private void initAudioManager() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioManager.OnAudioFocusChangeListener focusChangeListener = 
                new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    // 获得音频焦点，显示歌词
                    showLyrics();
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    // 失去音频焦点，隐藏歌词
                    hideLyrics();
                }
            }
        };
    }
    
    private void initLyricsData() {
        mLyricsList = new ArrayList&lt;&gt;();
        mLyricsList.add("正在播放音乐...");
        mLyricsList.add("请连接蓝牙设备");
        mLyricsList.add("歌词显示中...");
    }
    
    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.A2DP) {
                mBluetoothA2dp = (BluetoothA2dp) proxy;
                checkA2dpConnection();
            }
        }
        
        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.A2DP) {
                mBluetoothA2dp = null;
                mIsA2dpConnected = false;
            }
        }
    };
    
    private void checkA2dpConnection() {
        if (mBluetoothA2dp != null) {
            List&lt;BluetoothDevice&gt; devices = mBluetoothA2dp.getConnectedDevices();
            mIsA2dpConnected = !devices.isEmpty();
            if (mIsA2dpConnected) {
                showLyrics();
            }
        }
    }
    
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1);
                if (state == BluetoothA2dp.STATE_PLAYING) {
                    showLyrics();
                    startLyricsScroll();
                } else {
                    hideLyrics();
                }
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                mIsA2dpConnected = true;
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                mIsA2dpConnected = false;
                hideLyrics();
            }
        }
    };
    
    private void showLyrics() {
        if (mLyricsView != null &amp;&amp; mLyricsView.getVisibility() != View.VISIBLE) {
            mLyricsView.setVisibility(View.VISIBLE);
            updateLyrics("蓝牙音乐已连接");
        }
    }
    
    private void hideLyrics() {
        if (mLyricsView != null &amp;&amp; mLyricsView.getVisibility() == View.VISIBLE) {
            mLyricsView.setVisibility(View.GONE);
        }
    }
    
    private void updateLyrics(String text) {
        if (mLyricsTextView != null) {
            mLyricsTextView.setText(text);
        }
    }
    
    private void startLyricsScroll() {
        // 模拟歌词滚动
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsA2dpConnected) {
                    try {
                        Thread.sleep(3000);
                        mCurrentLyricIndex++;
                        if (mCurrentLyricIndex &gt;= mLyricsList.size()) {
                            mCurrentLyricIndex = 0;
                        }
                        final String lyric = mLyricsList.get(mCurrentLyricIndex);
                        
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateLyrics(lyric);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    private void runOnUiThread(Runnable runnable) {
        android.os.Handler handler = new android.os.Handler(getMainLooper());
        handler.post(runnable);
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLyricsView != null) {
            mWindowManager.removeView(mLyricsView);
        }
        unregisterReceiver(mBluetoothReceiver);
        if (mBluetoothAdapter != null &amp;&amp; mBluetoothA2dp != null) {
            mBluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, mBluetoothA2dp);
        }
    }
}
