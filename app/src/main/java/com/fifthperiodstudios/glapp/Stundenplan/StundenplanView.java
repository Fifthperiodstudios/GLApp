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

    private int screen_width;
    private int screen_height;
    private int painting_width;
    private int painting_height;

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

    public int convertDipToPixels(float dips)
    {
        return (int) (dips * this.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int xpad = (getPaddingLeft() + getPaddingRight());
        int ypad = (getPaddingTop() + getPaddingBottom());

        painting_width = w-xpad;
        painting_height = h-ypad;
        screen_height = h;
        screen_width = w;

    }

    public RectF makeStunde (int tag, int stunde) {
        int stunden_width = (int)((float) painting_width / 5.0f);
        int stunden_height = (int)((float) screen_height / 10.0f);

        int buffer = 10;
        int xpos = buffer + getPaddingLeft() + stunden_width*tag;
        int breitemitbuffer = stunden_width-2*buffer;

        int ypos = stunden_height*stunde + buffer + getPaddingTop();
        int hoehemitbuffer = stunden_height-2*buffer;

        return makeRect (xpos, ypos, breitemitbuffer, hoehemitbuffer);
    }

    public RectF makeRect (int xpos, int ypos, int w, int h) {
        int left = xpos;
        int top = ypos;
        int right = w+xpos;
        int bottom = h+ypos;
        return new RectF(left, top, right, bottom);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        for(int i = 0; i<stundenplan.getWochentage().size(); i++){
            for(int j = 0; j<stundenplan.getWochentage().get(i).getStunden().size(); j++){
                mPaint.setColor(Color.parseColor(stundenplan.getWochentage().get(i).getStunden().get(j).getFach().getColor()));
                int nte = Integer.parseInt(stundenplan.getWochentage().get(i).getStunden().get(j).getStunde())-1;
                RectF stunde = makeStunde(i, nte);
                canvas.drawRoundRect(stunde, 10, 10, mPaint);
            }
        }
    }


}
