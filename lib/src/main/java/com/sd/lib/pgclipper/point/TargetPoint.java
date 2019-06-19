package com.sd.lib.pgclipper.point;

public class TargetPoint extends ClipPoint
{
    private final boolean mIsSticky;

    public TargetPoint(int progress, boolean sticky)
    {
        super(progress);
        mIsSticky = sticky;
    }

    public boolean isSticky()
    {
        return mIsSticky;
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
