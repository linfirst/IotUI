package com.linfirst.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.linfirst.fragment.ConnectionFragment;
import com.linfirst.fragment.ControlFragment;
import com.linfirst.fragment.DataFragment;
import com.linfirst.fragment.ViewPagerAdapter;
import com.linfirst.until.MySocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    public static MySocket mySocket;
    TextView textView;

    private TabLayout tabLayout;
    private ViewPager viewPager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //覆盖状态栏背景//设为透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mySocket=new MySocket(getApplicationContext());

        textView = findViewById(R.id.text);
        initFragment();

    }

    private void initFragment() {
        tabLayout = findViewById(R.id.tablayout);
        //appBarLayout = findViewById(R.id.appbarid);
        viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.AddFragment(new DataFragment(),"数据");
        adapter.AddFragment(new ControlFragment(),"控制");
        adapter.AddFragment(new ConnectionFragment(),"连接");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(MainActivity.this, "未连接", Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    textView.setText(msg.obj.toString());
                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }
    };


}
