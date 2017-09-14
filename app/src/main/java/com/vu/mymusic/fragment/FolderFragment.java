package com.vu.mymusic.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vu.mymusic.PlayListActivity;
import com.vu.mymusic.R;

import java.util.ArrayList;

import com.vu.mymusic.util.FolderInfo;
import com.vu.mymusic.util.GetFolders;


/**
 * A simple {@link Fragment} subclass.
 */
public class FolderFragment extends Fragment {

    ArrayList<FolderInfo> arrFolder;

    ArrayAdapter<FolderInfo> arrayAdapter;
    ArrayList<String> arr;

    ListView lstFolder;

    public static String selectedDir="";

    public FolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_folder, container, false);

        arrFolder = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<FolderInfo>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, arrFolder);

        lstFolder = (ListView)view.findViewById(R.id.lstFolder);
        lstFolder.setAdapter(arrayAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey("ListFolder")) {
                ArrayList<FolderInfo> arr = (ArrayList<FolderInfo>) bundle.getSerializable("ListFolder");
                if (arr!=null) {
                    arrFolder.clear();
                    arrFolder.addAll(arr);
//                    arrayAdapter.clear();
//                    arrayAdapter.addAll(arr);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }

//        FolderInfo folderInfo = new FolderInfo();
//        folderInfo.setPath("test");
//        arrFolder.add(folderInfo);

//        Toast.makeText(getActivity(), "Start get Folder List", Toast.LENGTH_SHORT).show();
//        ParserFolder parserFolder = new ParserFolder();
//        parserFolder.execute();


        final OnViewPagerClickListener mCallback;

        mCallback =(OnViewPagerClickListener) getActivity() ;// **try catch this code**
        lstFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //PlaylistFragment playlistFragment= new PlaylistFragment();
                //playlistFragment.show();
                selectedDir = arrFolder.get(i).getPath();
                mCallback.onBlaBla(PlayListActivity.Fragment.PLAYLIST,selectedDir);
                }
        });

        return view;
    }
    public interface OnViewPagerClickListener {
        public void onBlaBla(Object arg, String selectedDir);
    }

    class ParserFolder extends GetFolders {
        public ParserFolder(Context activity) {
            super(activity);
        }

        @Override
        protected void onPostExecute(ArrayList<FolderInfo> s) {
            super.onPostExecute(s);
            arrayAdapter.addAll(s);
            arrayAdapter.notifyDataSetChanged();
            //Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
        }

    }

}

