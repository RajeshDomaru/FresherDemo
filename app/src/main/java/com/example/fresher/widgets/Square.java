package com.example.fresher.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Square extends View {

    private Rect rect;

    private Paint paint;

    public Square(Context context) {

        super(context);

        init();

    }

    public Square(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        init();

    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init();

    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        init();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawRect(rect, paint);

    }

    private void init() {

        rect = new Rect();

        int top = 10;
        int left = 10;

        int right = 100;
        int bottom = 100;

        rect.top = top;
        rect.left = left;

        rect.right = right;
        rect.bottom = bottom;

        paint = new Paint();
        paint.setColor(Color.RED);

    }

}