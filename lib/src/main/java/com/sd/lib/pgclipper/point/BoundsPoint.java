package com.sd.lib.pgclipper.point;

import android.graphics.Color;

public class BoundsPoint extends ClipPoint
{
    private int mBoundColor;

    private boolean mIsSelected;
    private int mSelectedColor;

    public BoundsPoint(int progress)
    {
        super(progress);
    }

    public int getBoundColor()
    {
        return mBoundColor;
    }

    public void setBoundColor(int color)
    {
        mBoundColor = color;
    }

    public boolean isSelected()
    {
        return mIsSelected;
    }

    public void setSelected(boolean selected)
    {
        mIsSelected = selected;
    }

    public int getSelectedColor()
    {
        if (mSelectedColor == 0)
            mSelectedColor = Color.RED;
        return mSelectedColor;
    }

    public void setSelectedColor(int color)
    {
        mSelectedColor = color;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof BoundsPoint))
            return false;

        final BoundsPoint other = (BoundsPoint) obj;

        return getProgress() == other.getProgress();
    }
}
