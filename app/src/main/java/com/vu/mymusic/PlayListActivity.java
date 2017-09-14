package com.vu.mymusic;

import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import com.vu.mymusic.fragment.FolderFragment;
import com.vu.mymusic.fragment.PlaylistFragment;
import com.vu.mymusic.fragment.LibraryFragment;
import com.vu.mymusic.util.FolderInfo;
import com.vu.mymusic.util.SongInfo;
import com.vu.mymusic.adapter.ViewPagerAdapter;

public class PlayListActivity extends AppCompatActivity implements FolderFragment.OnViewPagerClickListener , LibraryFragment.OnLibraryItemClick {


    @IntDef({Fragment.PLAYLIST, Fragment.FOLDER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Fragment {
        int PLAYLIST = 0;
        int FOLDER = 1;
    }

    //ArrayList<String> arrSongPath;

    ArrayList<SongInfo> lstSong;
    ArrayList<FolderInfo> arrFolder;
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    FolderFragment  folderFragment = new FolderFragment();
    PlaylistFragment playlistFragment = new PlaylistFragment();
    LibraryFragment libraryFragment = new LibraryFragment();
    int selectedFrag =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        //arrSongPath= new ArrayList<>();
        lstSong = new ArrayList<>();
        arrFolder = new ArrayList<>();
        Intent intent = getIntent();
        //arrSongPath = (ArrayList<String>) intent.getSerializableExtra("ListSong");
        if (intent.hasExtra("ListSong"))
        lstSong = (ArrayList<SongInfo>) intent.getSerializableExtra("ListSong");

        if (intent.hasExtra("ListFolder"))
        arrFolder = (ArrayList<FolderInfo>) intent.getSerializableExtra("ListFolder");
        if (intent.hasExtra("TabFrag"))
            selectedFrag = intent.getIntExtra("TabFrag",0);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);



        setSupportActionBar(toolbar);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Playlist");
        tabLayout.getTabAt(1).setText("Folder");
        tabLayout.getTabAt(2).setText("Library");

        tabLayout.getTabAt(selectedFrag).select();

    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        //

        Bundle bundle = new Bundle();
        //bundle.putSerializable("ListSong", arrSongPath);
        bundle.putSerializable("ListSong", lstSong);
        playlistFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(playlistFragment,"Playlist");

        Bundle bundle2 = new Bundle();
        //bundle.putSerializable("ListSong", arrSongPath);
        bundle2.putSerializable("ListFolder", arrFolder);
        folderFragment.setArguments(bundle2);

        viewPagerAdapter.addFragment(folderFragment,"Folder");

        viewPagerAdapter.addFragment(libraryFragment, "Library");
        viewPager.setAdapter(viewPagerAdapter);


    }

    @Override
    public void onBlaBla(Object arg, String selectedDir) {
        Bundle bundle = new Bundle();
        bundle.putString("PATH", selectedDir);
        playlistFragment.setArguments(bundle);
        tabLayout.getTabAt(PlayListActivity.Fragment.PLAYLIST).select();


        getSupportFragmentManager()
                .beginTransaction()
                .detach(playlistFragment)
                .attach(playlistFragment)
                .commit();
    }


    @Override
    public void onSelectPlaylist(int i) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("ListSong", ActivityMain.listPlaylist.get(i).getLstSong());
        playlistFragment.setArguments(bundle);
        tabLayout.getTabAt(PlayListActivity.Fragment.PLAYLIST).select();


        getSupportFragmentManager()
                .beginTransaction()
                .detach(playlistFragment)
                .attach(playlistFragment)
                .commit();
    }

}
