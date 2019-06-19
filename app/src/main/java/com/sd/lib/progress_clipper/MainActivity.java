package com.sd.lib.progress_clipper;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.sd.lib.adapter.FSimpleAdapter;
import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.TargetPoint;
import com.sd.lib.pgclipper.view.FClipProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
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
                cancelAnimator();

                // 创建一个边界点
                final BoundsPoint point = new BoundsPoint(mProgressBar.getProgress());
                // 设置边界点的颜色
                point.setDisplayColor(Color.WHITE);
                // 设置边界分段的颜色
                point.setBoundColor(Color.GREEN);
                // 设置边界分段选中的颜色
                point.setSelectedColor(Color.RED);
                // 设置边界分段是否为选中状态
                point.setSelected(false);
                // 设置边界分段是否为删除状态
                point.setDeleted(false);

                // 将边界点添加到进度条
                mProgressBar.addBoundsPoint(point);

                mAdapter.getDataHolder().addData(point);
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

            final CheckBox cb_select = get(R.id.cb_select, convertView);
            cb_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // 切换分段选中状态
                    model.setSelected(isChecked);
                    mProgressBar.updateUI();
                }
            });
            cb_select.setChecked(model.isSelected());

            final CheckBox cb_delete = get(R.id.cb_delete, convertView);
            cb_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // 切换分段删除状态
                    model.setDeleted(isChecked);
                    mProgressBar.updateUI();
                }
            });
            cb_delete.setChecked(model.isDeleted());
        }
    };

    private void reset()
    {
        cancelAnimator();
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        mProgressBar.addTargetPoint(new TargetPoint(20, true));
        mProgressBar.addTargetPoint(new TargetPoint(50, false));
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
                    mProgressBar.setProgress((Integer) animation.getAnimatedValue());
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
