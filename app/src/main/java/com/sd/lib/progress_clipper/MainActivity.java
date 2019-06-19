package com.sd.lib.progress_clipper;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.lib.pgclipper.ProgressClipper;
import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.TargetPoint;
import com.sd.lib.pgclipper.view.FClipProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int COLOR_DELETE = Color.RED;

    private FClipProgressBar mProgressBar;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.view_progress);
        mListView = findViewById(R.id.lv_bound);
        mListView.setAdapter(mAdapter);
        reset();

        // 设置背景颜色
        mProgressBar.setColorBackground(Color.parseColor("#999999"));
        // 设置进度颜色
        mProgressBar.setColorProgress(getResources().getColor(R.color.colorPrimary));
        // 设置最大进度值
        mProgressBar.setMax(100);

        // 设置边界点变化监听
        mProgressBar.setOnBoundsPointChangeCallback(new ProgressClipper.OnBoundsPointChangeCallback()
        {
            @Override
            public void onBoundsPointChanged(int size)
            {
                updateAdapter();
            }
        });
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
                // 创建一个边界点
                final BoundsPoint point = new BoundsPoint(mProgressBar.getProgress());
                // 设置边界点的颜色
                point.setDisplayColor(Color.WHITE);
                // 设置边界分段的颜色
                point.setBoundColor(Color.GREEN);
                // 将边界点添加到进度条
                mProgressBar.addBoundsPoint(point);

                // 刷新UI
                mProgressBar.updateUI();
                break;
            case R.id.btn_delete_bound:
                final BoundsPoint last = mProgressBar.getLastBoundsPoint();
                if (last != null)
                {
                    if (last.getBoundColor() == COLOR_DELETE)
                    {
                        mProgressBar.removeBoundsPoint(last.getProgress());
                    } else
                    {
                        last.setBoundColor(COLOR_DELETE);
                        mProgressBar.updateUI();
                    }
                }
                break;
        }
    }

    private final FSimpleAdapter<BoundsPoint> mAdapter = new FSimpleAdapter<BoundsPoint>()
    {
        @Override
        public int getLayoutId(int position, View convertView, ViewGroup parent)
        {
            return R.layout.item_bounds;
        }

        @Override
        public void onBindData(int position, View convertView, ViewGroup parent, final BoundsPoint model)
        {
            final TextView tv_name = get(R.id.tv_name, convertView);
            tv_name.setText("分段" + (position + 1));
        }
    };

    private void reset()
    {
        cancelAnimator();
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);

        // 清空所有目标点
        mProgressBar.clearTargetPoint();

        // 在进度为20的地方创建一个目标点，当进度到达目标点后，目标点会被进度覆盖
        final TargetPoint point = new TargetPoint(20);
        // 设置目标点大小，默认1dp
        point.setDisplaySize(5);
        // 设置目标点颜色，默认白色
        point.setDisplayColor(Color.WHITE);
        // true-当进度到达目标点后，目标点会被进度覆盖但是不会被清除，即进度小于目标点进度的时候目标点又可以被看到；false-会被清除掉
        point.setSticky(true);
        // 添加目标点
        mProgressBar.addTargetPoint(point);
        mProgressBar.addTargetPoint(new TargetPoint(50));

        // 清空所有边界点
        mProgressBar.clearBoundsPoint();
    }

    private void updateAdapter()
    {
        mAdapter.getDataHolder().setData(mProgressBar.getBoundsPoint());
    }

    private ValueAnimator mAnimator;

    private ValueAnimator getAnimator()
    {
        if (mAnimator == null)
        {
            mAnimator = new ValueAnimator();
            mAnimator.setDuration(10 * 1000);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    final Integer value = (Integer) animation.getAnimatedValue();
                    mProgressBar.setProgress(value);
                }
            });
        }
        return mAnimator;
    }

    private void startAnimator()
    {
        cancelAnimator();
        getAnimator().setIntValues(mProgressBar.getProgress(), mProgressBar.getMax());
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
