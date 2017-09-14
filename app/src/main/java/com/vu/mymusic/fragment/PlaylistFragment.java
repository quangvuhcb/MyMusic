package com.vu.mymusic.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vu.mymusic.ActivityMain;
import com.vu.mymusic.PlayListActivity;
import com.vu.mymusic.R;
import com.vu.mymusic.adapter.SongInfoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.vu.mymusic.util.FolderInfo;
import com.vu.mymusic.util.Playlist;
import com.vu.mymusic.util.SongInfo;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends Fragment {

    ArrayList<SongInfo> arrSong;
    SongInfoAdapter songInfoAdapter;
    ListView lstSong;
    //ArrayList<String> arrSongPath;
    //ArrayList<SongInfo>  songInfos;
    String selectedDir;
    //0: MainActivity lấy danh sách bài hát hiện tại
    //1: Từ folder hay list => nếu chọn 1 bài hát thì trả về 1 playlist mới cho Mainactivity
    int loadFrom = 0;

    public PlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadFrom = 0;
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
//        arrSongPath= new ArrayList<>();
//
//        Intent intent = getActivity().getIntent();
//        arrSongPath = (ArrayList<String>) intent.getSerializableExtra("ListSong");

        arrSong = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("ListSong")) {
                //arrSongPath = (ArrayList<String>) bundle.getSerializable("ListSong");
                arrSong = (ArrayList<SongInfo>) bundle.getSerializable("ListSong");
            }
        }

        lstSong = (ListView) view.findViewById(R.id.lstSong);

        songInfoAdapter = new SongInfoAdapter(getActivity(), R.layout.song_in_list, arrSong);

        lstSong.setAdapter(songInfoAdapter);

//        for (String s:arrSongPath)
//        {
//            arrSong.add(new SongInfo(getActivity(), s));
//        }
        //arrSong.addAll(songInfos);
        songInfoAdapter.notifyDataSetChanged();

        lstSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getActivity(), ActivityMain.class);
                intent1.putExtra("INDEX", i);
                if (loadFrom==1)
                {
                    intent1.putExtra("ListSong", arrSong);
                    getActivity().setResult(1003,intent1);
                }
                else {
                    getActivity().setResult(1002,intent1);
                }
                getActivity().finish();
            }
        });
        // Inflate the layout for this fragment
        //Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("PATH")) {
                selectedDir = bundle.getString("PATH");
                if (!selectedDir.equals("")) {
                    loadFrom = 1;
                    FolderInfo info = new FolderInfo();
                    info.setPath(selectedDir);

                    Log.i("PlaylistFragment", "start get songs: " + Calendar.getInstance().getTime().toString());
//                    arrSong.clear();
//                    arrSong.addAll(info.getSongs(getActivity()));
//                    Log.i("PlaylistFragment", "end get songs: " + Calendar.getInstance().getTime().toString());
//                    songInfoAdapter.notifyDataSetChanged();
//                    Log.i("PlaylistFragment", "end apply songs: " + Calendar.getInstance().getTime().toString());

                    GetSongsOfFolder getSongsOfFolder = new GetSongsOfFolder();
                    getSongsOfFolder.execute(selectedDir);
                }
            }
        }
        registerForContextMenu(lstSong);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_enqueue:
                Toast.makeText(getView().getContext(), "Rename", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add2playlist:
                Toast.makeText(getView().getContext(), "Add 2 playlist", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Playlist");
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.list_playlist, null);
                final ListView listView = (ListView) v.findViewById(R.id.listPlaylist);
                TextView textView = (TextView)v.findViewById(R.id.tvNewPlaylist);

                final ArrayList<Playlist> arrayList = new ArrayList<>();
                arrayList.addAll(ActivityMain.listPlaylist);

                final ArrayAdapter<Playlist> arrayAdapter = new ArrayAdapter<Playlist>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                arrayAdapter.clear();
                //arrayList.add(new Playlist("New Playlist"));
                arrayList.addAll(ActivityMain.listPlaylist);
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Create Playlist");
                        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.input_name_dialog, null);
                        final EditText txtName = (EditText) v.findViewById(R.id.name);
                        //txtName.setText(arrItem.get(adapterContextMenuInfo.position).getName());

                        builder.setView(v);
                        builder.setCancelable(false);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Playlist playlist = new Playlist(txtName.getText().toString());
                                playlist.addSong(arrSong.get(adapterContextMenuInfo.position));
                                ActivityMain.listPlaylist.add(playlist);
                                arrayList.add(playlist);
                                arrayAdapter.notifyDataSetChanged();

                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i < 0) return;
                        ActivityMain.listPlaylist.get(i).addSong(arrSong.get(adapterContextMenuInfo.position));
                    }
                });

                builder.setView(v);
                //builder.setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;

            default:
                //Toast.makeText(getView().getContext(), "invalid option!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Toast.makeText(getView().getContext(), "onCreateContextMenu", Toast.LENGTH_SHORT).show();
        //super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.song_menu, menu);

    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.menu_update,menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//
//        switch (item.getItemId()){
//            case R.id.item_update:
//
//                Toast.makeText(getActivity(), "Update ah", Toast.LENGTH_SHORT).show();
//
//
//
//                break;
//            case R.id.item_del:
//
//                Toast.makeText(getActivity(), "Delete ah", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        return super.onContextItemSelected(item);
//
//    }



    class GetSongsOfFolder extends AsyncTask<String, Void, ArrayList<SongInfo>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait in the moments....");
            progressDialog.show();
        }

        @Override
        protected ArrayList<SongInfo> doInBackground(String... str) {
            ArrayList<SongInfo> arrayList = new ArrayList<>();

            File curDir = new File(str[0]);
            if (!curDir.exists() || !curDir.canRead()) return arrayList;

            for (File file : curDir.listFiles())
            {
                if (file.isFile() && file.length()>0)
                {
                    if (file.getName().toLowerCase().endsWith(".mp3")
                            || file.getName().toLowerCase().endsWith(".wav")
                            //|| file.getName().toLowerCase().endsWith(".flac")
                            || file.getName().endsWith(".aac")
                            || file.getName().endsWith(".wav"))
                    {
                        arrayList.add(new SongInfo(file.getPath()));
                    }
                }
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<SongInfo> s) {
            super.onPostExecute(s);
            arrSong.clear();
            arrSong.addAll(s);
            songInfoAdapter.notifyDataSetChanged();
            Log.i("PlaylistFragment", "end get songs: " + Calendar.getInstance().getTime().toString());
            if (progressDialog.isShowing()) progressDialog.dismiss();
        }

    }
}
