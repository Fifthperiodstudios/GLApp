package com.fifthperiodstudios.glapp.Vertretungsplan;

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

class VertretungsViewAdapter extends RecyclerView.Adapter<VertretungsViewAdapter.ViewHolderKlasse> {

    Vertretungsplan vertretungsplan;
    Farben farben;

    public VertretungsViewAdapter(Vertretungsplan vertretungsplan, Farben farben){
        this.vertretungsplan = vertretungsplan;
        this.farben = farben;
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
        TextView raumlehrerview = holder.raumlehrer;
        TextView symbolView = holder.symbol;
        TextView bemerkung = holder.bemerkung;
        Vertretungsstunde stunde = vertretungsplan.getStunden().get(position);

        String fachfarbe = farben.getFarbeFach(stunde.getFach());
        String s = "";
        kursnameView.setText(stunde.getDatumAlsText() + " " + stunde.getStunde() + ". " + stunde.getFach().getFach());
        if (stunde.getVLehrer().isEmpty()){
            s += "Bei " + stunde.getFLehrer() + " ";
        }else if(!stunde.getVLehrer().equals(stunde.getFLehrer())){
            s += "Vertretung bei " + stunde.getVLehrer() + " ";
        }
        if (stunde.getRaumNeu().isEmpty()){
            s += "in Raum " + stunde.getRaum();
        }else{
            s += "Raumwechsel: " + stunde.getRaumNeu();
        }

        raumlehrerview.setText(s);
        bemerkung.setText(stunde.getBemerkung());
        symbolView.setText(stunde.getFach().getFach());

        Drawable background = symbolView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(fachfarbe));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(fachfarbe));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(fachfarbe));
        }

    }

    @Override
    public int getItemCount() {
        return vertretungsplan.getStunden().size();
    }

    public void setFarben(Farben farben) {
        this.farben = farben;
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        public TextView kursname;
        public TextView raumlehrer;
        public TextView symbol;
        public TextView bemerkung;

        public ViewHolderKlasse (@NonNull View itemView) {
            super (itemView);
            kursname = itemView.findViewById(R.id.kursname);
            raumlehrer = itemView.findViewById(R.id.raum_und_lehrer);
            symbol = itemView.findViewById(R.id.list_item_icon);
            bemerkung = itemView.findViewById(R.id.bemerkung);
        }
    }
}
