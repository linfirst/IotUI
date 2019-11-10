package com.linfirst.temperatureview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.linfirst.activity.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BrokenLineAdapter extends RecyclerView.Adapter<BrokenLineAdapter.BrokenLineHolder> {

    private static final String TAG = "Hour_Adapter";
    private Context mContext;
    private List<Integer> data = new ArrayList<>();
    private int minValue;
    private int maxValue;

    private int DATA_MAX_SIZE=5;
    //初始化
    public BrokenLineAdapter(Context context, List<Integer> data){
        this.data.addAll(data);
        //将不规则的数据组从小到大排序
//        Collections.sort(this.data);
        minValue = Collections.min(this.data);
        maxValue = Collections.max(this.data);
        mContext = context;
    }

    //清除数据
    public void clearData(){
        data.clear();
    }
    //设置数据
    public void setData(Integer lastdata){
        if (data.size()>=DATA_MAX_SIZE){
            this.data.remove(0);
        }
        this.data.add(data.size(),lastdata);
        minValue = Collections.min(this.data);
        maxValue = Collections.max(this.data);
    }



    @NonNull
    @Override
    public BrokenLineHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hour_item, viewGroup,false);
        return new BrokenLineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrokenLineHolder brokenLineHolder, int i) {

        //如果是第一个
        if(i==0){
            brokenLineHolder.brokenLineView.setDrawLeftLine(false);
        }
        //除第一个以外
        else{
            brokenLineHolder.brokenLineView.setDrawLeftLine(true);
            brokenLineHolder.brokenLineView.setLastValue(data.get(i-1));
        }

        //如果是最后一个
        if(i == data.size()-1){
            brokenLineHolder.brokenLineView.setDrawRightLine(false);
        }
        //除最后一个以外
        else{
            brokenLineHolder.brokenLineView.setDrawRightLine(true);
            brokenLineHolder.brokenLineView.setNextValue(data.get(i+1));
        }

        brokenLineHolder.brokenLineView.setCurrentValue(data.get(i));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BrokenLineHolder extends RecyclerView.ViewHolder{

        private BrokenLineView brokenLineView;

        public BrokenLineHolder(@NonNull View itemView) {
            super(itemView);
            brokenLineView = itemView.findViewById(R.id.temp_view);
            brokenLineView.setMinValue(minValue);
            brokenLineView.setMaxValue(maxValue);
        }
    }

}