package com.fifthperiodstudios.glapp.Stundenplan;

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

import com.fifthperiodstudios.glapp.R;

import java.util.ArrayList;

public class StundenListAdapter extends RecyclerView.Adapter<StundenListAdapter.MyViewHolder> {
    private Stundenplan.Wochentag wochentag;
    public StundenListAdapter(Stundenplan.Wochentag w) {
        wochentag = w;
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
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(stunde.getFach().getColor()));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(stunde.getFach().getColor()));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(stunde.getFach().getColor()));
        }
        symbolView.setText(stunde.getFach().getKursart().contains("LK") ? stunde.getFach() + "LK" : stunde.getFach().getFach());
        kursnameView.setText(stunde.getStunde() + ". " + stunde.getFach().getKurs());
        lehrerView.setText(stunde.getFach().getLehrer());
        raumView.setText(stunde.getFach().getRaum());

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