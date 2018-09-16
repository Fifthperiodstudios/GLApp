package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StundenListAdapter extends RecyclerView.Adapter<StundenListAdapter.MyViewHolder> {
    private StundenplanParser.Wochentag wochentag;
    private ArrayList<String> colors;
    public StundenListAdapter(StundenplanParser.Wochentag w) {
        wochentag = w;
        initColors();
    }

    private void initColors(){
        colors = new ArrayList<String>();
        colors.add("#1abc9c");
        colors.add("#3498db");
        colors.add("#2ecc71");
        colors.add("#9b59b6");
        colors.add("#34495e");
        colors.add("#16a085");
        colors.add("#f1c40f");
        colors.add("#e74c3c");
        colors.add("#95a5a6");
        colors.add("#B33771");
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from (context);
        View stundenView = inflater.inflate(R.layout.stundenlistelement, parent, false);
        MyViewHolder viewHolder = new MyViewHolder (stundenView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Stunde stunde = wochentag.getStunden().get(position);
        TextView kursnameView = holder.kursname;
        TextView lehrerView = holder.lehrer;
        TextView raumView = holder.raum;
        TextView symbolView = holder.symbol;

        Drawable background = symbolView.getBackground();
        int rand = (int)(Math.random()*colors.size());
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(colors.get(rand)));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(colors.get(rand)));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(colors.get(rand)));
        }
        colors.remove(rand);
        symbolView.setText(stunde.getKurs().contains("LK") ? stunde.getFach() + "LK" : stunde.getFach());
        kursnameView.setText(stunde.getStunde() + ". " + stunde.getKurs());
        lehrerView.setText(stunde.getLehrer());
        raumView.setText(stunde.getRaum());

    }

    @Override
    public int getItemCount() {
        return wochentag.getStunden().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kursname;
        public TextView lehrer;
        public TextView raum;
        public TextView symbol;
        public MyViewHolder (View stundenView) {
            super (stundenView);
            kursname = (TextView) stundenView.findViewById(R.id.kursname);
            lehrer = (TextView) stundenView.findViewById(R.id.lehrer);
            raum = (TextView) stundenView.findViewById(R.id.raum);
            symbol = (TextView) stundenView.findViewById(R.id.list_item_icon);
        }
    }
}
