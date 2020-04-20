package com.cxd.photor.activity.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cxd.photor.R;
import com.cxd.photor.utils.DensityUtil;

/**
 * create by cxd on 2020/4/16
 * 预览弹窗
 */
public class PreViewPop extends ShadowPopupWindow{

    private ImageView iv ;
    public PreViewPop(Activity context) {
        super(context);
        this.setWidth(getScreenWidth());
        this.setHeight(-1);
        this.setAnimationStyle(R.style.pop_animation);
    }

    @Override
    protected void onCreateView(View view) {
        iv = (ImageView) view;

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show(int layoutId , String url){
        Glide.with(context).load(url).into(iv);
        showCenteral(layoutId);
    }

    @Override
    protected long getAnimatorDuration() {
        return 400;
    }

    @Override
    protected float getAnimatorAlpha() {
        return 0.3f;
    }

    @Override
    protected Object getLayoutIdOrView() {
        ImageView iv = new ImageView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-1);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        iv.setPadding(0,DensityUtil.dp2px(context,50),0,DensityUtil.dp2px(context,50));
        return iv ;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
