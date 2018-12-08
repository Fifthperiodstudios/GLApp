package com.fifthperiodstudios.glapp.Klausurplan;

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

import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.R;
import com.fifthperiodstudios.glapp.Stundenplan.Fach;

public class KlausurplanViewAdapter extends RecyclerView.Adapter<KlausurplanViewAdapter.ViewHolderKlasse> {

    Klausurplan klausurplan;
    Farben farben;

    public KlausurplanViewAdapter(Klausurplan klausurplan, Farben farben){
        this.klausurplan = klausurplan;
        this.farben = farben;
    }

    @NonNull
    @Override
    public KlausurplanViewAdapter.ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from (context);
        View stundenView = inflater.inflate(R.layout.klausurplan_element, parent, false);
        KlausurplanViewAdapter.ViewHolderKlasse viewHolder = new KlausurplanViewAdapter.ViewHolderKlasse(stundenView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KlausurplanViewAdapter.ViewHolderKlasse holder, int position) {

        TextView tVdatum = holder.datum;
        TextView tVdauer = holder.dauer;
        TextView tVraum = holder.raum;
        TextView tVlehrer = holder.lehrer;
        TextView tVsymbol = holder.symbol;

        Klausur klausur = klausurplan.getKlausuren().get(position);

        tVdatum.setText(klausur.getDatum());
        tVdauer.setText(klausur.getStart()+" - "+klausur.getEnde());
        tVraum.setText(klausur.getRaum());
        tVlehrer.setText(klausur.getLehrkraft());
        tVsymbol.setText(klausur.getFach().getFach());

        Drawable background = tVsymbol.getBackground();

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(farben.getFarbeFach(klausur.getFach())));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(farben.getFarbeFach(klausur.getFach())));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(farben.getFarbeFach(klausur.getFach())));
        }

    }

    @Override
    public int getItemCount() {
        return klausurplan.getKlausuren().size();
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        public TextView datum;
        public TextView dauer;
        public TextView raum;
        public TextView symbol;
        public TextView lehrer;

        public ViewHolderKlasse (@NonNull View itemView) {
            super (itemView);
            datum = itemView.findViewById(R.id.datum);
            dauer = itemView.findViewById(R.id.dauer);
            raum = itemView.findViewById(R.id.raum);
            symbol = itemView.findViewById(R.id.list_item_icon);
            lehrer = itemView.findViewById(R.id.lehrer);
        }
    }

    public void setFarben(Farben farben){
        this.farben = farben;
    }
}
