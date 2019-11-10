package com.linfirst.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linfirst.sockettest01.R;

public class AddReductionView extends LinearLayout  {

    private OnAddRedClickListener listener;
    private EditText editText;
    /**
     * // 是否需要删除末尾
     */
    boolean deleteLastChar;

    public void setOnAddDelClickListener(OnAddRedClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.toString().contains(".")) {
//            // 如果点后面有超过三位数值,则删掉最后一位
//            int length = s.length() - s.toString().lastIndexOf(".");
//            // 说明后面有三位数值
//            deleteLastChar = length >= 4;
//        }
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        if (s == null) {
//            return;
//        }
//        if (deleteLastChar) {
//            // 设置新的截取的字符串
//            editText.setText(s.toString().substring(0, s.toString().length() - 1));
//            // 光标强制到末尾
//            editText.setSelection(editText.getText().length());
//        }
//        // 以小数点开头，前面自动加上 "0"
//        if (s.toString().startsWith(".")) {
//            editText.setText("0" + s);
//            editText.setSelection(editText.getText().length());
//        }
//    }


    public interface OnAddRedClickListener{
        void onAddClick(View v);
        void onRedClick(View v);
    }

    public AddReductionView(Context context) {
        this(context,null);
    }

    public AddReductionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AddReductionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs,defStyleAttr);
    }
    private void initView(Context context,AttributeSet attrs,int defStyleAttr){
        View.inflate(context, R.layout.layout_add_delete,this);
        Button but_add = findViewById(R.id.but_add);
        Button but_delete = findViewById(R.id.but_delete);
        TextView textView=findViewById(R.id.textView);
        editText = findViewById(R.id.et_number);
        //光标放在末尾
        editText.setSelection(editText.getText().length());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddReductionViewStyle);
        String leftText = typedArray.getString(R.styleable.AddReductionViewStyle_left_text);
        String middleText = typedArray.getString(R.styleable.AddReductionViewStyle_middle_text);
        String unitText=typedArray.getString(R.styleable.AddReductionViewStyle_unit_text);
        String rightText = typedArray.getString(R.styleable.AddReductionViewStyle_right_text);
        but_delete.setText(leftText);
        editText.setText(middleText);
        textView.setText(unitText);
        but_add.setText(rightText);
        //释放资源
        typedArray.recycle();

        but_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener==null){
                    return;
                }
                listener.onAddClick(view);
            }
        });
        but_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener==null){
                    return;
                }
                listener.onRedClick(view);
            }
        });


//        editText.addTextChangedListener(this);
    }
    /**
     * 对外提供设置EditText值的方法
     */
    public void setNumber(int number){
//        if (number>0){
            editText.setText(number+"");
            editText.setSelection(editText.getText().length());
//        }
    }
    /**
     * 得到控件原来的值
     */
    public int getNumber(){
        int number;
        try {
            String numberStr = editText.getText().toString().trim();
            number = Integer.valueOf(numberStr);
        } catch (Exception e) {
            number = 0;
        }
        return number;
    }




}
