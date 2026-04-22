
package com.nissan.carlauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    
    private GridView mAppGridView;
    private AppAdapter mAppAdapter;
    private List&lt;AppInfo&gt; mAppList;
    
    private CarDataService mCarDataService;
    private boolean mServiceBound = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initViews();
        initAppList();
        startServices();
    }
    
    private void initViews() {
        mAppGridView = (GridView) findViewById(R.id.app_grid);
        mAppAdapter = new AppAdapter();
        mAppGridView.setAdapter(mAppAdapter);
        
        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView&lt;?&gt; parent, View view, int position, long id) {
                AppInfo appInfo = mAppList.get(position);
                launchApp(appInfo.packageName, appInfo.activityName);
            }
        });
    }
    
    private void initAppList() {
        mAppList = new ArrayList&lt;&gt;();
        
        // 添加日产车机常用应用
        addApp("导航", "com.autonavi.amapauto", null, R.drawable.ic_nav);
        addApp("蓝牙电话", "com.android.bluetooth", null, R.drawable.ic_phone);
        addApp("音乐", "com.android.music", null, R.drawable.ic_music);
        addApp("收音机", "com.android.fmradio", null, R.drawable.ic_radio);
        addApp("设置", "com.android.settings", null, R.drawable.ic_settings);
        addApp("天气", "com.moji.weather", null, R.drawable.ic_weather);
        addApp("CarLife", "com.baidu.carlifevehicle", null, R.drawable.ic_carlife);
        addApp("应用商店", "com.nissan.appstore", null, R.drawable.ic_appstore);
        
        mAppAdapter.notifyDataSetChanged();
    }
    
    private void addApp(String name, String packageName, String activityName, int iconRes) {
        AppInfo appInfo = new AppInfo();
        appInfo.name = name;
        appInfo.packageName = packageName;
        appInfo.activityName = activityName;
        appInfo.iconRes = iconRes;
        mAppList.add(appInfo);
    }
    
    private void launchApp(String packageName, String activityName) {
        try {
            Intent intent;
            if (activityName != null) {
                intent = new Intent();
                intent.setComponent(new ComponentName(packageName, activityName));
            } else {
                intent = getPackageManager().getLaunchIntentForPackage(packageName);
            }
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "应用未安装: " + packageName, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void startServices() {
        // 启动蓝牙歌词服务
        Intent lyricsIntent = new Intent(this, BluetoothLyricsService.class);
        startService(lyricsIntent);
        
        // 启动车机数据服务
        Intent dataIntent = new Intent(this, CarDataService.class);
        bindService(dataIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        
        // 启动红绿灯服务
        Intent trafficIntent = new Intent(this, TrafficLightService.class);
        startService(trafficIntent);
    }
    
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CarDataService.LocalBinder binder = (CarDataService.LocalBinder) service;
            mCarDataService = binder.getService();
            mServiceBound = true;
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 处理方控按键
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_NEXT:
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                // 转发给媒体按钮接收器
                Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                intent.putExtra(Intent.EXTRA_KEY_EVENT, event);
                sendBroadcast(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }
    
    private static class AppInfo {
        String name;
        String packageName;
        String activityName;
        int iconRes;
    }
    
    private class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mAppList.size();
        }
        
        @Override
        public Object getItem(int position) {
            return mAppList.get(position);
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_app, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
                holder.name = (TextView) convertView.findViewById(R.id.app_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            AppInfo appInfo = mAppList.get(position);
            holder.icon.setImageResource(appInfo.iconRes);
            holder.name.setText(appInfo.name);
            
            return convertView;
        }
    }
    
    private static class ViewHolder {
        ImageView icon;
        TextView name;
    }
}
