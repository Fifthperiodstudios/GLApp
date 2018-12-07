package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.R;
import com.fifthperiodstudios.glapp.Stundenplan.Fach;

import java.util.ArrayList;

class VertretungsViewAdapter extends RecyclerView.Adapter<VertretungsViewAdapter.ViewHolderKlasse> {

    Vertretungsplan vertretungsplan;
    Farben farben;

    public VertretungsViewAdapter(Vertretungsplan vertretungsplan, Farben farben){
        this.vertretungsplan = vertretungsplan;
        this.farben = farben;
        //fuelleVertretungsliste();

    }

    @NonNull
    @Override
    public VertretungsViewAdapter.ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from (context);
        View stundenView = inflater.inflate(R.layout.vertretungsstundelistelement, parent, false);
        ViewHolderKlasse viewHolder = new ViewHolderKlasse (stundenView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VertretungsViewAdapter.ViewHolderKlasse holder, int position) {

        TextView kursnameView = holder.kursname;
        TextView lehrerView = holder.lehrer;
        TextView raumView = holder.raum;
        TextView symbolView = holder.symbol;
        TextView bemerkung = holder.bemerkung;
        VertretungsplanStunde stunde = vertretungsplan.getStunde().get(position);

//        Fach fach = new Fach();
//        fach.setKurs();fach.setFach();fach.setKursart();fach.setLehrer();
//        stunde.setFach(fach);

        kursnameView.setText(stunde.getStunde() + ". " + stunde.getFachName());
        if (stunde.getVLehrer()==null){
            lehrerView.setText(stunde.getFLehrer());
        }else{
            lehrerView.setText(stunde.getVLehrer());
            lehrerView.setTextColor(Color.RED);
        }
        if (stunde.getRaumNeu()==null){
            raumView.setText(stunde.getRaum());
        }else{
            raumView.setText(stunde.getRaumNeu());
            raumView.setTextColor(Color.RED);
        }
        bemerkung.setText(stunde.getBemerkung());

        /*kursnameView.setText(vertretungsplan.getStunde().get(position).getFach().getFach());

        if (vertretungsplan.getStunde().get(position).getStunde()!=0){

            symbolView.setText(vertretungsplan.getStunde().get(position).getStunde());

            if(vertretungsplan.getStunde().get(position).getVLehrer()==null){
                lehrerView.setText(vertretungsplan.getStunde().get(position).getFLehrer());
            }else{
                lehrerView.setText(vertretungsplan.getStunde().get(position).getVLehrer());
            }

            if(vertretungsplan.getStunde().get(position).getRaumNeu()==null){
                raumView.setText(vertretungsplan.getStunde().get(position).getRaum());
            }else{
                raumView.setText(vertretungsplan.getStunde().get(position).getRaumNeu());
            }

        }*/
        symbolView.setText(stunde.getFachName());

        Drawable background = symbolView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
        }

    }

    @Override
    public int getItemCount() {
        return vertretungsplan.getStunde().size();
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        public TextView kursname;
        public TextView lehrer;
        public TextView raum;
        public TextView symbol;
        public TextView bemerkung;

        public ViewHolderKlasse (@NonNull View itemView) {
            super (itemView);
            kursname = itemView.findViewById(R.id.kursname);
            lehrer = itemView.findViewById(R.id.lehrer);
            raum = itemView.findViewById(R.id.raum);
            symbol = itemView.findViewById(R.id.list_item_icon);
            bemerkung = itemView.findViewById(R.id.bemerkung);
        }
    }
}
