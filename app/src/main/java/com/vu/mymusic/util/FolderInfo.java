package com.vu.mymusic.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.vu.mymusic.ActivityMain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vu on 9/11/2017.
 */

public class FolderInfo implements Serializable {
    String name, path;
    int songNumber;
    double length;

    public FolderInfo() {
    }

    public FolderInfo(String name, String path, int songNumber, double length) {

        this.name = name;
        this.path = path;
        this.songNumber = songNumber;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return path;
    }
//    public ArrayList<SongInfo> getSongs(Context context)
//    {
//        ArrayList<SongInfo> arrayList = new ArrayList<>();
//
//        File curDir = new File(path);
//        if (!curDir.exists() || !curDir.canRead()) return arrayList;
//
//        for (File file : curDir.listFiles())
//        {
//            if (file.isFile() && file.length()>0)
//            {
//                if (file.getName().toLowerCase().endsWith(".mp3")
//                        || file.getName().toLowerCase().endsWith(".wav")
//                        || file.getName().toLowerCase().endsWith(".flac")
//                        || file.getName().endsWith(".aac")
//                        || file.getName().endsWith(".wav"))
//                {
//                    arrayList.add(new SongInfo(file.getPath()));
//                }
//            }
//        }
//        return arrayList;
//    }
//


}
