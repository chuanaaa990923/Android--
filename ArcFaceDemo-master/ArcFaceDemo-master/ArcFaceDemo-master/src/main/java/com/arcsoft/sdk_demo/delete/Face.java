package com.arcsoft.sdk_demo.delete;

import android.app.Activity;
import android.graphics.Bitmap;

import com.arcsoft.sdk_demo.Application;
import com.arcsoft.sdk_demo.FaceDB;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Face {
    private String name;
    private Bitmap mBitmap;

    public Face() {
        name = null;
        mBitmap = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}


