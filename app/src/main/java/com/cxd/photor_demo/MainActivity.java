package com.cxd.photor_demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cxd.photor.EMSource;
import com.cxd.photor.OnPhotorListener;
import com.cxd.photor.Photor;
import com.cxd.photor.model.ImgBean;
import com.cxd.photor.utils.DensityUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPhotorListener , View.OnClickListener {


    private NestedScrollView nsv ;
    private RadioButton cropRBTrue , cropRBFalse ,selectFromAblumRB , selectFromDirectoryRB;
    private EditText cropWidthET , cropHeightET ,selectLimitET;
    private Button clearCacheButton , cameraButton , selectButton;
    private RecyclerView recycler ;

    private PAdapter mAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nsv = findViewById(R.id.nsv);
        cropRBTrue = findViewById(R.id.cropRBTrue);
        cropRBFalse = findViewById(R.id.cropRBFalse);
        selectFromAblumRB = findViewById(R.id.selectFromAblumRB);
        selectFromDirectoryRB = findViewById(R.id.selectFromDirectoryRB);
        cropWidthET = findViewById(R.id.cropWidthET);
        cropHeightET = findViewById(R.id.cropHeightET);
        selectLimitET = findViewById(R.id.selectLimitET);
        clearCacheButton = findViewById(R.id.clearCacheButton);
        cameraButton = findViewById(R.id.cameraButton);
        selectButton = findViewById(R.id.selectButton);
        recycler = findViewById(R.id.recycler);

        cropRBTrue.setOnClickListener(this);
        cropRBFalse.setOnClickListener(this);
        selectFromAblumRB.setOnClickListener(this);
        selectFromDirectoryRB.setOnClickListener(this);
        clearCacheButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);


        recycler.setLayoutManager(new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int p = parent.getChildAdapterPosition(view);

                outRect.top = DensityUtil.dp2px(MainActivity.this,12) ;
                switch ((p+1) % 3){
                    case 1:
                        outRect.left = DensityUtil.dp2px(MainActivity.this,12) ;
                        outRect.right = DensityUtil.dp2px(MainActivity.this,4) ;
                        break;
                    case 2:
                        outRect.left = DensityUtil.dp2px(MainActivity.this,8) ;
                        outRect.right = DensityUtil.dp2px(MainActivity.this,8) ;
                        break;
                    case 0:
                        outRect.left = DensityUtil.dp2px(MainActivity.this,4) ;
                        outRect.right = DensityUtil.dp2px(MainActivity.this,12) ;
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClick(View v){
        int cropWidth , cropHeight = 0 ;
        if(cropRBTrue.isChecked()){
            cropWidth = Integer.valueOf(cropWidthET.getText().toString());
            cropHeight = Integer.valueOf(cropHeightET.getText().toString());
        }else{
            cropWidth = cropHeight = 0 ;
        }


        int limit = Integer.valueOf(selectLimitET.getText().toString());


        switch(v.getId()){
            case R.id.clearCacheButton:
                if(Photor.getInstance()
                        .context(this)
                        .clearCache()){
                    Toast.makeText(this,"清除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"清除失败",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cameraButton:
                Photor.getInstance()
                        .context(this)
                        .crop(cropWidth,cropHeight)
                        .onPhotorListener(this)
                        .requestImgFromCamera();
                break;
            case R.id.selectButton:
                if(selectFromAblumRB.isChecked()){
                    Photor.getInstance()
                            .context(this)
                            .crop(cropWidth,cropHeight)
                            .onPhotorListener(this)
                            .requestImgsFromAlbum(limit);
                }else{
                    /*从文件*/
                    Photor.getInstance()
                            .context(this)
                            .crop(cropWidth,cropHeight)
                            .onPhotorListener(this)
                            .requestImgsFromDirectory(limit);
                }
                break;
        }
    }

    @Override
    public void onSuccess(List<ImgBean> IMGs, EMSource source) {
        mAdapter = new PAdapter(IMGs);
        recycler.setAdapter(mAdapter);
//        for (ImgBean imgBean : IMGs) {
//            Log.i("aaa", "onSuccess: "+imgBean.toString());
//        }
//        Log.i("aaa", "onSuccess: EMSource = " + source);

        int w = (DensityUtil.getScreenWidth(MainActivity.this) -
                DensityUtil.dp2px(MainActivity.this,48) ) / 3 ;
        nsv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                nsv.smoothScrollBy(0,(IMGs.size() / 3) * (w + DensityUtil.dp2px(MainActivity.this,12)));
                nsv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    class PAdapter extends RecyclerView.Adapter<PAdapter.ViewHolder>{
        List<ImgBean> IMGs ;

        public PAdapter(List<ImgBean> IMGs) {
            this.IMGs = IMGs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ImageView iv = new ImageView(MainActivity.this);
            int w = (DensityUtil.getScreenWidth(MainActivity.this) -
                    DensityUtil.dp2px(MainActivity.this,48) ) / 3 ;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w,w);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return new ViewHolder(iv);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Glide.with(MainActivity.this).load(IMGs.get(i).getUrl()).into((ImageView) viewHolder.itemView);
            viewHolder.itemView.setOnClickListener(v -> {
                new CropPop(MainActivity.this).show(IMGs.get(i).getUrl());
            });
        }

        @Override
        public int getItemCount() {
            return IMGs == null ? 0 : IMGs.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }


    class CropPop extends ShadowPopupWindow{

        ImageView iv ;
        public CropPop(Activity context) {
            super(context);
            this.setWidth(getScreenWidth());
            this.setHeight(getScreenWidth());
        }

        @Override
        protected void onCreateView(View view) {
            iv = (ImageView) view;
        }

        public void show(String url){
            Glide.with(MainActivity.this).load(url).into(iv);
            showCenteral(R.layout.activity_main);
        }

        @Override
        protected long getAnimatorDuration() {
            return 400;
        }

        @Override
        protected Object getLayoutIdOrView() {
            ImageView iv = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-1);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
}
