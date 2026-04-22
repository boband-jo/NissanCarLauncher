
package com.nissan.carlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonReceiver extends BroadcastReceiver {
    
    private static long sLastClickTime = 0;
    private static int sClickCount = 0;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                handleMediaKey(context, event.getKeyCode());
            }
        }
    }
    
    private void handleMediaKey(Context context, int keyCode) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                handlePlayPause(context);
                break;
                
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                handlePause(context);
                break;
                
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                handleNext(context);
                break;
                
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                handlePrevious(context);
                break;
                
            case KeyEvent.KEYCODE_VOLUME_UP:
                adjustVolume(context, true);
                break;
                
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                adjustVolume(context, false);
                break;
                
            case KeyEvent.KEYCODE_CALL:
                makeCall(context);
                break;
                
            case KeyEvent.KEYCODE_ENDCALL:
                endCall(context);
                break;
        }
    }
    
    private void handlePlayPause(Context context) {
        // 发送播放/暂停广播给音乐应用
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "togglepause");
        context.sendBroadcast(intent);
        Toast.makeText(context, "播放/暂停", Toast.LENGTH_SHORT).show();
    }
    
    private void handlePause(Context context) {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "pause");
        context.sendBroadcast(intent);
        Toast.makeText(context, "暂停", Toast.LENGTH_SHORT).show();
    }
    
    private void handleNext(Context context) {
        Intent intent = new Intent("com.android.music.musicservicecommand");
        intent.putExtra("command", "next");
        context.sendBroadcast(intent);
        Toast.makeText(context, "下一曲", Toast.LENGTH_SHORT).show();
    }
    
    private void handlePrevious(Context context) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - sLastClickTime < 500) {
            sClickCount++;
            if (sClickCount >= 2) {
                // 双击上一曲
                Intent intent = new Intent("com.android.music.musicservicecommand");
                intent.putExtra("command", "prev");
                context.sendBroadcast(intent);
                Toast.makeText(context, "上一曲", Toast.LENGTH_SHORT).show();
                sClickCount = 0;
            }
        } else {
            sClickCount = 1;
        }
        sLastClickTime = currentTime;
    }
    
    private void adjustVolume(Context context, boolean up) {
        // 这里可以实现音量调整逻辑
        String msg = up ? "音量+" : "音量-";
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
    private void makeCall(Context context) {
        Toast.makeText(context, "拨打电话", Toast.LENGTH_SHORT).show();
    }
    
    private void endCall(Context context) {
        Toast.makeText(context, "挂断电话", Toast.LENGTH_SHORT).show();
    }
}
