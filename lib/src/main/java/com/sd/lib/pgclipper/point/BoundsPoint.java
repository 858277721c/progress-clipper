package com.sd.lib.pgclipper.point;

import android.graphics.Color;

import java.util.Comparator;

public class BoundsPoint extends ClipPoint implements Comparable<BoundsPoint>
{
    private int mBoundColor;
    private int mSelectedColor;
    private boolean mIsDeleted;
    private boolean mIsSelected;

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

    public boolean isDeleted()
    {
        return mIsDeleted;
    }

    public void setDeleted(boolean deleted)
    {
        mIsDeleted = deleted;
    }

    public boolean isSelected()
    {
        return mIsSelected;
    }

    public void setSelected(boolean selected)
    {
        mIsSelected = selected;
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

    @Override
    public int compareTo(BoundsPoint o)
    {
        return getProgress() - o.getProgress();
    }

    public static final Comparator<BoundsPoint> COMPARATOR = new Comparator<BoundsPoint>()
    {
        @Override
        public int compare(BoundsPoint o1, BoundsPoint o2)
        {
            return o1.getProgress() - o2.getProgress();
        }
    };
}
