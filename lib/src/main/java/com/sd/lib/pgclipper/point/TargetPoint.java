package com.sd.lib.pgclipper.point;

public class TargetPoint extends ClipPoint
{
    private boolean mIsSticky;

    public TargetPoint(int progress)
    {
        super(progress);
    }

    public boolean isSticky()
    {
        return mIsSticky;
    }

    public void setSticky(boolean sticky)
    {
        mIsSticky = sticky;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        if (!(obj instanceof TargetPoint))
            return false;

        final TargetPoint other = (TargetPoint) obj;

        return getProgress() == other.getProgress();
    }
}
