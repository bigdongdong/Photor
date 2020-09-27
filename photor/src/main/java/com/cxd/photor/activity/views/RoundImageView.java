package com.cxd.photor.activity.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * create by cxd on 2020/4/7
 * 圆角ImageView
 */
public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {
    //圆角弧度
    private float[] rids = {dip2px(8), dip2px(8), dip2px(8), dip2px(8),
            dip2px(8), dip2px(8), dip2px(8), dip2px(8)};

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        //绘制圆角imageview
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    private int dip2px(int dipVal) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipVal * scale + 0.5f);
    }
}
