# Gradle
[![](https://jitpack.io/v/zj565061763/progress-clipper.svg)](https://jitpack.io/#zj565061763/progress-clipper)

# Demo
![](https://github.com/zj565061763/progress-clipper/blob/master/screenshot/fclipprogressbar.gif?raw=true)

```java
// 设置背景颜色
mProgressBar.setColorBackground(Color.parseColor("#999999"));
// 设置进度颜色
mProgressBar.setColorProgress(getResources().getColor(R.color.colorPrimary));
// 设置最大进度值
mProgressBar.setMax(100);
```

目标点：
```java
// 在进度为20的地方创建一个目标点，当进度到达目标点后，目标点会被进度覆盖
final TargetPoint point = new TargetPoint(20);
// 设置目标点大小，默认1dp
point.setDisplaySize(10);
// 设置目标点颜色，默认白色
point.setDisplayColor(Color.WHITE);
// true-当进度到达目标点后，目标点会被进度覆盖但是不会被清除，即进度小于目标点进度的时候目标点又可以被看到；false-会被清除掉
point.setSticky(true);
// 添加目标点
mProgressBar.addTargetPoint(point);

// 移除目标点
mProgressBar.removeTargetPoint(point.getProgress());

// 清空所有目标点
mProgressBar.clearTargetPoint();
```

边界点：
```java
// 创建一个边界点，边界点会显示在进度的上层，不会被进度覆盖
final BoundsPoint point = new BoundsPoint(mProgressBar.getProgress());
// 设置边界分段的颜色
point.setBoundColor(Color.GREEN);
// 将边界点添加到进度条
mProgressBar.addBoundsPoint(point);
```

# 接口
```java
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
     * 同步进度到某个点
     *
     * @param point
     */
    void synchronizeProgressToPoint(ClipPoint point);

    /**
     * 同步进度到最后一个边界点，如果没有边界点，则进度设置为0
     */
    void synchronizeProgressToLastBoundsPoint();

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
```