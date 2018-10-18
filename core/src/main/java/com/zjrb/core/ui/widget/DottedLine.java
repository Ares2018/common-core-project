package com.zjrb.core.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.zjrb.core.R;
import com.zjrb.core.utils.UIUtils;

/**
 * 自定义虚线 view
 *
 * @author a_liYa
 * @date 2016/5/28 13:46.
 */
public class DottedLine extends View {

    private Paint mPaint = null;
    private Path path = null;
    private PathEffect pe = null;

    private int dotted_width; // 虚线宽度 默认2dp
    private int dotted_dap; // 虚线间距 默认2dp

    private int dotted_color; // 虚线颜色

    private int orientation = 0; // 横向:0 | 竖向:1

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final String DOTTED_COLOR_DEF = "#dddddd";

    public static final int[] ORIENTATION = {HORIZONTAL, VERTICAL};

    public DottedLine(Context context) {
        this(context, null);
    }

    public DottedLine(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DottedLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DottedLine);

            dotted_color = a.getColor(R.styleable.DottedLine_dotted_color, Color.parseColor
                    (DOTTED_COLOR_DEF));
            dotted_width = a.getDimensionPixelSize(R.styleable.DottedLine_dotted_width, UIUtils
                    .dip2px(2.0f));
            dotted_dap = a.getDimensionPixelSize(R.styleable.DottedLine_dotted_dap, UIUtils
                    .dip2px(2.0f));
            orientation = ORIENTATION[a.getInt(R.styleable.DottedLine_orientation, 0)];

            a.recycle();

            this.mPaint = new Paint();
            this.path = new Path();
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(dotted_color);
            this.mPaint.setAntiAlias(true);

            float[] arrayOfFloat = new float[4];
            arrayOfFloat[0] = dotted_width;
            arrayOfFloat[1] = dotted_dap;
            this.pe = new DashPathEffect(arrayOfFloat, UIUtils.dip2px(1.0F));
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        switch (orientation) {
            case HORIZONTAL:
                mPaint.setStrokeWidth(h);
                break;
            case VERTICAL:
                mPaint.setStrokeWidth(w);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(0.0F, 0.0F);
        switch (orientation) {
            case HORIZONTAL:
                path.lineTo(getMeasuredWidth(), 0.0F);
                break;
            case VERTICAL:
                path.lineTo(0.0F, getMeasuredHeight());
                break;
        }
        mPaint.setPathEffect(pe);
        canvas.drawPath(path, mPaint);
    }
}
