package cdictv.scmv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.xuezj.cardbanner.ImageData;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cdictv.scmv.R;
import cdictv.scmv.adatper.GrideAdatper;
import cdictv.scmv.bean.Mv;
import cdictv.scmv.util.SpUtils;

public class IndexActivity extends AppCompatActivity {


    private GridView cardBanner;
    private List<String> mBsf;
    private List<ImageData> mImageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        SpUtils spUtils=new SpUtils();
        mBsf = spUtils.getDataList("bsf",  String[].class);
        if(mBsf.size()!=0||mBsf==null) {
            Log.d("=","2");
            initadata();

        }else {
            Log.d("=","3");
            ImageData imageData1 = new ImageData();
            imageData1.setImage(R.mipmap.wu);
            mImageData.add(imageData1);
            Log.d("===",imageData1.getImage().toString());
        }
        initbanner();
    }

    private void initadata() {
        for (String integer:mBsf) {
            Log.d("integer", "initbanner: "+integer);
            List<Mv> newsList = LitePal.where("biaoshi = ? ",integer+"").find(Mv.class);
            if(newsList.size()!=0) {
                ImageData imageData1 = new ImageData();
                imageData1.setImage(newsList.get(0).path);
                Log.d("integer", "initbanner: "+newsList.get(0).path);
                mImageData.add(imageData1);
            }

        }
    }

    private void initbanner() {
        cardBanner.setAdapter(new GrideAdatper(mImageData,IndexActivity.this));
        cardBanner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mImageData.get(position).getImage().toString().equals("2131427340")) {
                    Intent intent = new Intent(IndexActivity.this, MvActiyity.class);
                    intent.putExtra("position", position + "");
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"暂无历史记录",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private void initView() {
        cardBanner =findViewById(R.id.banner);
        mImageData = new ArrayList<>();
    }


    public void sc(View view) {
        startActivity(new Intent(IndexActivity.this,MainActivity.class));
        finish();
    }
}
