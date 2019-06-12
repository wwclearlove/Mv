package cdictv.scmv.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuezj.cardbanner.ImageData;

import java.util.List;

import cdictv.scmv.R;

public class GrideAdatper extends BaseAdapter {
    private List<ImageData> mImageData;
    private Context mContext;

    public GrideAdatper(List<ImageData> imageData, Context context) {
        mImageData = imageData;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mImageData.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        ImageData bean = mImageData.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_mainactivity, null);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.img_index);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext).load(bean.getImage())
                .placeholder(R.drawable.default_image).error(R.drawable.default_image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(viewHolder.img);
        return convertView;
    }

    class ViewHolder {

        ImageView img;

    }
}
