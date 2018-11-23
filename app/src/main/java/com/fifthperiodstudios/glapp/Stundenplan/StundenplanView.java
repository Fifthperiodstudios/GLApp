package com.fifthperiodstudios.glapp.Stundenplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class StundenplanView extends View {
    private Paint mPaint;

    private int linkegrenze;

    private int painting_width;
    private int painting_height;
    private ArrayList<RectF> zeile;
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

    public void setStundenplan(Stundenplan stundenplan) {
        this.stundenplan = stundenplan;
    }

    public int convertDipToPixels(float dips) {
        return (int) (dips * this.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        linkegrenze = getPaddingLeft();
        int xpad = 2 * linkegrenze;
        int ypad = 2 * linkegrenze;

        painting_width = w - xpad;
        painting_height = h - ypad;

        zeile = makeWochenZeile();
    }

    public RectF makeStunde(int tag, int stunde) {
        int stunden_width = (int) ((float) painting_width / 5.0f);
        int stunden_height = (int) ((float) painting_height / 10.0f);

        int buffer = 10;

        // 188 - 2*10 = 168 (breite mit beidseitigem Buffer)
        int breitemitbuffer = stunden_width - 2 * buffer;
        int hoehemitbuffer = stunden_height - 2 * buffer;

        //xpos = 70 + buffer + tag * (breitemitbuffer + 2*buffer)
        int xpos = linkegrenze + buffer + tag * (breitemitbuffer + 2 * buffer);

        int ypos = linkegrenze + buffer + stunde * (hoehemitbuffer + 2 * buffer);
        //int ypos = stunden_height * stunde + buffer + getPaddingTop();

        Log.d("TAG", "Xpos: " + xpos + " breite: " + breitemitbuffer + " von " + xpos + " bis " + (xpos + breitemitbuffer));
        return makeRect(xpos, ypos, breitemitbuffer, hoehemitbuffer);
    }

    public RectF makeRect(int xpos, int ypos, int w, int h) {
        int left = xpos;
        int top = ypos;
        int right = w + xpos;
        int bottom = h + ypos;
        return new RectF(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < stundenplan.getWochentage().size(); i++) {
            for (int j = 0; j < stundenplan.getWochentage().get(i).getStunden().size(); j++) {
                mPaint.setColor(Color.parseColor(stundenplan.getWochentage().get(i).getStunden().get(j).getFach().getColor()));
                int nte = Integer.parseInt(stundenplan.getWochentage().get(i).getStunden().get(j).getStunde()) - 1;
                RectF stunde = makeStunde(i, nte);
                canvas.drawRoundRect(stunde, 10, 10, mPaint);
                mPaint.setColor(Color.WHITE);
                mPaint.setTextSize(stunde.height()/3.3f);
                mPaint.setTextAlign(Paint.Align.CENTER);
                Log.d("TAGA", Float.toString(stunde.right));
                String bezeichnung = stundenplan.getWochentage().get(i).getStunden().get(j).getFach().getFach();
                if(stundenplan.getWochentage().get(i).getStunden().get(j).getFach().getKursart().contains("LK")){
                    bezeichnung += "-LK";
                }
                canvas.drawText(bezeichnung, stunde.centerX(), stunde.centerY()+15.0f, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xpos = (int) event.getX();
        int tag = identifyTag (xpos);
        Log.d("msg", Integer.toString(tag));
        if(tag != -1){
            Intent intent = new Intent(this.getContext(), WochentagActivity.class);
            Activity host = (Activity) this.getContext();
            intent.putExtra("Wochentag", stundenplan.getWochentage().get(tag));
            getContext().startActivity(intent);
        }
        return false;
    }

    public int identifyTag(float x){
        for(int i = 0; i<zeile.size(); i++){
            if(x >= zeile.get(i).left && x <= zeile.get(i).left + zeile.get(i).width()) {
                return i;
            }
        }
        return -1;
    }

    private ArrayList<RectF> makeWochenZeile () {
        ArrayList<RectF> z = new ArrayList<RectF>();
        for(int i = 0; i<stundenplan.getWochentage().size(); i++){
            z.add(makeStunde(i, 0));
            Log.d("MAS", "JKK");
        }
        return z;
    }
}

