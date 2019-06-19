package com.sd.lib.pgclipper;

import com.sd.lib.pgclipper.point.BoundsPoint;
import com.sd.lib.pgclipper.point.TargetPoint;

import java.util.List;

public interface ProgressClipper
{
    /**
     * 返回当前进度
     *
     * @return
     */
    int getProgress();

    /**
     * 返回最大值
     *
     * @return
     */
    int getMax();

    /**
     * 返回某个值处于总进度的百分多少[0-1]
     *
     * @return [0-1]
     */
    float getProgressPercent(int progress);

    /**
     * 返回目标点
     *
     * @return
     */
    List<TargetPoint> getTargetPoint();

    /**
     * 返回最后一个目标点
     *
     * @return
     */
    TargetPoint getLastTargetPoint();

    /**
     * 返回边界点
     *
     * @return
     */
    List<BoundsPoint> getBoundsPoint();

    /**
     * 返回最后一个边界点
     *
     * @return
     */
    BoundsPoint getLastBoundsPoint();

    /**
     * 设置进度值
     *
     * @param progress
     */
    void setProgress(int progress);

    /**
     * 设置最大值
     *
     * @param max
     */
    void setMax(int max);

    /**
     * 添加目标点
     *
     * @param point
     */
    void addTargetPoint(TargetPoint point);

    /**
     * 移除目标点
     *
     * @param progress
     */
    void removeTargetPoint(int progress);

    /**
     * 移除所有目标点
     */
    void clearTargetPoint();

    /**
     * 边界点变化监听
     *
     * @param callback
     */
    void setOnBoundsPointChangeCallback(OnBoundsPointChangeCallback callback);

    /**
     * 添加边界点
     *
     * @param point
     */
    void addBoundsPoint(BoundsPoint point);

    /**
     * 移除边界点
     *
     * @param progress
     */
    void removeBoundsPoint(int progress);

    /**
     * 清空所有边界点
     */
    void clearBoundsPoint();

    /**
     * 更新UI
     */
    void updateUI();

    interface OnBoundsPointChangeCallback
    {
        /**
         * 边界点变化监听
         *
         * @param size
         */
        void onBoundsPointChanged(int size);
    }
}
