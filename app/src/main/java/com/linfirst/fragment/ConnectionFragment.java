package com.linfirst.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.linfirst.activity.R;
import com.linfirst.until.MyHandler;
import com.linfirst.until.MySocket;

/**
 * @author linfirst
 */
public class ConnectionFragment extends Fragment implements View.OnClickListener, MySocket.OnConnectionListener {
    View view;
    MySocket mySocket;
    Button connection;

    public ConnectionFragment() {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_connection,container,false);
        if (mySocket==null){
            mySocket=new MySocket(getContext());
        }
        connection=view.findViewById(R.id.connection);
        connection.setOnClickListener(this);
        mySocket.setOnConnectionListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.connection:
                mySocket.initSocket("192.168.1.200",6004);
                break;
                default:
                    break;
        }
    }


    /**
     * 是否连接成功
     */
    @Override
    public void succeed() {
       Message message = new Message();
       message.what=1;
       handler.sendMessage(message);
       mySocket.recerver();
    }

    @Override
    public void failure() {
        Message message = new Message();
        message.what=2;
        handler.sendMessage(message);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getContext(), "未连接", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}