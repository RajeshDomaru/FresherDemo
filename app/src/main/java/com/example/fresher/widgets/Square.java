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

    public Square(Context context) {

        super(context);

    }

    public Square(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }

    public Square(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        Rect rect = new Rect();

        rect.top = 10;
        rect.left = 10;

        rect.right = 100;
        rect.bottom = 100;

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawRect(rect, paint);

    }

}