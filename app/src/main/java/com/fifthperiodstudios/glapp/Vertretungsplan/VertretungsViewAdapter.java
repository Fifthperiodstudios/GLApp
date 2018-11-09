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
import android.widget.ImageView;
import android.widget.TextView;

import com.fifthperiodstudios.glapp.R;

class VertretungsViewAdapter extends RecyclerView.Adapter<VertretungsViewAdapter.ViewHolderKlasse> {

    Vertretungsplan.VertretungsTag vertretungsTag;

    public VertretungsViewAdapter(Vertretungsplan.VertretungsTag vertretungstag){
        this.vertretungsTag = vertretungstag;
    }

    @NonNull
    @Override
    public VertretungsViewAdapter.ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vertretungsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stundenlistelement,null);
        return new ViewHolderKlasse(vertretungsView);
    }

    @Override
    public void onBindViewHolder(@NonNull VertretungsViewAdapter.ViewHolderKlasse holder, int position) {

        TextView kursnameView = holder.kursname;
        TextView lehrerView = holder.lehrer;
        TextView raumView = holder.raum;
        TextView symbolView = holder.symbol;

        Drawable background = symbolView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.RED);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.BLUE);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.GREEN);
        }

        kursnameView.setText("Kurs: "+position);
        lehrerView.setText("Lehrer: "+position);
        raumView.setText("Raum: "+position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        public TextView kursname;
        public TextView lehrer;
        public TextView raum;
        public TextView symbol;

        public ViewHolderKlasse (@NonNull View itemView) {
            super (itemView);
            kursname = itemView.findViewById(R.id.kursname);
            lehrer = itemView.findViewById(R.id.lehrer);
            raum = itemView.findViewById(R.id.raum);
            symbol = itemView.findViewById(R.id.list_item_icon);
        }
    }
}
