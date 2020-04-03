package com.cxd.photor.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.R;
import com.cxd.photor.activity.adapters.BucketAdapter;
import com.cxd.photor.PhotoManager;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.views.ConfirmView;

import java.util.ArrayList;

public class BucketActivity extends BaseActivity {

    private ImageView closeIV ;
    private TextView titleTV ;
    private ConfirmView confirmView ;
    private RecyclerView recycler ;

    private BucketAdapter adapter ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bucket;
    }

    @Override
    protected void setViews() {
        closeIV = findViewById(R.id.closeIV);
        titleTV = findViewById(R.id.titleTV);
        titleTV.setText("选择图片");
        confirmView = findViewById(R.id.confirmView);
        recycler = findViewById(R.id.recycler);
    }

    @Override
    protected void initialize() {
        PhotoManager.getInstance().init(9);

        recycler.setLayoutManager(new LinearLayoutManager(context,LinearLayout.VERTICAL,false));
        adapter = new BucketAdapter(context);
        recycler.setAdapter(adapter);

        adapter.update(null,PhotoManager.getInstance().getBuckets(context));
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
        Log.i("aaa", "BucketActivity: onEventUpdate" + photoList.size());

        confirmView.setCount(photoList.size(),PhotoManager.getInstance().getTotalCount());
        adapter.update(PhotoManager.getInstance().getSelectedList(),PhotoManager.getInstance().getBuckets(context));
    }
}
