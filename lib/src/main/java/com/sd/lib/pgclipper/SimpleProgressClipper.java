package com.sd.lib.pgclipper;

import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.TargetPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class SimpleProgressClipper implements ProgressClipper
{
    private final TreeMap<Integer, TargetPoint> mTargetHolder;
    private final TreeMap<Integer, BoundsPoint> mBoundsHolder;

    private int mMax;
    private int mProgress;

    private OnBoundsPointCountChangeCallback mOnBoundsPointCountChangeCallback;

    public SimpleProgressClipper()
    {
        mTargetHolder = new TreeMap<>(mIntegerComparator);
        mBoundsHolder = new TreeMap<>(mIntegerComparator);
    }

    private final Comparator<Integer> mIntegerComparator = new Comparator<Integer>()
    {
        @Override
        public int compare(Integer o1, Integer o2)
        {
            return o1 - o2;
        }
    };

    @Override
    public int getProgress()
    {
        return mProgress;
    }

    @Override
    public int getMax()
    {
        return mMax;
    }

    @Override
    public float getProgressPercent(int progress)
    {
        if (progress < 0)
            progress = 0;
        return mMax == 0 ? 0.0f : (float) progress / mMax;
    }

    @Override
    public List<TargetPoint> getTargetPoint()
    {
        return new ArrayList<>(mTargetHolder.values());
    }

    @Override
    public TargetPoint getLastTargetPoint()
    {
        final Map.Entry<Integer, TargetPoint> last = mTargetHolder.lastEntry();
        return last == null ? null : last.getValue();
    }

    @Override
    public List<BoundsPoint> getBoundsPoint()
    {
        return new ArrayList<>(mBoundsHolder.values());
    }

    @Override
    public BoundsPoint getLastBoundsPoint()
    {
        final Map.Entry<Integer, BoundsPoint> last = mBoundsHolder.lastEntry();
        return last == null ? null : last.getValue();
    }

    @Override
    public void setProgress(int progress)
    {
        progress = safeProgress(progress);
        if (mProgress != progress)
        {
            mProgress = progress;

            if (mProgress == 0)
            {
                mBoundsHolder.clear();
                if (mOnBoundsPointCountChangeCallback != null)
                    mOnBoundsPointCountChangeCallback.onBoundsPointCountChanged(mBoundsHolder.size());
            }

            updateUI();
        }
    }

    private int safeProgress(int progress)
    {
        if (progress < 0)
            progress = 0;

        if (progress > mMax)
            progress = mMax;

        return progress;
    }

    @Override
    public void setMax(int max)
    {
        if (max < 0)
            max = 0;

        if (mMax != max)
        {
            mMax = max;
            updateUI();
        }
    }

    @Override
    public void addTargetPoint(TargetPoint point)
    {
        final int progress = point.getProgress();
        if (progress < 0 || progress > mMax)
            throw new IllegalArgumentException("progress out of range");

        if (mTargetHolder.containsKey(progress))
            return;

        mTargetHolder.put(progress, point);
        updateUI();
    }

    @Override
    public void removeTargetPoint(int progress)
    {
        if (mTargetHolder.remove(progress) != null)
            updateUI();
    }

    @Override
    public void clearTargetPoint()
    {
        mTargetHolder.clear();
        updateUI();
    }

    @Override
    public void setOnBoundsPointCountChangeCallback(OnBoundsPointCountChangeCallback callback)
    {
        mOnBoundsPointCountChangeCallback = callback;
    }

    @Override
    public BoundsPoint addBoundsPoint()
    {
        final int progress = getProgress();
        if (progress == 0)
            return null;

        if (mBoundsHolder.containsKey(progress))
            return null;

        final BoundsPoint point = new BoundsPoint(progress);
        mBoundsHolder.put(progress, point);
        updateUI();

        if (mOnBoundsPointCountChangeCallback != null)
            mOnBoundsPointCountChangeCallback.onBoundsPointCountChanged(mBoundsHolder.size());

        return point;
    }

    @Override
    public void removeBoundsPoint(int progress)
    {
        if (progress < 0 || progress > mMax)
            throw new IllegalArgumentException("progress out of range");

        if (mBoundsHolder.remove(progress) != null)
        {
            updateUI();
            if (mOnBoundsPointCountChangeCallback != null)
                mOnBoundsPointCountChangeCallback.onBoundsPointCountChanged(mBoundsHolder.size());
        }
    }

    @Override
    public final void updateUI()
    {
        updateUIImpl(getTargetPoint(), getBoundsPoint());
    }

    protected abstract void updateUIImpl(List<TargetPoint> targetPoints, List<BoundsPoint> boundsPoints);
}
