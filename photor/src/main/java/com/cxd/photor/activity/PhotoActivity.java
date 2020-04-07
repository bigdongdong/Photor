package com.cxd.photor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.PDataManager;
import com.cxd.photor.R;
import com.cxd.photor.activity.adapters.PhotoAdapter;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.Constant;
import com.cxd.photor.utils.DensityUtil;
import com.cxd.photor.activity.views.ConfirmView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * create by cxd on 2020/4/7
 */
public class PhotoActivity extends BaseActivity {

    private ImageView closeIV ;
    private TextView titleTV ;
    private ConfirmView confirmView ;
    private RecyclerView recycler ;


    private List<ImgBean> photoList ;
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
            titleTV.setText("选择图片");
        }else{
            closeIV.setImageResource(R.drawable.back_arrow);
            titleTV.setText("选择图片 - "+curBucketName);
        }

        photoList = (List<ImgBean>) this.getIntent().getSerializableExtra(Constant.INTENT_PHOTO_LIST);
        if(photoList == null){
            /*重新获取所有的图片*/
            photoList = PDataManager.getInstance().getImgs(context);
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

        adapter.update(PDataManager.getInstance().getSelectedImgs(),photoList);

        /*PDataManager.getInstance().getPhotoList() 不会为null*/
        confirmView.setCount(PDataManager.getInstance().getSelectedImgs().size(), PDataManager.getInstance().getLimit());
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

    /**
     * 从BucketActivity跳转过来，会传文件夹名和分类后的数据集合
     * @param context
     * @param bucketName  文件夹名
     * @param dataList  对应文件夹里的imgs数据集合
     */
    public static void jump(Context context ,String bucketName ,List<ImgBean> dataList){
        if(context != null){
            Intent intent = new Intent(context,PhotoActivity.class);
            intent.putExtra(Constant.INTENT_BUCKET_NAME,bucketName);
            intent.putExtra(Constant.INTENT_PHOTO_LIST, (Serializable) dataList);
            context.startActivity(intent);
        }
    }

    /**
     * 从Photor单独跳转，需要赋值limit
     * @param context
     * @param limit
     */
    public static void jump(Context context , int limit){
        PDataManager.getInstance().init(limit);
        if(context != null){
            Intent intent = new Intent(context,PhotoActivity.class);
            context.startActivity(intent);
        }
    }

    @EventBoxSubscribe
    public void onEvent(Integer event){
        if(event == null || confirmView == null || adapter == null){
            return;
        }

        switch(event){
            case Constant.EVENTBOX_UPDATE:
                confirmView.setCount(PDataManager.getInstance().getSelectedImgs().size(), PDataManager.getInstance().getLimit());
                adapter.update(PDataManager.getInstance().getSelectedImgs(),photoList);
                break;
            case Constant.EVENTBOX_EXCEED_LIMIT:
                Toast.makeText(context,"最多只可选择"+PDataManager.getInstance().getLimit()+"张！",Toast.LENGTH_SHORT).show();
                break;
            case Constant.EVENTBOX_COMMIT_FINISH:
                finish();
                break;
            default:
                break;
        }

    }
}
