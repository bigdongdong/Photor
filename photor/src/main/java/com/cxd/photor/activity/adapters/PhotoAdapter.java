package com.cxd.photor.activity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxd.photor.R;
import com.cxd.photor.PhotoManager;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<ImgBean> selectedList ; //选中的图片
    private List<ImgBean> beans ;
    private Context context ;

    private final int vw ;

    public PhotoAdapter(Context context) {
        this.context = context;
        beans = new ArrayList<>();
        vw = ( DensityUtil.getScreenWidth(context) - 3 * DensityUtil.dp2px(context,3) ) / 4 ;
    }

    public void update(List<ImgBean> selectedList , List<ImgBean> beans){
        this.selectedList = selectedList ;
        if(beans != null){
            this.beans.clear();
            this.beans.addAll(beans);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup vp = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item_photo,null,false);
        RelativeLayout rootRL = vp.findViewById(R.id.rootRL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(vw,vw);
        rootRL.setLayoutParams(params);

        return new ViewHolder(vp);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final ImgBean bean = beans.get(i);
        GradientDrawable gd ;
        final int index = indexOf(selectedList,bean);
        if(selectedList != null && index != -1){
            /*当前选中*/
            gd = new GradientDrawable();
            gd.setColor(Color.parseColor("#05C25D"));
            gd.setCornerRadius(DensityUtil.dp2px(context,10));
            holder.tv.setBackground(gd);
            int position = index + 1 ; //当前是第几个
            holder.tv.setText(String.valueOf(position));
        }else{
            /*未选中*/
            gd = new GradientDrawable();
            gd.setColor(Color.parseColor("#11111111"));
            gd.setStroke(DensityUtil.dp2px(context,2),Color.WHITE);
            gd.setCornerRadius(DensityUtil.dp2px(context,10));
            holder.tv.setBackground(gd);
            holder.tv.setText(null);
        }
        Glide.with(context).load(bean.getUrl()).into(holder.iv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index != -1){
                    PhotoManager.getInstance().removeImg(bean);
                }else{
                    PhotoManager.getInstance().addImg(bean);
                }
            }
        });

    }

    private int indexOf(List<ImgBean> selectedList , ImgBean bean){
        for (int i = 0; i < selectedList.size(); i++) {
            ImgBean temp = selectedList.get(i);
            if(temp.getUrl().equals(bean.getUrl())){
                return i ;
            }
        }
        return -1 ;
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv ;
        private TextView tv ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}