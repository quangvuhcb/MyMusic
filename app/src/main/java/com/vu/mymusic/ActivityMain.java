package com.vu.mymusic;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.vu.mymusic.util.FolderInfo;
import com.vu.mymusic.util.GetFolders;
import com.vu.mymusic.util.MediaPlayerController;
import com.vu.mymusic.util.PlaybackInfoListener;
import com.vu.mymusic.util.Playlist;
import com.vu.mymusic.util.SongInfo;

public class ActivityMain extends AppCompatActivity {

    public static int indexSong = 0, indexArrSeq = 0;
    public static String pathPlaying;
    public static String pathPlaying()
    {
        String str = songInfos.get(indexSong).getPath();
        return str;
    };

    public static Context context;
    public  static ArrayList<Playlist> listPlaylist;

    Button btnPlay, btnRepeat, btnShuffle, btnPrevious, btnNext;
    ImageView image;
    TextView tvSongName, tvSinger, tvCurrentTime, tvSongInfo, tvDuration;

    SeekBar seekBar;
    LinearLayout songName;

    MediaPlayerController mHolder;
    Boolean isSeeking = false, isShuffle = false, isPlaying = false, isWaitingAsync = false;
    int repeat = 0;//0: không, 1: repeat all, 2: repeat 1


    PlaybackListener playbackListener;

    //ArrayList<String> songs;
    public  static  ArrayList<SongInfo> songInfos;

    ArrayList arrSeq;//Thứ tự phát random

    //String dir = "Music";//Zing MP3
    //String dir = "Zing MP3";


    ParserFolder parserFolder;
    ProgressDialog progressDialog;

    int tagFrag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        loadResource();

        playbackListener = new PlaybackListener();

        listPlaylist = new ArrayList<>();
        //songs = new ArrayList<>();
        songInfos = new ArrayList<>();
        arrSeq = new ArrayList();
        progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Loading....");
        progressDialog.setTitle("Please wait in the moments....");

        mHolder = new MediaPlayerController(ActivityMain.this);
        mHolder.setPlaybackInfoListener(playbackListener);

        //mHolder.loadMedia(R.raw.symphony);

        setEvent();

//        if (songs.size() > 0)
//            PlaySong(songs.get(0), isPlaying);

//        if (songInfos.size() > 0)
//            PlaySong(songInfos.get(0), isPlaying);

        //Chạy ngầm, lấy trước thông tin
        parserFolder = new ParserFolder(this);
        parserFolder.execute();
        CheckPermission();
    }

    private void loadResource() {
        btnPlay = (Button) findViewById(R.id.btnPlay);
        seekBar = (SeekBar) findViewById(R.id.seekbar);

        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        btnShuffle = (Button) findViewById(R.id.btnShuffle);
        btnPrevious = (Button) findViewById(R.id.btnPlayPrevious);
        btnNext = (Button) findViewById(R.id.btnPlayNext);

        image = (ImageView) findViewById(R.id.image);
        tvSongName = (TextView) findViewById(R.id.tvSongName);
        tvSinger = (TextView) findViewById(R.id.tvSinger);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvSongInfo = (TextView) findViewById(R.id.tvSongInfo);
        tvDuration = (TextView) findViewById(R.id.tvDuration);

        songName = (LinearLayout) findViewById(R.id.songName);
    }

    private void setEvent() {

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHolder.isPlaying()) {
                    mHolder.pause();
                    //btnPlay.setBackgroundResource(R.drawable.ic_play);
                } else {
                    mHolder.play();
                    //btnPlay.setBackgroundResource(R.drawable.ic_pause);
                }
            }
        });
        btnPlay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHolder.reset();
                //btnPlay.setBackgroundResource(R.drawable.ic_play);
                return true;
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int newPosition = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    newPosition = i;
                }
                tvCurrentTime.setText(ConverTimeLengthToString(i / 1000));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
                if (newPosition != mHolder.getCurrentPosition()) {
                    mHolder.seekTo(newPosition);
                }
            }
        });
        songName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isWaitingAsync = false;
                tagFrag=0;

                //Toast.makeText(ActivityMain.this, "Please wait in the moments....", Toast.LENGTH_SHORT).show();
                if (parserFolder.getStatus() == AsyncTask.Status.RUNNING) {
                    progressDialog.show();
                }
                if (parserFolder.getStatus() != AsyncTask.Status.RUNNING) {
                    startPlayListActivity();
                } else {
                    isWaitingAsync = true;
                }
//                try {
//                    parserFolder.execute().get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (indexSong == songs.size() - 1) {
                if (indexSong == songInfos.size() - 1) {
                    indexSong = 0;
                } else {
                    if (isShuffle) {
                        if (indexArrSeq == arrSeq.size() - 1) {
                            indexArrSeq = 0;
                        } else {
                            indexArrSeq++;
                        }
                        indexSong = (int) arrSeq.get(indexArrSeq);
                    } else {
                        indexSong++;

                    }
                }
                //PlaySong(songs.get(indexSong), isPlaying);
                PlaySong(songInfos.get(indexSong), isPlaying);
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexSong == 0) {
                    //indexSong = songs.size() - 1;
                    indexSong = songInfos.size() - 1;
                } else {
                    if (isShuffle) {
                        if (indexArrSeq <= 0) {
                            indexArrSeq = arrSeq.size() - 1;
                        } else {
                            indexArrSeq--;
                        }
                        indexSong = (int) arrSeq.get(indexArrSeq);
                    } else {
                        indexSong--;
                    }
                }
                //PlaySong(songs.get(indexSong), isPlaying);
                PlaySong(songInfos.get(indexSong), isPlaying);
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShuffle) {
                    btnShuffle.setBackgroundResource(R.drawable.ic_shuffle_off);
                } else {
                    btnShuffle.setBackgroundResource(R.drawable.ic_shuffle_on);
                    indexArrSeq = -1;
                    arrSeq = new ArrayList();

                    Random rd = new Random();
                    ArrayList arrTmp = new ArrayList();
                    //for (int i = 0; i < songs.size(); i++) {
                    for (int i = 0; i < songInfos.size(); i++) {
                        arrTmp.add(i);
                    }
                    Random ran = new Random();
                    while (arrTmp.size() != 0) {
                        int a = ran.nextInt(arrTmp.size());
                        arrSeq.add(a);
                        arrTmp.remove(a);
                        //System.out.print(String.valueOf(a));
                    }


                }
                isShuffle = !isShuffle;
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (repeat) {
                    case 0:
                        repeat = 1;
                        btnRepeat.setBackgroundResource(R.drawable.ic_repeat_all);
                        break;
                    case 1:
                        repeat = 2;
                        btnRepeat.setBackgroundResource(R.drawable.ic_repeat_one);
                        break;
                    case 2:
                        repeat = 0;
                        btnRepeat.setBackgroundResource(R.drawable.ic_repeat_no);
                        break;
                }
            }
        });

    }

    public void startPlayListActivity() {
        Intent intent = new Intent(ActivityMain.this, PlayListActivity.class);
        //intent.putExtra("ListSong", songs);
        intent.putExtra("ListSong", songInfos);
        intent.putExtra("TabFrag", tagFrag);

        intent.putExtra("ListFolder", arrFolder);
        startActivityForResult(intent, 1001);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mHolder.loadMedia(songInfos.get(indexSong).getPath());
        //Log.d(TAG, "onStart: create MediaPlayer");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mHolder.isPlaying()) {
            //Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mHolder.release();
            //Log.d(TAG, "onStop: release MediaPlayer");
        }
    }


    public void PlaySong(String path, Boolean isPlay) {
        pathPlaying = path;
        SongInfo songInfo = new SongInfo(path);

        Bitmap bitmap = songInfo.getAlbumArt();
        if (bitmap != null) {
            image.setImageBitmap(songInfo.getAlbumArt());
        }
        else {
            image.setImageResource(R.drawable.img_default);
        }
        tvSongName.setText(songInfo.getTitle());

        tvSinger.setText(songInfo.getArtist());
        tvSongInfo.setText(String.valueOf(songInfo.getBitrate() / 1000) + "KBPS\t" + String.valueOf(Math.round(songInfo.getSampleRate() / 10) / 100) + "KHZ");
        tvDuration.setText(ConverTimeLengthToString(songInfo.getLength() / 1000));

        mHolder.reset();
        mHolder.loadMedia(path);
        if (isPlay)
            mHolder.play();
    }

    public void PlaySong(SongInfo songInfo, Boolean isPlay) {
        pathPlaying = songInfo.getPath();
        Bitmap bitmap = songInfo.getAlbumArt();
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
        else {
            image.setImageResource(R.drawable.img_default);
        }
        tvSongName.setText(songInfo.getTitle());

        tvSinger.setText(songInfo.getArtist());
        tvSongInfo.setText(String.valueOf(songInfo.getBitrate() / 1000) + "KBPS\t" + String.valueOf(Math.round(songInfo.getSampleRate() / 10) / 100) + "KHZ");
        tvDuration.setText(ConverTimeLengthToString(songInfo.getLength() / 1000));

        mHolder.reset();
        mHolder.loadMedia(songInfo.getPath());
        if (isPlay)
            mHolder.play();
    }

    public static String ConverTimeLengthToString(int time) {
        long h, m, s;
        String lengthString = "";
        s = time;
        m = s / 60;
        s = s % 60;
        h = m / 60;
        m = m % 60;
        if (h > 0)
            lengthString = String.format("%02d", h) + ":";

        lengthString = lengthString +
                String.format("%02d", m) + ":" +
                String.format("%02d", s);
        return lengthString;

    }

    public void LoadPlaylist() {
        //songName.callOnClick();
        tagFrag=1;
        isWaitingAsync = false;

        //Toast.makeText(ActivityMain.this, "Please wait in the moments....", Toast.LENGTH_SHORT).show();
        if (parserFolder.getStatus() == AsyncTask.Status.RUNNING) {
            progressDialog.show();
        }
        if (parserFolder.getStatus() != AsyncTask.Status.RUNNING) {
            startPlayListActivity();
        } else {
            isWaitingAsync = true;
        }

//        File[] externalSdCards =
//                getExternalFilesDirs("");
//        File externalSdCard = null;
//        for (int i = 0; i < externalSdCards.length; i++) {
//            externalSdCard = externalSdCards[i].getParentFile().getParentFile().getParentFile().getParentFile();
//        }
//
//        File musicDir = new File(externalSdCard.getPath() + "/" + dir);
//        if (!musicDir.exists())
//
//        {
//            externalSdCard =
//                    getExternalFilesDirs("")[0].getParentFile().getParentFile().getParentFile().getParentFile();
//
//
//            musicDir = new File(externalSdCard.getPath() + "/" + dir);
//        }
//        File[] files = musicDir.listFiles();
//        int k = 0;
//        for (File file : files) {
//            String tmp = file.getPath();
//            if (file.isFile() && tmp.toUpperCase().endsWith(".MP3")) {
//                //songs.add(file.getPath());
//                songInfos.add(new SongInfo(file.getPath()));
//                //Toast.makeText(this, file.getPath(), Toast.LENGTH_SHORT).show();
//                k++;
//                //if (k == 5) break;
//            }
//
//        }



    }

    public void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            LoadPlaylist();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == 1002) {
                indexSong = data.getIntExtra("INDEX", 0);
                //PlaySong(songs.get(indexSong), true);
                PlaySong(songInfos.get(indexSong), true);

            } else if (resultCode == 1003) {
                if (data.hasExtra("ListSong")) {
                    songInfos.clear();
                    ArrayList<SongInfo> arrayList = (ArrayList<SongInfo>) data.getSerializableExtra("ListSong");
                    songInfos.addAll(arrayList);
                    indexSong = data.getIntExtra("INDEX", 0);
                    //PlaySong(songs.get(indexSong), true);
                    PlaySong(songInfos.get(indexSong), true);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Didn't grant permission!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            LoadPlaylist();
        }
    }


    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            seekBar.setMax(duration);
            String log = String.format("setPlaybackDuration: setMax(%d)", duration);
            //Log.d(TAG, log);
            Toast.makeText(ActivityMain.this, log, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPositionChanged(int position) {
            if (!isSeeking) {
                seekBar.setProgress(position);
                //tvCurrentTime.setText(ConverTimeLengthToString(position/1000));
                //String log =String.format("setPlaybackPosition: setProgress(%d)", position);
                //Log.d(TAG, log);
                //Toast.makeText(ActivityMain.this, log, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
//            if (isPlaying && state != State.PLAYING) {
//                btnPlay.setBackgroundResource(R.drawable.ic_play);
//            } else if (!isPlaying && state == State.PLAYING) {
//                btnPlay.setBackgroundResource(R.drawable.ic_pause);
//            }
//            if (state == State.PLAYING) {
//                isPlaying = true;
//            } else {
//                //isPlaying = false;
//            }
            if (state == State.PLAYING) {
                isPlaying = true;
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            } else {
                //isPlaying = false;
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            }
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
            if (repeat == 2) {
                btnPlay.callOnClick();
            } else {
                //if (indexSong == songs.size() - 1) {
                if (indexSong == songInfos.size() - 1) {
                    if (repeat == 0) {
                        isPlaying = false;
                    } else//repeat all
                    {
                        btnNext.callOnClick();
                    }
                } else {
                    btnNext.callOnClick();
                }
            }
        }

        @Override
        public void onLogUpdated(String message) {
//            if (mTextDebug != null) {
//                mTextDebug.append(message);
//                mTextDebug.append("\n");
//                // Moves the scrollContainer focus to the end.
//                mScrollContainer.post(
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
//                            }
//                        });
//            }
            //Toast.makeText(ActivityMain.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<FolderInfo> arrFolder;

    class ParserFolder extends GetFolders {
        public ParserFolder(Context activity) {
            super(activity);
        }

        @Override
        protected void onPostExecute(ArrayList<FolderInfo> s) {
            super.onPostExecute(s);
            arrFolder = new ArrayList<>();
            arrFolder.addAll(s);

            if (isWaitingAsync) {
                startPlayListActivity();
            }

            Toast.makeText(ActivityMain.this, "Get all folder done!", Toast.LENGTH_SHORT).show();
        }

    }
}
