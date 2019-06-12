package cdictv.scmv.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cdictv.scmv.R;
import cdictv.scmv.bean.Song;

public class MuscAdatper extends BaseAdapter {
    ArrayList<Song> mAllSongs;
    private Context mContext;

    public MuscAdatper(ArrayList<Song> allSongs, Context context) {
        mAllSongs = allSongs;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAllSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Song bean = mAllSongs.get(position);
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_music, null);
            viewHolder = new ViewHolder();
            viewHolder.id = convertView.findViewById(R.id.music_id);
            viewHolder.musciname = convertView.findViewById(R.id.music_name);
            viewHolder.songname = convertView.findViewById(R.id.music_songname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.id.setText(position+1+"");
        viewHolder.musciname.setText(bean.getFileName());
        viewHolder.songname.setText(bean.getSinger());
        //

        return convertView;
    }

    class ViewHolder {
        TextView id;
        TextView musciname;
        TextView songname;
    }
}
