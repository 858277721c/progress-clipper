package com.sd.lib.pgclipper.point;

public class BoundsPoint extends ClipPoint
{
    private int mBoundColor;

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
