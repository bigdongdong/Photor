package com.cxd.photor.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxd.eventbox.EventBoxSubscribe;
import com.cxd.photor.PDataManager;
import com.cxd.photor.R;
import com.cxd.photor.activity.adapters.BucketAdapter;
import com.cxd.photor.activity.views.ConfirmView;
import com.cxd.photor.utils.Constant;

/**
 * create by cxd on 2020/4/7
 */
public class PBucketActivity extends PBaseActivity {

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
        recycler.setLayoutManager(new LinearLayoutManager(context,LinearLayout.VERTICAL,false));
        adapter = new BucketAdapter(context);
        recycler.setAdapter(adapter);

        adapter.update(null, PDataManager.getInstance().getBuckets(context));
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
     * 从Photor跳转，需要赋值limit
     * @param context
     * @param limit
     */
    public static void jump(Context context , int limit){
        PDataManager.getInstance().init(limit);
        if(context != null){
            Intent intent = new Intent(context, PBucketActivity.class);
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
                adapter.update(PDataManager.getInstance().getSelectedImgs(), PDataManager.getInstance().getBuckets(context));
                break;
            case Constant.EVENTBOX_COMMIT_FINISH:
                finish();
                break;
            default:
                break;
        }
    }
}
