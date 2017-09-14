package com.vu.mymusic.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vu.mymusic.ActivityMain;
import com.vu.mymusic.R;
import com.vu.mymusic.util.SongInfo;

import java.util.ArrayList;

/**
 * Created by Vu on 9/9/2017.
 */

public class SongInfoAdapter extends ArrayAdapter<SongInfo> {

    Context context;
    int resource;
    ArrayList<SongInfo> arrSongs;
    public SongInfoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<SongInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrSongs = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView=inflater.inflate(resource,null);


        ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
        TextView tvSongName = (TextView)convertView.findViewById(R.id.tvSongName);
        TextView tvSinger = (TextView)convertView.findViewById(R.id.tvSinger);
        TextView tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);
        TextView tvAlbum = (TextView)convertView.findViewById(R.id.tvAlbum);

        SongInfo songInfo = arrSongs.get(position);
//        if (position== ActivityMain.indexSong)
//        {
//            //convertView.setBackgroundColor(0xff0000);
//            convertView.setBackgroundResource(R.drawable.cell_playing);
//        }
//        else
//        {
//            //convertView.setBackgroundColor(0x000000);
//            convertView.setBackgroundResource(R.drawable.cell_normal);
//        }
        if (songInfo.getPath()== ActivityMain.pathPlaying)
        {
            //convertView.setBackgroundColor(0xff0000);
            convertView.setBackgroundResource(R.drawable.cell_playing);
        }
        else
        {
            //convertView.setBackgroundColor(0x000000);
            convertView.setBackgroundResource(R.drawable.cell_normal);
        }
        Bitmap bitmap = songInfo.getAlbumArt();
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            //bitmap.recycle();
        }
        else {
            imageView.setImageResource(R.drawable.img_default_list);
        }

        tvSongName.setText(songInfo.getTitle());
        if (songInfo.getArtist() == null || songInfo.getArtist().equals(""))
        {
            tvSinger.setText("Unknow Artist");
        }
        else {
            tvSinger.setText(songInfo.getArtist());
        }
        tvDuration.setText(ActivityMain.ConverTimeLengthToString(songInfo.getLength()/1000));
        if (songInfo.getAlbum() == null || songInfo.getAlbum().equals(""))
        {
            tvAlbum.setText("Unknow Album");
        }
        else
        {
            tvAlbum.setText(songInfo.getAlbum());
        }

        return convertView;
    }
}
