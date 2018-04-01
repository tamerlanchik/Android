package com.example.andrey.criminalintent;

import android.util.Log;

import java.util.Date;
import java.util.UUID;

/**
 * Created by PC on 18.02.2018.
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public void copy(Crime c) {
        mId = c.getId();
        mTitle = c.getTitle();
        mDate = c.getDate();
        mSolved = c.isSolved();
        mSuspect = c.getSuspect();
    }

    public String getSuspect() {
        return mSuspect;
    }
    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }
    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
