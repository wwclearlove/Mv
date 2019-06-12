package cdictv.scmv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import cdictv.scmv.R;
import cdictv.scmv.adatper.MuscAdatper;
import cdictv.scmv.bean.Song;
import cdictv.scmv.util.AudioUtils;

public class MusicActivity extends AppCompatActivity {

    private ListView list;
    private ArrayList<Song> mAllSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initView();

    }

    private void initView() {
        list = (ListView) findViewById(R.id.list);
        mAllSongs = AudioUtils.getAllSongs(MusicActivity.this);
        Log.d("song", "initView: "+mAllSongs);
        list.setAdapter(new MuscAdatper(mAllSongs,MusicActivity.this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MusicActivity.this,MainActivity.class);
                intent.putExtra("uri",mAllSongs.get(position).getFileUrl());
                int resultCode = 2;
                setResult(resultCode,intent);
                finish();
            }
        });
    }
}
