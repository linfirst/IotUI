package com.linfirst.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NavUtils;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.linfirst.sockettest01.R;
import com.linfirst.until.AnalysisData;
import com.linfirst.until.MySocket;

import org.w3c.dom.Text;

import java.text.MessageFormat;

import static com.linfirst.until.Constant.LIG;
import static com.linfirst.until.Constant.TEM_HUM;
import static com.linfirst.until.MySocket.executorService;
import static com.linfirst.until.MySocket.sharedPreferences;
import static com.linfirst.until.MySocket.socket;

/**
 * @author linfirst
 */
public class ControlFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {
    View view;

    private SwitchCompat switchFan;
    private SwitchCompat switchLock;
    private SeekBar seekBarLight;

    private MySocket mySocket;
    private AnalysisData analysisData;

    private TextView fanTextView;
    private TextView lockTextView;
    private TextView lightTextView;

    private TextView fanStatus;
    private TextView lockStatus;
    private TextView lightStatus;

    private TextView mLigNumber;
    private TextView lig_number01;



    public ControlFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control,container,false);
        switchFan=view.findViewById(R.id.switch_fan);
        switchLock=view.findViewById(R.id.switch_lock);

        seekBarLight=view.findViewById(R.id.seekBar_lamp);

        fanTextView=view.findViewById(R.id.fan_number);
        lockTextView=view.findViewById(R.id.lock_number);
        lightTextView=view.findViewById(R.id.tunable_number);

        fanStatus=view.findViewById(R.id.fan_status);
        lockStatus=view.findViewById(R.id.lock_status);
        lightStatus=view.findViewById(R.id.tunable_status);
        mLigNumber=view.findViewById(R.id.my_number);

        lig_number01=view.findViewById(R.id.lig_number01);

       getAllData();
        mySocket=new MySocket(getContext());
        analysisData=new AnalysisData();

        switchFan.setOnCheckedChangeListener(this);
        switchLock.setOnCheckedChangeListener(this);
        seekBarLight.setOnSeekBarChangeListener(this);

        return view;
    }

//    private void setTextNumber() {
//            String fanNumber=analysisData.getDeviceNumber(sharedPreferences.getString("FAN_DATA", "01"));
//
//
//            String lockNumber=analysisData.getDeviceNumber(sharedPreferences.getString("LOCK_DATA", "01"));
//            lockTextView.setText(lockNumber);
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_fan:
                String number=analysisData.getDeviceNumber(sharedPreferences.getString("FAN_DATA", "01"));
                Log.e("WWWWWWWWW", number);
                if (isChecked){
                    String order = MessageFormat.format("Hwcaf{0}02onT",number);
                    mySocket.send(order);
                    fanTextView.setText(number);
                    fanStatus.setText("开启");
                    Log.e("WWWWWWW fan","off off off");
                }else {
                    String order = MessageFormat.format("Hwcaf{0}03offT",number);
                    mySocket.send(order);
                    fanStatus.setText("关闭");
                    Log.e("WWWWWWW fan","on on on");
                }
                break;
                //
            case R.id.switch_lock:
                String lockNumber=analysisData.getDeviceNumber(sharedPreferences.getString("LOCK_DATA", "01"));
                Log.e("WWWWWWWWW", lockNumber);
                if (isChecked){
                    String order = MessageFormat.format("Hwcel{0}02onT",lockNumber);
                    mySocket.send(order);
                    lockTextView.setText(lockNumber);
                    lockStatus.setText("开启");
                    Log.e("WWWWWWW fan","off off off");
                }else {
                    String order = MessageFormat.format("Hwcel{0}03offT",lockNumber);
                    mySocket.send(order);
                    lockStatus.setText("关闭");
                    Log.e("WWWWWWW fan","on on on");
                }

                break;
                default:
                    break;
        }
    }

    /**
     * 可调灯
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String lampNumber=analysisData.getDeviceNumber(sharedPreferences.getString("LAMP_DATA","01"));
        String order="Hwcal"+lampNumber+"0300"+progress+"T";
        if (String.valueOf(progress).length()==1){
            order ="Hwcal"+lampNumber+"0300"+progress+"T";
        }

        if (String.valueOf(progress).length()==2){
            order ="Hwcal"+lampNumber+"030"+progress+"T";
        }

        if (String.valueOf(progress).length()==3){
            order ="Hwcal"+lampNumber+"03"+progress+"T";
        }

        Log.e("LLLLLL",order);

        lig_number01.setText(progress+"%");

        mySocket.send(order);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }



    private void getAllData() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);


                        String data = sharedPreferences.getString("FAN_DATA", "0");
                        Message message = new Message();
                        message.what = 0;
                        message.obj=data;
                        handler.sendMessage(message);

                        String data1 = sharedPreferences.getString("LIG_DATA", "0");
                        Message message1 = new Message();
                        message1.what = 1;
                        message1.obj=data1;
                        handler.sendMessage(message1);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    if (!("0".equals(String.valueOf(msg.obj)))) {
                        Log.e("DDDDDD Tem", String.valueOf(msg.obj));
                        if (TEM_HUM.equals(analysisData.getDeviceType(String.valueOf(msg.obj)))){

                        }
                    }
                    break;
                case 1:
                    if (LIG.equals(analysisData.getDeviceType(String.valueOf(msg.obj)))){
                        lightStatus.setText(analysisData.getLig(String.valueOf(msg.obj)));
                        mLigNumber.setText(String.valueOf(100-(Integer.valueOf(analysisData.getLig(String.valueOf(msg.obj))))/10)+"%");
//                        seekBarLight.setMax(Integer.valueOf(100-(Integer.valueOf(analysisData.getLig(String.valueOf(msg.obj))))/10));
                    }

                default:
                    break;
            }
        }
    };

}