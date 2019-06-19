package com.sd.lib.pgclipper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.sd.lib.pgclipper.ProgressClipper;
import com.sd.lib.pgclipper.SimpleProgressClipper;
import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.ClipPoint;
import com.sd.lib.pgclipper.point.TargetPoint;

import java.util.List;

public class FClipProgressBar extends View implements ProgressClipper
{
    private SimpleProgressClipper mClipper;

    private int mColorBackground;
    private int mColorProgress;

    private final Paint mPaint = new Paint();

    private List<TargetPoint> mTargetPoints;
    private List<BoundsPoint> mBoundsPoints;

    public FClipProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mPaint.setAntiAlias(true);

        setColorBackground(Color.parseColor("#999999"));

        final TypedArray array = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
        final int colorAccent = array.getColor(0, Color.RED);
        setColorProgress(colorAccent);
    }

    /**
     * 设置进度条背景颜色
     *
     * @param color
     */
    public void setColorBackground(int color)
    {
        mColorBackground = color;
    }

    /**
     * 设置进度颜色
     *
     * @param color
     */
    public void setColorProgress(int color)
    {
        mColorProgress = color;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(mColorBackground);

        final int totalSize = getMeasuredWidth();
        final int currentProgress = getClipper().getProgress();

        // draw target
        final List<TargetPoint> listTarget = mTargetPoints;
        if (listTarget != null)
        {
            for (TargetPoint item : listTarget)
            {
                if (item.getProgress() <= currentProgress)
                {
                    if (!item.isSticky())
                        getClipper().removeTargetPoint(item.getProgress());
                    continue;
                }
                drawPoint(item, canvas, mPaint);
            }
        }

        // draw progress
        final float progressPercent = getClipper().getProgressPercent(currentProgress);
        final int progressEnd = (int) (progressPercent * totalSize);

        mPaint.setColor(mColorProgress);
        canvas.drawRect(0, 0, progressEnd, getHeight(), mPaint);

        // draw bound
        final List<BoundsPoint> listBound = mBoundsPoints;
        if (listBound != null)
        {
            final int count = listBound.size();
            for (int i = count - 1; i >= 0; i--)
            {
                final BoundsPoint item = listBound.get(i);
                if (currentProgress < item.getProgress())
                {
                    getClipper().removeBoundsPoint(item.getProgress());
                    continue;
                }

                int start = 0;

                final BoundsPoint itemPre = i > 0 ? listBound.get(i - 1) : null;
                if (itemPre != null)
                {
                    start = getPointStart(itemPre, totalSize);
                }
                final int end = getPointStart(item, totalSize);

                int color = 0;
                if (item.isDeleted())
                {
                    color = mColorBackground;
                } else
                {
                    if (item.isSelected())
                    {
                        color = item.getSelectedColor();
                    } else
                    {
                        color = item.getBoundColor();
                    }

                    if (color == 0)
                        color = mColorProgress;
                }
                mPaint.setColor(color);
                canvas.drawRect(start, 0, end, getHeight(), mPaint);

                drawPoint(itemPre, canvas, mPaint);
                drawPoint(item, canvas, mPaint);
            }
        }
    }

    private void drawPoint(ClipPoint point, Canvas canvas, Paint paint)
    {
        if (point == null)
            return;

        final int start = getPointStart(point, getMeasuredWidth());
        final int end = start + point.getDisplaySize();

        paint.setColor(point.getDisplayColor());
        canvas.drawRect(start, 0, end, getHeight(), paint);
    }

    private int getPointStart(ClipPoint point, int totalSize)
    {
        if (totalSize <= 0)
            return 0;

        final float percent = getClipper().getProgressPercent(point.getProgress());
        return (int) (percent * totalSize);
    }

    private SimpleProgressClipper getClipper()
    {
        if (mClipper == null)
        {
            mClipper = new SimpleProgressClipper()
            {
                @Override
                protected void updateUIImpl(List<TargetPoint> targetPoints, List<BoundsPoint> boundsPoints)
                {
                    mTargetPoints = targetPoints;
                    mBoundsPoints = boundsPoints;
                    FClipProgressBar.this.invalidate();
                }
            };
        }
        return mClipper;
    }

    @Override
    public int getProgress()
    {
        return getClipper().getProgress();
    }

    @Override
    public int getMax()
    {
        return getClipper().getMax();
    }

    @Override
    public float getProgressPercent(int progress)
    {
        return getClipper().getProgressPercent(progress);
    }

    @Override
    public List<TargetPoint> getTargetPoint()
    {
        return getClipper().getTargetPoint();
    }

    @Override
    public TargetPoint getLastTargetPoint()
    {
        return getClipper().getLastTargetPoint();
    }

    @Override
    public List<BoundsPoint> getBoundsPoint()
    {
        return getClipper().getBoundsPoint();
    }

    @Override
    public BoundsPoint getLastBoundsPoint()
    {
        return getClipper().getLastBoundsPoint();
    }

    @Override
    public void setProgress(int progress)
    {
        getClipper().setProgress(progress);
    }

    @Override
    public void setMax(int max)
    {
        getClipper().setMax(max);
    }

    @Override
    public void addTargetPoint(TargetPoint point)
    {
        getClipper().addTargetPoint(point);
    }

    @Override
    public void removeTargetPoint(int progress)
    {
        getClipper().removeTargetPoint(progress);
    }

    @Override
    public void clearTargetPoint()
    {
        getClipper().clearTargetPoint();
    }

    @Override
    public void addBoundsPoint(BoundsPoint point)
    {
        getClipper().addBoundsPoint(point);
    }

    @Override
    public void removeBoundsPoint(int progress)
    {
        getClipper().removeBoundsPoint(progress);
    }

    @Override
    public void clearBoundsPoint()
    {
        getClipper().clearBoundsPoint();
    }

    @Override
    public void updateUI()
    {
        getClipper().updateUI();
    }
}
