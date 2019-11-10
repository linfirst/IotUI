package com.linfirst.until;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.linfirst.until.Constant.FAN;
import static com.linfirst.until.Constant.LAMP;
import static com.linfirst.until.Constant.LIG;
import static com.linfirst.until.Constant.LOCK;
import static com.linfirst.until.Constant.TEM_HUM;

public class MySocket {
    public static ExecutorService executorService;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private AnalysisData analysisData=new AnalysisData();


    public static Socket socket;
    Context context;

    InputStream inputStream;
    OutputStream outputStream;
    MyHandler myHandler=new MyHandler();

    OnConnectionListener onConnectionListener;
    OnReceiveListener onReceiveListener;

    public MySocket(Context context) {
        if (executorService==null){
            executorService = Executors.newCachedThreadPool();
        }
        this.context=context;

        sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void initSocket(final String ip, final int port) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                        socket = new Socket(ip,port);
//                        SocketAddress socketAddress = new InetSocketAddress(ip, port);
//                        socket.connect(socketAddress, 2000);
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        Log.e("SSSSSS 111", "连接成功");
                        onConnectionListener.succeed();
                    } catch(IOException e){
                        e.printStackTrace();
                        if (onConnectionListener != null) {
                            onConnectionListener.failure();
                        }
                    }
                }
        };
        executorService.execute(runnable);
    }



    public void send(final String messege) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {

//                PrintWriter printWriter = new PrintWriter(outputStream, true);
//                printWriter.print(messege);
//                printWriter.flush();

                //第二种

                try {
                    if (socket != null) {
                        outputStream=socket.getOutputStream();
                    }
                    // 步骤1：从Socket 获得输出流对象OutputStream
                    // 该对象作用：发送数据
//                    outputStream = socket.getOutputStream();
                    // 步骤2：写入需要发送的数据到输出流对象中
                    if (outputStream==null){
                        return;
                    }
                    outputStream.write(messege.getBytes("utf-8"));
                    // 步骤3：发送数据到服务端
                    outputStream.flush();
                    Log.e("DDDDDDDDDD 发送数据成功",messege);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void recerver() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (inputStream == null) {
                    myHandler.toast(0,context);
                    return;
                }
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    try {
                        //第三种
                        byte[] b = new byte[1024];
                        int length = inputStream.read(b);
                        String data = new String(b, 0, length, "utf-8");
                        Log.e("SSSSS 333333", data);

                        if (TEM_HUM.equals(analysisData.getDeviceType(data))){
                            editor.putString("TEM_HUM_DATA", data);
                            editor.commit();
                        }
                       if (LIG.equals(analysisData.getDeviceType(data))){
                           editor.putString("LIG_DATA", data);
                           editor.commit();
                       }
                       if (LAMP.equals(analysisData.getDeviceType(data))){
                           editor.putString("LAMP_DATA", data);
                           editor.commit();
                       }
                        if (LOCK.equals(analysisData.getDeviceType(data))){
                            editor.putString("LOCK_DATA", data);
                            editor.commit();
                        }
                        if (FAN.equals(analysisData.getDeviceType(data))){
                            editor.putString("FAN_DATA", data);
                            editor.commit();
                        }
                        if (LAMP.equals(analysisData.getDeviceType(data))){
                            editor.putString("LAMP_DATA", data);
                            editor.commit();
                        }
//                        String userId = sharedPreferences.getString("ALL_DATA", "25");
//                        Log.e("DDDDDD MySocket", userId);
//                        if (onReceiveListener!=null) {
//                            Log.e("SSSSS 6666666", data);
//                          onReceiveListener.receive(data);
//                        }
                    } catch (IOException e) {
//                        if (onReceiveListener!=null){
//                            onReceiveListener.failure();
//                        }
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 是否连接成功回调接口
     */
    public interface OnConnectionListener{
        void succeed();
        void failure();
    }

    public void setOnConnectionListener(OnConnectionListener onConnectionListener){
        this.onConnectionListener=onConnectionListener;
    }

    /**
     * 消息回调接口
     */
    public interface OnReceiveListener{
        void receive(String msg);
        void failure();
    }
    public void setOnReceiveListener(OnReceiveListener onReceiveListener){
        this.onReceiveListener=onReceiveListener;
    }

}
