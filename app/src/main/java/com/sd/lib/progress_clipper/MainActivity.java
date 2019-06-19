package com.sd.lib.progress_clipper;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sd.lib.adapter.FSimpleAdapter;
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
                if (mProgressBar.getProgress() == 0)
                {
                    Toast.makeText(this, "不能在进度为0的时候添加边界点", Toast.LENGTH_SHORT).show();
                } else
                {
                    // 创建一个边界点
                    final BoundsPoint point = mProgressBar.addBoundsPoint();
                    // 设置边界点的颜色
                    point.setDisplayColor(Color.WHITE);
                    // 设置边界分段的颜色
                    point.setBoundColor(Color.GREEN);
                    // 刷新UI
                    mProgressBar.updateUI();

                    updateAdapter();
                }
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
        mProgressBar.addTargetPoint(new TargetPoint(20));
        mProgressBar.addTargetPoint(new TargetPoint(50));

        updateAdapter();
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
