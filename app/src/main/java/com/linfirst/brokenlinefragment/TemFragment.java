package com.linfirst.brokenlinefragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linfirst.sockettest01.R;
import com.linfirst.temperatureview.BrokenLineAdapter;
import com.linfirst.until.AnalysisData;
import com.linfirst.until.MySocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.linfirst.until.Constant.TEM_HUM;
import static com.linfirst.until.MySocket.executorService;
import static com.linfirst.until.MySocket.sharedPreferences;
import static com.linfirst.until.MySocket.socket;


/**
 * @author linfirst
 */
public class TemFragment extends Fragment {
    View view;

    private RecyclerView recyclerView;
    List<Integer> data;
    BrokenLineAdapter adapter;
    int i = 0;

    //    MySocket mySocket ;
    AnalysisData analysisData;


    public TemFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tem, container, false);
        initRecyclerView();

//        if (mySocket==null){
//            mySocket = new MySocket(getContext());
//        }
        if (analysisData == null) {
            analysisData = new AnalysisData();
        }
//        recerver();
        getAllData();
        Log.e("Tem Tem Tem", "SSSSSSS");
        return view;
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.hum_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        data = new ArrayList<>();
        data.add(0);

        recyclerView.scrollToPosition(data.size() - 1);
        adapter = new BrokenLineAdapter(view.getContext(), data);
        recyclerView.setAdapter(adapter);
    }

    public void addTemData(String msg) {
        Log.e("Tem SSSSS", msg);
        if (adapter != null && recyclerView != null) {
            adapter.setData(Integer.valueOf(analysisData.getTem(msg)));
            recyclerView.scrollToPosition(data.size() - 1);
            recyclerView.setAdapter(adapter);
        }

    }


    public void recerver() {
        InputStream inputStream = null;
        try {
            if (socket != null) {
                inputStream = socket.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        final InputStream finalInputStream = inputStream;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (finalInputStream == null) {
                    return;
                }
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while (true) {
                    try {
                        //第三种
                        byte[] b = new byte[1024];
                        int length = finalInputStream.read(b);
                        String data = new String(b, 0, length, "utf-8");
                        Log.e("SSSSS 333333", data);
                        Message message = new Message();
                        message.what = 0;
                        message.obj = data;
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private void getAllData() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                String userId = sharedPreferences.getString("ALL_DATA", "0");
//                Log.e("DDDDDD Tem", userId);
//                Message message = new Message();
//                message.what = 0;
//                message.obj = message;
//                handler.sendMessage(message);
//            }
//        }, 1500);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        String data = sharedPreferences.getString("TEM_HUM_DATA", "0");
                        Message message = new Message();
                        message.what = 0;
                        message.obj=data;
                        handler.sendMessage(message);
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
                            addTemData(msg.obj + "");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
