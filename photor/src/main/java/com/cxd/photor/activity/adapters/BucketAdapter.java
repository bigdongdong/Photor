package com.cxd.photor.activity.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cxd.photor.R;
import com.cxd.photor.activity.PhotoActivity;
import com.cxd.photor.model.BucketBean;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.Constant;
import com.cxd.photor.utils.DensityUtil;
import com.cxd.photor.views.RoundImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketAdapter.ViewHolder> {
    private List<ImgBean> selectedList ; //选中的图片
    private List<BucketBean> beans ;
    private Context context ;

    public BucketAdapter(Context context) {
        this.context = context;
        beans = new ArrayList<>();
    }

    public void update(List<ImgBean> selectedList ,List<BucketBean> beans){
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bucket,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final BucketBean bean = beans.get(i);

        Glide.with(context).load(bean.getCoverUrl()).into(holder.coverRIV);
        holder.nameTV.setText(bean.getName());
        holder.countTV.setText(bean.getList().size() + "");

        /*显示当前选中的数量*/
        if(getPhotoInsideCount(bean.getName()) == 0){
            holder.photoCountTV.setVisibility(View.GONE);
            holder.photoCountTV.setText(null);
        }else{
            holder.photoCountTV.setVisibility(View.VISIBLE);
            holder.photoCountTV.setText(String.valueOf(getPhotoInsideCount(bean.getName())));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra(Constant.INTENT_DATA_LIST,(Serializable) bean.getList());
                intent.putExtra(Constant.INTENT_BUCKET_NAME,bean.getName());
                context.startActivity(intent);
            }
        });

        if(i == beans.size() - 1){
            holder.lineView.setVisibility(View.GONE);
        }else{
            holder.lineView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取当前选中在当前bucket的数量
     * @param bucketName
     * @return
     */
    private int getPhotoInsideCount(String bucketName){
        if(selectedList == null || bucketName == null){
           return 0;
        }

        int count = 0 ;
        for (ImgBean bean : selectedList) {
            if(bucketName.equals(bean.getBucketName())){
                count++;
            }
        }
        return count ;
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RoundImageView coverRIV ;
        private TextView nameTV , countTV ,photoCountTV;
        private View lineView ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coverRIV = itemView.findViewById(R.id.coverRIV);
            nameTV = itemView.findViewById(R.id.nameTV);
            countTV = itemView.findViewById(R.id.countTV);
            photoCountTV = itemView.findViewById(R.id.photoCountTV);
            lineView = itemView.findViewById(R.id.lineView);

            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(DensityUtil.dp2px(context,11.5f));
            gd.setColor(Color.parseColor("#05C25D"));
            photoCountTV.setBackground(gd);
        }
    }
}
