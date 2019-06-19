package com.sd.lib.pgclipper.point;

import android.graphics.Color;

public class ClipPoint
{
    private final int mProgress;
    private int mDisplaySize;
    private int mDisplayColor;

    public ClipPoint(int progress)
    {
        mProgress = progress;
    }

    public int getProgress()
    {
        return mProgress;
    }

    public int getDisplaySize()
    {
        return mDisplaySize;
    }

    public void setDisplaySize(int size)
    {
        mDisplaySize = size;
    }

    public int getDisplayColor()
    {
        if (mDisplayColor == 0)
            mDisplayColor = Color.WHITE;
        return mDisplayColor;
    }

    public void setDisplayColor(int displayColor)
    {
        mDisplayColor = displayColor;
    }
}
