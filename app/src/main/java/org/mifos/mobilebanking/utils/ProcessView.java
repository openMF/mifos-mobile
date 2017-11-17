package org.mifos.mobilebanking.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import org.mifos.mobilebanking.R;

/**
 * Created by dilpreet on 30/6/17.
 */

public class ProcessView extends View {

    private String valueStr;
    private Paint textPaint, backgroundPaint;

    public ProcessView(Context context) {
        super(context, null);
    }

    public ProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProcessView);
        valueStr = typedArray.getString(R.styleable.ProcessView_value);

        typedArray.recycle();

        textPaint = new Paint();
        textPaint.setColor(getColorCompat(android.R.color.white));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(40f);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(getColorCompat(R.color.gray_dark));
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2) - (int) (textPaint.measureText(String.
                valueOf(valueStr)) / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() +
                textPaint.ascent()) / 2));

        int usableWidth = getWidth() - (getPaddingLeft() + getPaddingRight());
        int usableHeight = getHeight() - (getPaddingTop() + getPaddingBottom());

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = getPaddingLeft() + (usableWidth / 2);
        int cy = getPaddingTop() + (usableHeight / 2);

        canvas.drawCircle(cx, cy, radius, backgroundPaint);
        canvas.drawText(valueStr, xPos, yPos, textPaint);
    }

    public void setCurrentActive() {
        backgroundPaint.setColor(getColorCompat(R.color.primary));
        invalidate();
    }

    public void setCurrentCompeleted() {
        backgroundPaint.setColor(getColorCompat(R.color.primary));
        valueStr = "\u2713";
        invalidate();
    }

    private int getColorCompat(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }
}
