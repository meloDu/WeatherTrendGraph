package com.smart.melo.weathertrendgraph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class WeatherTrendView extends View {
    private ArrayList<WeatherItem> items;
    private String unit;
    private String yFormat = "0";

    private Context context;
    //点的宽度
    private float pointWidth;
    //点的颜色
    private int pointColor=Color.parseColor("#aa0000");
    //线的宽度
    private float lineWidth;
    //线的颜色
    private int lineColor=Color.parseColor("#aa0000");
    //文字
    private Paint mTextPaint;
    //画单位
    private Paint mUnitPaint;

    //画线
    private Paint mLinePaint;

    // 画点
    private Paint mPointPaint;

    public void SetTuView(ArrayList<WeatherItem> list, String unitInfo) {
        this.items = list;
        this.unit = unitInfo;
        invalidate();
    }

    public WeatherTrendView(Context ct) {
        super(ct);
        this.context = ct;
        initPaint();
        Log.i("tag","initPaint1");
    }

    public WeatherTrendView(Context ct, AttributeSet attrs) {
        super(ct, attrs);
        this.context = ct;
        initAttrs(attrs);
        initPaint();

        Log.i("tag","initPaint2");

    }

    public WeatherTrendView(Context ct, AttributeSet attrs, int defStyle) {
        super(ct, attrs, defStyle);
        Log.i("tag","initPaint3");
        this.context = ct;
        initAttrs(attrs);
        initPaint();
    }


    //初始化画笔
    public void initPaint() {
        Log.i("tag","initPaint");
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.parseColor("#FF4081"));//#7fffffff
        mTextPaint.setStrokeWidth(4);
        mTextPaint.setStyle(Style.STROKE);

        // 画单位
        mUnitPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 抗锯齿,抗抖动
//        p.setAlpha(0x0000ff);
        mUnitPaint.setTextSize(sp2px(context, 15));
        mUnitPaint.setColor(Color.parseColor("#FF4081"));

        //画线
        mLinePaint =  new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 抗锯齿,抗抖动
        mLinePaint.setColor(lineColor);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth);//8
        //画点
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); // 抗锯齿,抗抖动
        mPointPaint.setColor(pointColor);
        mPointPaint.setStyle(Style.FILL);
        mPointPaint.setStrokeWidth(pointWidth);
    }


    public void initAttrs(AttributeSet attrs) {
        Log.i("tag","initAttrs");
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WeatherLineView);
        lineWidth = (float) array.getDimension(R.styleable.WeatherLineView_lineWidth, dip2px(context,10));
        lineColor = array.getColor(R.styleable.WeatherLineView_lineColor, Color.BLUE);
        Log.i("tag",lineColor+"");
        pointWidth = (float) array.getDimension(R.styleable.WeatherLineView_pointWidth, dip2px(context,10));
        pointColor = array.getColor(R.styleable.WeatherLineView_pointColor, Color.parseColor("#FF4081"));
        array.recycle();


    }


    private Canvas cacheCanvas;// 创建画布、画家


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (items == null) {
            return;
        }

        //屏幕高
        int height = getHeight();
        //屏幕宽
        int width = getWidth();
        int split = dip2px(context, 8);
        int marginl = width / 24;//12
        int margint = dip2px(context, 60);
        int margint2 = dip2px(context, 25);
        int bheight = height - margint - 2 * split;
        // 画单位
        canvas.drawText(unit, split, margint2 , mUnitPaint);
        // 计算每天的水平坐标x轴
        ArrayList<Integer> list = new ArrayList<Integer>();
        mTextPaint.setColor(Color.parseColor("#FF4081"));
        for (int i = 0; i < items.size(); i++) {
            //计算每天item的宽度
            int itemWidth = (width - 2 * marginl) / items.size();
            //计算每个item的水平中点
            int x = marginl + itemWidth / 2 + itemWidth * i;
            list.add(x);
            drawText(items.get(i).getX(), x, 700, canvas);
        }

        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < items.size(); i++) {
            float y = items.get(i).getY();
            if (y > max) {
                max = y;
            }
            if (y < min) {
                min = y;
            }
        }

        float span = max - min;
        if (span == 0) {
            span = 6.0f;
        }
        max = max + span / 6.0f;
        min = min - span / 6.0f;

        // 获取点集合
        Point[] mPoints = getPoints(list, max, min, bheight, margint);
        // 画线
        drawLine(mPoints, canvas, mLinePaint);

        // 画点
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, 8, mPointPaint);//radius=12
            String yText = new java.text.DecimalFormat(yFormat).format(items
                    .get(i).getY());
            drawText(yText + "°", mPoints[i].x,
                    mPoints[i].y - dip2px(context, 12), canvas);
        }
    }

    private Point[] getPoints(ArrayList<Integer> xlist, float max, float min,
                              int h, int top) {
        Point[] points = new Point[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int ph = top + h
                    - (int) (h * ((items.get(i).getY() - min) / (max - min)));
            points[i] = new Point(xlist.get(i), ph);
        }
        return points;
    }

    private void drawLine(Point[] ps, Canvas canvas, Paint paint) {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < ps.length - 1; i++) {
            startp = ps[i];
            endp = ps[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, paint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas) {
        Paint p = new Paint();
//        p.setAlpha(0x0000ff);
        p.setTextSize(sp2px(context, 15));
        p.setTextAlign(Paint.Align.CENTER);
        p.setColor(Color.parseColor("#FF4081"));
        canvas.drawText(text, x, y, p);
    }

    public ArrayList<WeatherItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<WeatherItem> items) {
        this.items = items;
    }

    public String getyFormat() {
        return yFormat;
    }

    public void setyFormat(String yFormat) {
        this.yFormat = yFormat;
    }

    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
