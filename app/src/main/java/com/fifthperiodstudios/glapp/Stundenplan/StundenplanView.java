package com.fifthperiodstudios.glapp.Stundenplan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class StundenplanView extends View {
    private Paint mPaint;
    private int stunden_width;
    private int stunden_height;
    private int width;
    private int height;
    private int topBorder;
    private int leftBorder;
    private int buffer;

    private Stundenplan stundenplan;

    public StundenplanView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    public void setStundenplan (Stundenplan stundeplan) {
        this.stundenplan = stundeplan;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());
        buffer = 5;
        this.width = (int) ((float)w - xpad);
        this.height = (int) ((float)h - ypad);

        stunden_width = (int)((float) width / 5.0f);
        stunden_height = (int)((float) height / 10.0f);

        leftBorder = getPaddingLeft();
        topBorder = getPaddingTop();

        Log.d("asdlj", Integer.toString(leftBorder)+" "+Integer.toString(getPaddingRight()));
    }

    public RectF makeStunde (int xpos, int ypos, int width, int height) {
        int left = xpos;
        int top = ypos;
        int right = width+xpos;
        int bottom = height+ypos;
        return new RectF(left, top, right, bottom);
    }
    @Override
    protected void onDraw (Canvas canvas) {

        for(int i = 0; i<stundenplan.getWochentage().size(); i++){
            for(int j = 0; j<stundenplan.getWochentage().get(i).getStunden().size(); j++){
                mPaint.setColor(Color.parseColor(stundenplan.getWochentage().get(i).getStunden().get(j).getFach().getColor()));
                int nte = Integer.parseInt(stundenplan.getWochentage().get(i).getStunden().get(j).getStunde())-1;
                canvas.drawRoundRect(makeStunde(i*(stunden_width+buffer)+leftBorder, nte*(stunden_height+buffer)+topBorder, stunden_width-buffer, stunden_height-buffer), 10, 10, mPaint);
            }
        }
    }
}
