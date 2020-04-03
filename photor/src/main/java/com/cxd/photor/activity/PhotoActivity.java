package com.cxd.photor.activity;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.R;
import com.cxd.photor.activity.adapters.PhotoAdapter;
import com.cxd.photor.PhotoManager;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.Constant;
import com.cxd.photor.utils.DensityUtil;
import com.cxd.photor.views.ConfirmView;

import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends BaseActivity {

    private ImageView closeIV ;
    private TextView titleTV ;
    private ConfirmView confirmView ;
    private RecyclerView recycler ;


    private List<ImgBean> curDataList ;
    private String curBucketName ;

    private PhotoAdapter adapter ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void setViews() {
        closeIV = findViewById(R.id.closeIV);
        titleTV = findViewById(R.id.titleTV);
        confirmView = findViewById(R.id.confirmView);
        recycler = findViewById(R.id.recycler);
    }

    @Override
    protected void initialize() {
        curBucketName = getIntent().getStringExtra(Constant.INTENT_BUCKET_NAME);
        if(curBucketName == null){
            closeIV.setImageResource(R.drawable.toolbar_close_icon);
        }else{
            closeIV.setImageResource(R.drawable.back_arrow);
        }
        if(curBucketName != null){
            titleTV.setText("选择图片 - "+curBucketName);
        }else{
            PhotoManager.getInstance().init(9);
            titleTV.setText("选择图片");
        }

        curDataList = (List<ImgBean>) this.getIntent().getSerializableExtra(Constant.INTENT_DATA_LIST);
        if(curDataList == null){
            /*重新获取所有的图片*/
            curDataList = PhotoManager.getInstance().getAllImgs(context);
        }

        recycler.setLayoutManager(new GridLayoutManager(context,4));
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = DensityUtil.dp2px(context,3);
            }
        });
        adapter = new PhotoAdapter(context);
        recycler.setAdapter(adapter);

        adapter.update(PhotoManager.getInstance().getSelectedList(),curDataList);


        /*PhotoManager.getInstance().getPhotoList() 不会为null*/
        confirmView.setCount(PhotoManager.getInstance().getSelectedList().size(),PhotoManager.getInstance().getTotalCount());
    }

    @Override
    protected void setListeners() {
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
    }

    @EventBoxSubscribe
    public void onEventUpdate(ArrayList<ImgBean> photoList){
        if(photoList == null){
            return;
        }
        Log.i("aaa", "PhotoActivity: onEventUpdate" + photoList.size());

        confirmView.setCount(photoList.size(),PhotoManager.getInstance().getTotalCount());
        adapter.update(PhotoManager.getInstance().getSelectedList(),curDataList);
    }
}
