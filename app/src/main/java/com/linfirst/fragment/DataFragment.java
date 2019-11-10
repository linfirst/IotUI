package com.linfirst.fragment;

import android.content.Context;
import android.icu.util.ValueIterator;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.linfirst.brokenlinefragment.HumFragment;
import com.linfirst.brokenlinefragment.LigFragment;
import com.linfirst.brokenlinefragment.TemFragment;
import com.linfirst.customview.AddReductionView;
import com.linfirst.sockettest01.R;
import com.linfirst.until.AnalysisData;
import com.linfirst.until.MySocket;

import java.io.IOException;
import java.io.InputStream;


import static com.linfirst.until.Constant.FAN_NUM;
import static com.linfirst.until.Constant.HUMER_NUM;
import static com.linfirst.until.Constant.HUM_NUM;
import static com.linfirst.until.Constant.LAM_NUM;
import static com.linfirst.until.Constant.LIG;
import static com.linfirst.until.Constant.LIG_NUM;
import static com.linfirst.until.Constant.TEM_NUM;
import static com.linfirst.until.MySocket.editor;
import static com.linfirst.until.MySocket.executorService;
import static com.linfirst.until.MySocket.sharedPreferences;
import static com.linfirst.until.MySocket.socket;


/**
 * @author linfirst
 */
public class DataFragment extends Fragment {



    AddReductionView addReductionViewTem;
    AddReductionView addReductionViewFan;
    AddReductionView addReductionViewHum;
    AddReductionView addReductionViewHumidifier;
    AddReductionView addReductionViewLight;
    AddReductionView addReductionViewLamp;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    MySocket mySocket;
    AnalysisData analysisData;


    private TextView temp_number,humidity_number,lig_number;

    private int tem_num,fan_num,hum_num,humidifier_num,light_num,lamp_num;

    View view;
    public DataFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data,container,false);
        temp_number=view.findViewById(R.id.temp_number);
        humidity_number=view.findViewById(R.id.humidity_number);
        lig_number=view.findViewById(R.id.lig_number);

        mySocket=new MySocket(getContext());
        analysisData=new AnalysisData();

//        setInitNumber();
        initAddRedView();
        getAllData();
//        addBrokenLineFragment();
        initFragment();

//        recerver();

        return view;
    }
//
//    private void setInitNumber() {
////        addReductionViewTem.setNumber();
////        addReductionViewFan.setNumber();
////        addReductionViewHum.setNumber();
////        addReductionViewHumidifier.setNumber();
//        if (sharedPreferences!=null){
//            addReductionViewLight.setNumber(sharedPreferences.getInt(LIG_NUM, 200));
//            addReductionViewLamp.setNumber(sharedPreferences.getInt(LIG_NUM, 200));
//        }
//
//    }

//    private void setTextViewTem() {
//        Log.e("DataF SSSS","123123");
//     mySocket.setOnReceiveListener(new MySocket.OnReceiveListener() {
//         @Override
//         public void receive(String msg) {
//             Log.e("DataF SSSS","111111");
//             textViewTem.setText(analysisData.getTem(msg));
//         }
//
//         @Override
//         public void failure() {
//
//         }
//     });
//    }


    private void initFragment() {
        tabLayout = view.findViewById(R.id.tab_layout);
        //appBarLayout = findViewById(R.id.appbarid);
        viewPager = view.findViewById(R.id.viewpage);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.AddFragment(new TemFragment(),"");
        adapter.AddFragment(new HumFragment(),"");
        adapter.AddFragment(new LigFragment(),"");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }



    private void initAddRedView(){
        addReductionViewTem= view.findViewById(R.id.tem_add_re);
        addReductionViewFan=view.findViewById(R.id.tem_add_re_fan);
        addReductionViewHum=view.findViewById(R.id.hum_add_re);
        addReductionViewHumidifier=view.findViewById(R.id.hum_add_re_humer);
        addReductionViewLight=view.findViewById(R.id.lig_add_re);
        addReductionViewLamp=view.findViewById(R.id.lig_add_re_lamp);

        setListener();
    }




    private void setListener() {
        //报警温度
        addReductionViewTem.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewTem.setNumber(sharedPreferences.getInt(TEM_NUM, 25)+1);
                editor.putInt(TEM_NUM, Integer.valueOf(addReductionViewTem.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewTem.setNumber(sharedPreferences.getInt(FAN_NUM, 25)+1);
                editor.putInt(FAN_NUM, Integer.valueOf(addReductionViewTem.getNumber()));
                editor.commit();
            }
        });

        //打开风扇
        addReductionViewFan.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewFan.setNumber(addReductionViewFan.getNumber()+1);
                editor.putInt(FAN_NUM, Integer.valueOf(addReductionViewFan.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewFan.setNumber(addReductionViewFan.getNumber()-1);
                editor.putInt(FAN_NUM, Integer.valueOf(addReductionViewFan.getNumber()));
                editor.commit();
            }
        });

        //湿度报警
        addReductionViewHum.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewHum.
                        setNumber(addReductionViewHum.getNumber()+1);
                editor.putInt(HUM_NUM, Integer.valueOf(addReductionViewHum.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewHum.
                        setNumber(addReductionViewHum.getNumber()-1);
                editor.putInt(HUM_NUM, Integer.valueOf(addReductionViewHum.getNumber()));
                editor.commit();
            }
        });

        //加湿器
        addReductionViewHumidifier.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewHumidifier.
                        setNumber(addReductionViewHumidifier.getNumber()+1);
                editor.putInt(HUMER_NUM, Integer.valueOf(addReductionViewHumidifier.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewHumidifier.
                        setNumber(addReductionViewHumidifier.getNumber()-1);
                editor.putInt(HUMER_NUM, Integer.valueOf(addReductionViewHumidifier.getNumber()));
                editor.commit();
            }
        });

        //亮度上限
        addReductionViewLight.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewLight.
                        setNumber(sharedPreferences.getInt(LIG_NUM, 200)+1);
                editor.putInt(LIG_NUM, Integer.valueOf(addReductionViewLight.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewLight.
                        setNumber(sharedPreferences.getInt(LIG_NUM, 200)-1);
                editor.putInt(LIG_NUM, Integer.valueOf(addReductionViewLight.getNumber()));
                editor.commit();
            }
        });

        //亮度下限
        addReductionViewLamp.setOnAddDelClickListener(new AddReductionView.OnAddRedClickListener() {
            @Override
            public void onAddClick(View v) {
                addReductionViewLamp.
                        setNumber(sharedPreferences.getInt(LAM_NUM, 200)+1);
                editor.putInt(LAM_NUM, Integer.valueOf(addReductionViewLamp.getNumber()));
                editor.commit();
            }

            @Override
            public void onRedClick(View v) {
                addReductionViewLamp.
                        setNumber(sharedPreferences.getInt(LAM_NUM, 200)-1);
                editor.putInt(LAM_NUM, Integer.valueOf(addReductionViewLamp.getNumber()));
                editor.commit();
            }
        });

    }


    private void getAllData() {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        String data1 = sharedPreferences.getString("TEM_HUM_DATA", "0");

                        Message message = new Message();
                        message.what = 0;
                        message.obj=data1;
                        handler.sendMessage(message);

                        Message message2 = new Message();
                        String data2 = sharedPreferences.getString("LIG_DATA", "0");
                        message2.what = 1;
                        message2.obj=data2;
                        handler.sendMessage(message2);

                        Message message3 = new Message();
                        message3.what = 2;
                        handler.sendMessage(message3);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }




    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    if ("he".equals(analysisData.getDeviceType(String.valueOf(msg.obj)))){
                        temp_number.setText(analysisData.getTem(msg.obj+""));
                        humidity_number.setText(analysisData.getHum(msg.obj+""));
                    }
                    break;
                case 1:
                    if (LIG.equals(analysisData.getDeviceType(String.valueOf(msg.obj)))){
                        lig_number.setText(analysisData.getLig(String.valueOf(msg.obj)));
                    }
                    int max=sharedPreferences.getInt(LIG_NUM, 200);
                    int min=sharedPreferences.getInt(LAM_NUM, 200);
                    Log.e("AAAAAAA 0000","MAX:"+max+"  "+"MIN"+min);
                    if (mySocket==null){
                        break;
                    }
                    if (Integer.valueOf(analysisData.getLig(String.valueOf(msg.obj)))>max){
                        String lampNumber=analysisData.getDeviceNumber(sharedPreferences.getString("LAMP_DATA","01"));
                        String order="Hwcal"+lampNumber+"03"+000+"T";
                        Log.e("AAAAAAA 11111",order);
                        mySocket.send("Hwcal1103000T");
                    }

                    if (Integer.valueOf(analysisData.getLig(String.valueOf(msg.obj)))<min){
                        String lampNumber=analysisData.getDeviceNumber(sharedPreferences.getString("LAMP_DATA","01"));
                        String order="Hwcal"+lampNumber+"03"+100+"T";
                        Log.e("AAAAAAA 2222",order);
                        mySocket.send(order);
                    }
                    break;
                case 2:


                default:
                    break;
            }
        }
    };






}
