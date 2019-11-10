package com.linfirst.until;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;



public class MyHandler {
    public void toast(int number, final Context context){
        android.os.Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        Toast.makeText(context, "未连接", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
