package com.vu.mymusic.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.vu.mymusic.util.Playlist;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends Fragment {

    ArrayList<Playlist> arrPlaylist;
    ArrayAdapter arrayAdapter;
    ListView listView;
    TextView textView;

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        listView = (ListView) view.findViewById(R.id.lstPlaylist);
        textView = (TextView)view.findViewById(R.id.tvNewPlaylist);

        arrPlaylist = new ArrayList<>();
        arrPlaylist.addAll(ActivityMain.listPlaylist);
        ;
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, arrPlaylist);
        listView.setAdapter(arrayAdapter);

        final OnLibraryItemClick mCallback;
        mCallback = (OnLibraryItemClick) getActivity();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCallback.onSelectPlaylist(i);

            }
        });
        registerForContextMenu(listView);
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
                        ActivityMain.listPlaylist.add(playlist);
                        arrPlaylist.add(playlist);
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

        return view;
    }

    public interface OnLibraryItemClick {
        public void onSelectPlaylist(int i);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Toast.makeText(getView().getContext(), "onCreateContextMenu", Toast.LENGTH_SHORT).show();
        //super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.playlist_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_play:
                Intent intent1 = new Intent(getActivity(), ActivityMain.class);
                intent1.putExtra("INDEX", 0);
                intent1.putExtra("ListSong", ActivityMain.listPlaylist.get(adapterContextMenuInfo.position).getLstSong());
                getActivity().setResult(1003, intent1);
                getActivity().finish();
            case R.id.menu_enqueue:
                Toast.makeText(getView().getContext(), "menu_enqueue", Toast.LENGTH_SHORT).show();
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

                final ArrayAdapter<Playlist> playlistAdapter = new ArrayAdapter<Playlist>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
                playlistAdapter.clear();
                //arrayList.add(new Playlist("New Playlist"));
                arrayList.addAll(ActivityMain.listPlaylist);
                playlistAdapter.notifyDataSetChanged();
                listView.setAdapter(playlistAdapter);

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
                                playlist.addAllSong(arrPlaylist.get(adapterContextMenuInfo.position).getLstSong());
                                ActivityMain.listPlaylist.add(playlist);
                                arrayList.add(playlist);
                                playlistAdapter.notifyDataSetChanged();

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
                        ActivityMain.listPlaylist.get(i).addAllSong(arrPlaylist.get(adapterContextMenuInfo.position).getLstSong());
                    }
                });

                builder.setView(v);
                //builder.setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;

            case R.id.menu_rename:
                Toast.makeText(getView().getContext(), "Rename", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Rename Playlist");
                final View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.input_name_dialog, null);
                final EditText txtName = (EditText) v1.findViewById(R.id.name);
                //txtName.setText(arrItem.get(adapterContextMenuInfo.position).getName());

                builder1.setView(v1);
                builder1.setCancelable(false);

                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrPlaylist.get(adapterContextMenuInfo.position).setName(txtName.getText().toString());
                        ActivityMain.listPlaylist.get(adapterContextMenuInfo.position).setName(txtName.getText().toString());
                        arrayAdapter.notifyDataSetChanged();

                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
            default:
                //Toast.makeText(getView().getContext(), "invalid option!", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
