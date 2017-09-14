package com.vu.mymusic.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import com.vu.mymusic.ActivityMain;
import com.vu.mymusic.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Calendar;


/**
 * Created by Vu on 9/9/2017.
 */

public class SongInfo implements Serializable {

    public String path;//Đường dẫn
    public String title; //Tên bài hát
    public String composer;//Nhạc sĩ
    public String genre;//Thể loại
    public String artist;//Ca sĩ
    public String album;//
    public int length;//độ dài bài hát
    public transient Bitmap albumArt;
    public int bitrate;
    public int sampleRate;

    public SongInfo() {
        composer = "";
    }

    public SongInfo(String path) {
        Log.i("SongInfo", Calendar.getInstance().getTime() + "\nStart get song info: " + path);
        this.path = path;
        composer = "";
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);

//            FileInputStream inputStream = new FileInputStream(path);
//            mmr.setDataSource(inputStream.getFD());
//            inputStream.close();

            File file = new File(path);
//            Uri contentUri = Uri.fromFile(file);
//            mmr.setDataSource(String.valueOf(contentUri));

            title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (title == null || title.equals("")) {
                title = file.getName();
            }
            try {
                composer = URLEncoder.encode(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER), "UTF-8");

            } catch (Exception ex) {

            }
            genre = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
            artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            length = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

//        byte[] artBytes =  mmr.getEmbeddedPicture();
//        if(artBytes!=null)
//        {
//            albumArt = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
//        }
//        else
//        {
//            //albumArt = BitmapFactory.decodeResource(context.getResources(),R.drawable.img_default);
//            albumArt = BitmapFactory.decodeResource(ActivityMain.context.getResources(),R.drawable.img_default);
//        }
            bitrate = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            MediaExtractor mex = new MediaExtractor();
            try {
                //mex.setDataSource(path);// the adresss location of the sound on sdcard.
                FileInputStream fis = new FileInputStream(file);
                FileDescriptor fd = fis.getFD();
                mex.setDataSource(fd);

                MediaFormat mf = mex.getTrackFormat(0);

                sampleRate = Integer.parseInt(String.valueOf(mf.getInteger(MediaFormat.KEY_SAMPLE_RATE)));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.i("SongInfo", "Path fail: " + path);

        }
        finally {
            mmr.release();
        }


    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Bitmap getAlbumArt() {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(path);
            byte[] artBytes = mmr.getEmbeddedPicture();

            if (artBytes != null) {
                return BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            } else {
                return null;//BitmapFactory.decodeResource(ActivityMain.context.getResources(), R.drawable.img_default_list);

            }
        }
        catch (Exception e)
        {
            return null;//return BitmapFactory.decodeResource(ActivityMain.context.getResources(), R.drawable.img_default);
        }
        finally {
            mmr.release();
        }
        //return albumArt;
    }

    public void setAlbumArt(Bitmap albumArt) {
        this.albumArt = albumArt;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
}

