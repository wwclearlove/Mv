package cdictv.scmv.activity;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import cdictv.scmv.R;
import cdictv.scmv.adatper.ImagePickerAdapter;
import cdictv.scmv.bean.Music;
import cdictv.scmv.bean.Mv;
import cdictv.scmv.bean.Song;
import cdictv.scmv.util.GlideImageLoader;
import cdictv.scmv.util.SelectDialog;
import cdictv.scmv.util.SpUtils;

public class MainActivity extends AppCompatActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    SpUtils spUtils;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private ArrayList<String> bs; //当前选择的所有图片
    private int maxImgCount = 15;               //允许选择图片最大数
    public int i;
    Button mButton;
    Button mMusic;
    private ArrayList<Song> mAllSongs;
    private String mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      initview();
        //最好放到 Application oncreate执行
        initImagePicker();
        initWidget();
        initlintener();
    }

    private void initview() {
        MediaScannerConnection.scanFile(this, new String[] { Environment
                .getExternalStorageDirectory().getAbsolutePath() }, null, null);
        mButton=findViewById(R.id.btn);
        mMusic=findViewById(R.id.music);
        bs=new ArrayList<>();
        spUtils=new SpUtils();
//        spUtils.setDataList("bsf",null);
        i=1;
    }

    private void initlintener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(selImageList);
            }
        });
        mMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivityForResult(new Intent(MainActivity.this,MusicActivity.class),1);

            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                            //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(false);                      //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(1200);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(1600);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1200);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1600);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style.windswtle, listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(MainActivity.this, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }
                    }
                }, names);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS,true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&&resultCode == 2) {
            mUri = data.getStringExtra("uri");
            Log.d("uri","ww"+ mUri);
        }
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null){
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null){
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }

    }

    private void uploadImage(ArrayList<ImageItem> pathList) {

        Intent intent=new Intent(MainActivity.this,IndexActivity.class);
        int numcode = (int) ((Math.random() * 9 + 1) * 100000);
        Music music=new Music();
        music.setBiaoshi(numcode+"");
        music.setUri(mUri);
        music.save();
        for (ImageItem item : pathList) {
            Mv mv=new Mv();
            mv.setPath(item.path);
            mv.setBiaoshi(numcode+"");
            mv.setMimeType("jpg");
            mv.save();
        }
        Log.d("bs", "uploadImage: "+numcode);
        List<String> bsf = spUtils.getDataList("bsf",  String[].class);
        if(bsf==null){

        }else {
            for (String in: bsf) {
               bs.add(in);
            }

        }
        bs.add(numcode+"");
        spUtils.setDataList("bsf",bs);
        List<String>mBsf = spUtils.getDataList("bsf",  String[].class);
        Log.d("+++", "uploadImage: "+mBsf);
        startActivity(intent);//启动intent
        finish();
    }

}
