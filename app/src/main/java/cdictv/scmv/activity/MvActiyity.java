package cdictv.scmv.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lzy.imagepicker.bean.ImageItem;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cdictv.scmv.R;
import cdictv.scmv.bean.Music;
import cdictv.scmv.bean.Mv;
import cdictv.scmv.service.Iservice;
import cdictv.scmv.service.MusicService;
import cdictv.scmv.util.GlideImageBanner;
import cdictv.scmv.util.SpUtils;


public class MvActiyity extends AppCompatActivity {
    private Iservice iservice;
    private Myconn conn;
    private Banner mBanner;
    private int tag=0;
    private ArrayList<ImageItem> selImageList;
    private String mPosition;
    private SpUtils mSpUtils;
    private List<String> mBsf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mv);
        mBanner=findViewById(R.id.banner);
        mSpUtils = new SpUtils();

        Intent intent2 = this.getIntent();
        mPosition = intent2.getStringExtra("position");
        mBsf = mSpUtils.getDataList("bsf",  String[].class);

        List<Music> newsList = LitePal.where("biaoshi = ?", mBsf.get(Integer.parseInt(mPosition))+"").find(Music.class);
        //[1]先调用srartservic 目的保证服务在后台才起运行
        Intent intent=new Intent(this,MusicService.class);

        intent.putExtra("uri",newsList.get(0).uri);
        startService(intent);
        conn = new Myconn();
        //[2]调用bandiservice 目的为了获取我们定义的中间人对象 就可以调用服务里的方法
        bindService(intent, conn,BIND_AUTO_CREATE);
        initBanner();
    }

    public void bf(View view) {
        change();
    }

    public void sc(View view) {


        SpUtils spUtils=new SpUtils();
//

        LitePal.deleteAll(Mv.class, "biaoshi = ? ",mBsf.get(Integer.parseInt(mPosition)));
        LitePal.deleteAll(Music.class, "biaoshi = ? ",mBsf.get(Integer.parseInt(mPosition)));
        mBsf.remove(mBsf.get(Integer.parseInt(mPosition)));
        spUtils.setDataList("bsf",mBsf);
    ;
        finish();
        Intent intent=new Intent(MvActiyity.this,IndexActivity.class);
        startActivity(intent);

    }

    public void fh(View view) {
        finish();
        Intent intent=new Intent(MvActiyity.this,IndexActivity.class);
        startActivity(intent);

    }

    //监听服务状态
    private  class  Myconn implements ServiceConnection {
        //服务监听成功
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //获取binder对象
            iservice = (Iservice) iBinder;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
    public void change() {
        if (tag == 0) {
            iservice.callplayMusic();
            tag = 1;
        } else if(tag==1){
            iservice.callpauseMusic();
            tag = 2;
        }else {
            iservice.callreplayMusic();
            tag=1;
        }
    }
    private void initBanner() {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader( new GlideImageBanner());
        //设置图片集合
        List<String> imgs=new ArrayList<>();



        List<Mv> newsList = LitePal.where("biaoshi = ?",mBsf.get(Integer.parseInt(mPosition))+"").find(Mv.class);
        List<String> titles = new ArrayList<>();
        for (int i = 1; i <=newsList.size(); i++) {
            titles.add("商品"+i);
        }
        for (Mv item:newsList) {
            Log.i("+=",item.path);
            imgs.add(item.path);
        }

        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置标题集合（当banner样式有显示title时）
        mBanner.setBannerTitles(titles);
        mBanner.setImages(imgs);
        mBanner.setBannerAnimation(Transformer.RotateDown);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iservice.callpauseMusic();
//        iservice.callplayMusic();
        finish();

    }
}

