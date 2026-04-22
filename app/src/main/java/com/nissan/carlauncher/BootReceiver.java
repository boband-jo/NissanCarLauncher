
package com.nissan.carlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // 开机自启动主界面
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
            
            // 启动服务
            Intent lyricsIntent = new Intent(context, BluetoothLyricsService.class);
            context.startService(lyricsIntent);
            
            Intent trafficIntent = new Intent(context, TrafficLightService.class);
            context.startService(trafficIntent);
            
            Intent dataIntent = new Intent(context, CarDataService.class);
            context.startService(dataIntent);
        }
    }
}
