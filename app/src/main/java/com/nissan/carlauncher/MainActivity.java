package com.nissan.carlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView mAppGridView;
    private List<AppInfo> mAppList;
    private AppAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 确保列表被初始化
        if (mAppList == null) {
            mAppList = new ArrayList<>();
        }

        initViews();
        initAppList();
        loadInstalledApps();
    }

    private void initViews() {
        mAppGridView = (GridView) findViewById(R.id.app_grid);
        mAdapter = new AppAdapter();
        mAppGridView.setAdapter(mAdapter);

        mAppGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo app = mAppList.get(position);
                if (app.packageName != null) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage(app.packageName);
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    }
                }
            }
        });
    }

    private void initAppList() {
        // 清空并添加默认应用
        mAppList.clear();
        addApp("导航", "com.autonavi.amapauto", null, R.drawable.ic_nav);
        addApp("蓝牙电话", "com.android.bluetooth", null, R.drawable.ic_phone);
        addApp("音乐", "com.android.music", null, R.drawable.ic_music);
        addApp("收音机", "com.android.fmradio", null, R.drawable.ic_radio);
        addApp("设置", "com.android.settings", null, R.drawable.ic_settings);
        addApp("天气", "com.moji.weather", null, R.drawable.ic_weather);
        addApp("CarLife", "com.baidu.carlifevehicle", null, R.drawable.ic_carlife);
        addApp("应用商店", "com.nissan.appstore", null, R.drawable.ic_appstore);
        mAdapter.notifyDataSetChanged();
    }

    private void loadInstalledApps() {
        PackageManager pm = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info : resolveInfos) {
            String packageName = info.activityInfo.packageName;
            // 避免重复添加已存在的默认应用
            boolean exists = false;
            for (AppInfo app : mAppList) {
                if (app.packageName != null && app.packageName.equals(packageName)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                String appName = info.loadLabel(pm).toString();
                addApp(appName, packageName, null, android.R.drawable.sym_def_app_icon);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void addApp(String name, String packageName, String className, int iconRes) {
        AppInfo app = new AppInfo();
        app.name = name;
        app.packageName = packageName;
        app.className = className;
        app.iconRes = iconRes;
        mAppList.add(app);
    }

    // 内部类 AppInfo
    private class AppInfo {
        String name;
        String packageName;
        String className;
        int iconRes;
    }

    // 适配器
    private class AppAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return (mAppList != null) ? mAppList.size() : 0;
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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_app, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
                holder.name = (TextView) convertView.findViewById(R.id.app_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppInfo app = mAppList.get(position);
            holder.name.setText(app.name);
            if (app.iconRes != 0) {
                holder.icon.setImageResource(app.iconRes);
            } else {
                holder.icon.setImageResource(android.R.drawable.sym_def_app_icon);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
        }
    }
}
