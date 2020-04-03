package com.cxd.photor.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cxd.photor.utils.DensityUtil;

public class ConfirmView extends android.support.v7.widget.AppCompatTextView {
    private Context context ;

    public ConfirmView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context ;

        this.setCount(0,1);
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



    private GradientDrawable getBackground(int count){
        GradientDrawable gd = new GradientDrawable();
        if(count == 0){
            gd.setColor(Color.parseColor("#434343"));
            gd.setCornerRadius(DensityUtil.dp2px(context,4));

        }else{
            gd.setColor(Color.parseColor("#05C25D"));
            gd.setCornerRadius(DensityUtil.dp2px(context,4));
        }
        return gd ;
    }
}
