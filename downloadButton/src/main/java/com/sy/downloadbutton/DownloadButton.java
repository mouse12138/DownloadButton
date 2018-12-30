package com.sy.downloadbutton;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DownloadButton extends View {


    private long downTime;
    /**
     * 下载完成前
     */
    public static int NORMAL = 0;
    /**
     * 下载中
     */
    public static int DOWNLOAD = 1;
    /**
     * 下载完成
     */
    public static int FINISH = 2;
    /**
     * 下载结束（已下载）
     */
    public static int END = 3;

    /**
     * 按钮状态类型
     */
    private int btnType = NORMAL;
    /**
     * 最小大小
     */
    private int minSize = 100;
    /**
     * 控件宽
     */
    private int width;
    /**
     * 控件高
     */
    private int height;
    /**
     * 半径
     */
    private int radius;
    /**
     * 圆心x坐标
     */
    private int centerX;
    /**
     * 圆心y坐标
     */
    private int centerY;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 点击回调
     */
    private BtnClickListener btnClickListener;
    /**
     * 线条宽度
     */
    private float lineWidth = 5;
    /**
     * 线条颜色
     */
    private int lineColor = Color.BLACK;
    /**
     * 背景色
     */
    private int bgColor = Color.WHITE;
    /**
     * 下载完成前路径
     */
    private Path normalPath;
    /**
     * 箭头路径
     */
    private Path arrowPath;
    /**
     * 箭头水平夹角
     */
    private float arrowDegrees = 60;

    private PathMeasure downloadCirclePathMeasure;
    private Path downloadCirclePath;
    private Path downloadDst;

    /**
     * 下载中动画进度
     */
    private Float downloadValue = 0f;
    /**
     * 下载中动画持续时间
     */
    private int downloadAniDuration = 1500;
    /**
     * 下载完成动画进度
     */
    private Float finishValue = 0f;

    /**
     * 下载完成动画持续时间
     */
    private int finishAniDuration = 300;
    private int hookAniDuration = 300;
    /**
     * 下载动画
     */
    private ValueAnimator downloadAnimator;

    private Path finishDst;
    private Path finishCirclePath;
    private PathMeasure finishCirclePathMeasure;
    /**
     * 下载完成动画
     */
    private ValueAnimator finishAnimator;
    private ValueAnimator hookAnimator;
    private Paint circlePaint;


    /**
     * 下载完成动画结束监听
     */
    private HookAniFinishListener hookAniFinishListener;

    public DownloadButton(Context context) {
        super(context);
        init();
    }

    public DownloadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public DownloadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = minSize;
        int desiredHeight = minSize;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 如果控件宽度是指定大小，宽度为指定的尺寸
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        // 如果控件高度是指定大小，高度为指定的尺寸
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }
        // 设定控件大小
        setMeasuredDimension(width, height);

        radius = (int) (Math.min(width, height) / 2 - lineWidth);
        centerX = width / 2;
        centerY = height / 2;

        initPaths();

    }

    private void initPaths() {
        normalPath.reset();
        arrowPath.reset();
        downloadCirclePath.reset();
        finishCirclePath.reset();
        //下载完成前路径
        normalPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
        int top = (int) (height * 0.3);
        int bottom = (int) (height * 0.7);
        int arrowLength = (int) (height * 0.2);
        arrowPath.moveTo(width / 2, top);
        arrowPath.lineTo(width / 2, bottom);
        arrowPath.moveTo(width / 2, bottom);
        arrowPath.lineTo(width / 2 + (float) (Math.cos(Math.toDegrees(arrowDegrees)) * arrowLength), bottom - (float) (Math.sin(Math.toDegrees(arrowDegrees)) * arrowLength));
        arrowPath.moveTo(width / 2, bottom);
        arrowPath.lineTo(width / 2 - (float) (Math.cos(Math.toDegrees(arrowDegrees)) * arrowLength), bottom - (float) (Math.sin(Math.toDegrees(arrowDegrees)) * arrowLength));
        normalPath.addPath(arrowPath);

        //下载中路径
        downloadCirclePath.addCircle(centerX, centerY, radius, Path.Direction.CW);
        downloadCirclePathMeasure.setPath(downloadCirclePath, true);

        //下载结束路径
        finishCirclePath.addCircle(centerX, centerY, radius, Path.Direction.CW);
        finishCirclePath.moveTo(centerX - centerX / 2, centerY);
        finishCirclePath.lineTo(centerX, centerY + centerX / 2);
        finishCirclePath.lineTo(centerX + centerX / 2, centerY - centerX / 3);
        finishCirclePathMeasure.setPath(finishCirclePath, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (btnType == NORMAL) {
            drawNormalButton(canvas);
        } else if (btnType == DOWNLOAD) {
            drawDownloadButton(canvas);
        } else if (btnType == FINISH) {
            drawFinishButton(canvas);
        } else if (btnType == END) {
            drawEndButton(canvas);
        }
    }

    /**
     * 绘制下载结束路径
     *
     * @param canvas
     */
    private void drawEndButton(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.drawPath(finishCirclePath, paint);

    }

    /**
     * 绘制下载完成路径
     *
     * @param canvas
     */
    private void drawFinishButton(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        float stop = finishCirclePathMeasure.getLength() * finishValue;
        finishCirclePathMeasure.getSegment(0, stop, finishDst, true);
        canvas.drawPath(finishDst, paint);
    }


    private void init() {
        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        paint = new Paint();
        circlePaint = new Paint();
        initPaint();
        //初始化正常状态下路径
        //圆环路径
        normalPath = new Path();
        //箭头路径
        arrowPath = new Path();

        //下载中进度路径
        downloadDst = new Path();
        downloadCirclePath = new Path();
        downloadCirclePathMeasure = new PathMeasure();
        //下载进度动画
        downloadAnimator = ValueAnimator.ofFloat(0f, 1f);

        //下载完成路径
        finishDst = new Path();
        finishCirclePath = new Path();
        finishCirclePathMeasure = new PathMeasure();
        //下载完成动画
        finishAnimator = ValueAnimator.ofFloat(0f, 1f);
        hookAnimator = ValueAnimator.ofFloat(0f, 1f);

        initAni();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(lineColor);

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(bgColor);
        circlePaint.setStrokeWidth(lineWidth);

    }

    /**
     * 初始化动画
     */
    private void initAni() {
        //下载动画
        downloadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                downloadValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        downloadAnimator.setRepeatCount(ValueAnimator.INFINITE);
        downloadAnimator.setRepeatMode(ValueAnimator.RESTART);

        //下载完成动画
        finishAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                finishValue = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        finishAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finishCirclePathMeasure.nextContour();
                hookAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        finishValue = (Float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                hookAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (hookAniFinishListener != null) {
                            hookAniFinishListener.finished();
                        }
                        setBtnType(END);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                });
                hookAnimator.setDuration(hookAniDuration);
                hookAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

        });
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long time = System.currentTimeMillis() - downTime;
                if (time > 0 && time < 1500) {
                    if (btnClickListener != null) {
                        btnClickListener.btnClick();
                    }
                    postInvalidate();
                }
                break;
        }
        return true;
    }

    /**
     * 绘制下载完成前路径
     *
     * @param canvas
     */
    private void drawNormalButton(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.drawPath(normalPath, paint);

    }

    /**
     * 绘制下载中路径
     *
     * @param canvas
     */
    private void drawDownloadButton(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        downloadDst.reset();
        //每次绘制的结束点
        float stop = downloadCirclePathMeasure.getLength() * downloadValue;
        float start = (float) (stop - ((0.5 - Math.abs(downloadValue - 0.5)) * downloadCirclePathMeasure.getLength()));
        downloadCirclePathMeasure.getSegment(start, stop, downloadDst, true);
        canvas.drawPath(downloadDst, paint);

    }

    /**
     * 开始下载动画
     */
    private void startDownloadAni() {
        downloadAnimator.setDuration(downloadAniDuration);
        downloadAnimator.start();
    }


    public int getBtnType() {
        return btnType;
    }

    public void setBtnType(int btnType) {
        this.btnType = btnType;
        cancelAllAni();
        if (btnType == NORMAL) {
            invalidate();
        } else if (btnType == DOWNLOAD) {
            startDownloadAni();
            invalidate();
        } else if (btnType == FINISH) {
            startFinishAni();
            invalidate();
        } else if (btnType == END) {
            invalidate();
        }
    }

    /**
     * 开始下载完成动画
     */
    private void startFinishAni() {
        finishDst.reset();
        finishCirclePathMeasure.setPath(finishCirclePath, false);
        finishAnimator.setDuration(finishAniDuration);
        finishAnimator.start();
    }

    private void cancelAllAni() {
        if (downloadAnimator != null) {
            downloadAnimator.cancel();
        }
        if (finishAnimator != null) {
            finishAnimator.cancel();
        }
        if (hookAnimator != null) {
            hookAnimator.cancel();
        }
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        initPaint();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        initPaint();
    }

    public float getArrowDegrees() {
        return arrowDegrees;
    }

    public void setArrowDegrees(float arrowDegrees) {
        this.arrowDegrees = arrowDegrees;
    }

    public int getDownloadAniDuration() {
        return downloadAniDuration;
    }

    public void setDownloadAniDuration(int downloadAniDuration) {
        this.downloadAniDuration = downloadAniDuration;
    }

    public int getFinishAniDuration() {
        return finishAniDuration;
    }

    public void setFinishAniDuration(int finishAniDuration) {
        this.finishAniDuration = finishAniDuration;
    }

    public int getHookAniDuration() {
        return hookAniDuration;
    }

    public void setHookAniDuration(int hookAniDuration) {
        this.hookAniDuration = hookAniDuration;
    }

    public BtnClickListener getBtnClickListener() {
        return btnClickListener;
    }

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public HookAniFinishListener getHookAniFinishListener() {
        return hookAniFinishListener;
    }

    public void setHookAniFinishListener(HookAniFinishListener hookAniFinishListener) {
        this.hookAniFinishListener = hookAniFinishListener;
    }

    public interface BtnClickListener {
        void btnClick();
    }


    public interface HookAniFinishListener {
        void finished();
    }
}
