package com.vu.mymusic.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.vu.mymusic.ActivityMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vu on 9/12/2017.
 */

public abstract class GetFolders extends AsyncTask<Void, Void, ArrayList<FolderInfo>> {

    public GetFolders(Context activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
    }

    private Context activity;
    ProgressDialog progressDialog;

//    protected void onPreExecute() {
//
//        progressDialog = new ProgressDialog(activity);
//        progressDialog.setTitle("Loading....");
//        progressDialog.show();
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<FolderInfo> s) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }

        @Override
    protected ArrayList<FolderInfo> doInBackground(Void... voids) {
        ArrayList<FolderInfo> arrayList = new ArrayList<>();

        File[] externalSdCards =
                ActivityMain.context.getExternalFilesDirs("");
        File externalSdCard = null;
        for (int i = 0; i < externalSdCards.length; i++) {
            externalSdCard = externalSdCards[i].getParentFile().getParentFile().getParentFile().getParentFile();
            arrayList.addAll(GetFolderList(externalSdCard.getPath()));
        }

        return arrayList;
    }



    public ArrayList<FolderInfo> GetFolderList(String path) {
        ArrayList<FolderInfo> arr = new ArrayList<>();
        File curDir = new File(path);
        if (!curDir.exists() || !curDir.canRead()) return arr;
//        FilenameFilter filter = new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String s) {
//                if (file.isFile() && (s.toLowerCase().endsWith(".mp3")
//                        || s.toLowerCase().endsWith(".wav")
//                        || s.toLowerCase().endsWith(".flac")
//                        || s.endsWith(".aac")
//                        || s.endsWith(".wav")
//                )) return true;
//                return false;
//            }
//        };
//        String[] listFile = curDir.list(filter);
//        if (listFile.length > 0) {
//            FolderInfo folderInfo = new FolderInfo();
//            folderInfo.setPath(path);
//        }



        for (File file : curDir.listFiles())
        {
            if (file.isFile() && file.length()>0)
            {
                if (file.getName().toLowerCase().endsWith(".mp3")
                        || file.getName().toLowerCase().endsWith(".wav")
                        || file.getName().toLowerCase().endsWith(".flac")
                        || file.getName().endsWith(".aac")
                        || file.getName().endsWith(".wav"))
                {
                    FolderInfo folderInfo = new FolderInfo();
                    folderInfo.setPath(path);
                    arr.add(folderInfo);
                    break;
                }
            }
        }
        for (File file : curDir.listFiles())
        {
            if (file.isDirectory())
            {
                arr.addAll(GetFolderList(file.getPath()));
            }


        }

//        FilenameFilter filterFolder = new FilenameFilter() {
//            @Override
//            public boolean accept(File file, String s) {
//                if (file.isDirectory()) return true;
//                return false;
//            }
//        };
//        String[] listFile2 = curDir.list(filter);
//        for (String subDir : listFile2) {
//            arr.addAll(GetFolderList(subDir));
//        }
        return arr;
    }

}
