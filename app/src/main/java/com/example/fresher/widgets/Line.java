package com.example.fresher.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Line extends View {

    private Paint paint;

    public Line(Context context) {

        super(context);

        init();

    }

    public Line(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        init();

    }

    public Line(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init();

    }

    public Line(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawLine(0, 0, 100, 100, paint);

    }

    private void init() {

        paint = new Paint();

        paint.setColor(Color.GREEN);

        paint.setStrokeWidth(10);

    }

}