package com.linfirst.until;

import android.util.Log;

public class AnalysisData {

    /**
     * 设备类型
     * @param msg
     * @return
     */
    public String getDeviceType(String msg){
        return msg.substring(3,5);
    }

    /**
     * 设备编号
     * @param msg
     * @return
     */
    public String getDeviceNumber(String msg){
        return msg.substring(5,7);
    }

    /**
     * 温度数据
     * @param msg
     * @return
     */
    public String getTem(String msg){
        return msg.substring(11,13);
    }

    /**
     * 湿度数据
     * @param msg
     * @return
     */
    public String getHum(String msg){
        return msg.substring(16,18);
    }

    /**
     *光照强度
     * @param msg
     * @return
     */
    public String getLig(String msg){
        return msg.substring(12,15);
    }

//    public String get
}
