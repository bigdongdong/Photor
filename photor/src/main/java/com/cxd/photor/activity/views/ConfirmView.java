package com.cxd.photor.activity.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.cxd.photor.PDataManager;
import com.cxd.photor.utils.DensityUtil;


/**
 * create by cxd on 2020/4/7
 */
public class ConfirmView extends androidx.appcompat.widget.AppCompatTextView {
    private Context context ;

    public ConfirmView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context ;

        this.setCount(0,1);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PDataManager.getInstance().commit();
            }
        });
    }

    /**
     * 设置数量
     * @param count
     */
    public void setCount(int count , int total){
        GradientDrawable gd = new GradientDrawable();
        if(count == 0){
            this.setEnabled(false);
            gd.setColor(Color.parseColor("#434343"));
            gd.setCornerRadius(DensityUtil.dp2px(context,4));
            this.setTextColor(Color.parseColor("#686868"));
            this.setText("完成");
        }else{
            this.setEnabled(true);
            gd.setColor(Color.parseColor("#05C25D"));
            gd.setCornerRadius(DensityUtil.dp2px(context,4));
            this.setTextColor(Color.WHITE);
            this.setText("完成("+count+"/"+total+")");
        }
        this.setBackground(gd);
    }
}
