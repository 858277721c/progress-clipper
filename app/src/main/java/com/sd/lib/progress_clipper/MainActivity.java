package com.sd.lib.progress_clipper;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.TargetPoint;
import com.sd.lib.pgclipper.view.FClipProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private FClipProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressView = findViewById(R.id.view_progress);
        reset();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_animator:
                if (cancelAnimator())
                {
                } else
                {
                    startAnimator();
                }
                break;
            case R.id.btn_add_bound:
                cancelAnimator();
                final BoundsPoint boundPoint = new BoundsPoint(mProgressView.getProgress());
                boundPoint.setDisplayColor(Color.YELLOW);
                boundPoint.setBoundColor(Color.GREEN);
                boundPoint.setSelectedColor(Color.RED);
                mProgressView.addBoundsPoint(boundPoint);
                break;
        }
    }

    private void reset()
    {
        cancelAnimator();
        mProgressView.setProgress(0);
        mProgressView.setMax(100);
        mProgressView.addTargetPoint(new TargetPoint(10, true));
        mProgressView.addTargetPoint(new TargetPoint(20, false));
        mProgressView.addTargetPoint(new TargetPoint(50, false));
    }

    private ValueAnimator mAnimator;

    private ValueAnimator getAnimator()
    {
        if (mAnimator == null)
        {
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(5 * 1000);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    mProgressView.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        }
        return mAnimator;
    }

    private void startAnimator()
    {
        cancelAnimator();
        getAnimator().setIntValues(mProgressView.getProgress(), mProgressView.getMax());
        getAnimator().start();
    }

    private boolean cancelAnimator()
    {
        if (mAnimator != null && mAnimator.isStarted())
        {
            mAnimator.cancel();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        cancelAnimator();
    }
}
