package com.vu.mymusic.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vu on 9/13/2017.
 */

public class Playlist implements Serializable {
    String name;
    String comment;
    Date createdDate;
    Date editedDate;
    ArrayList<SongInfo> lstSong;

    public Playlist() {
        createdDate = Calendar.getInstance().getTime();
        lstSong = new ArrayList<>();
    }

    public Playlist(String name) {
        this.name = name;
        lstSong = new ArrayList<>();
    }

    public Playlist(String name, String comment, Date createdDate, Date editedDate, ArrayList<SongInfo> lstSong) {
        this.name = name;
        this.comment = comment;
        this.createdDate = createdDate;
        this.editedDate = editedDate;
        this.lstSong = lstSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    public ArrayList<SongInfo> getLstSong() {
        return lstSong;
    }

    public void setLstSong(ArrayList<SongInfo> lstSong) {
        this.lstSong = lstSong;
    }

    public void addSong(SongInfo song) {
        //if (this.lstSong.indexOf(song)==-1)
        this.lstSong.add(song);
    }
    public void addAllSong(ArrayList<SongInfo> songs) {
        //if (this.lstSong.indexOf(song)==-1)
        for (SongInfo song: songs) {
            this.lstSong.add(song);
        }
    }


    @Override
    public String toString() {
        return name;
    }
}
