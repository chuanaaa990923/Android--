package com.arcsoft.sdk_demo;

import java.util.Date;
import java.util.UUID;

public class Crime {




    private UUID mUUID;
    private int mId;
    private String mName;
    private Date mDate;
    private boolean mSolved;

    public Crime() {
        mUUID=UUID.randomUUID();
        mDate = new Date();
        mSolved =false;
    }
    public Crime(UUID uuid) {

        mUUID=uuid;
        mDate = new Date();
        mSolved =false;
    }


    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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
}
