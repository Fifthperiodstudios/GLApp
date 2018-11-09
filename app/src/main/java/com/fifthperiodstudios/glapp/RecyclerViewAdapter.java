package com.fifthperiodstudios.glapp;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderKlasse> {
    Stundenplan stundenplan;

    public RecyclerViewAdapter (Stundenplan stundenplan) {
        this.stundenplan = stundenplan;
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View einFach = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_settings_element,null);

        return new ViewHolderKlasse(einFach);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderKlasse holder, final int position) {

        Drawable background = holder.farbeWaehlen.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(Color.parseColor(stundenplan.getFächer().get(position).getColor()));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(Color.parseColor(stundenplan.getFächer().get(position).getColor()));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(Color.parseColor(stundenplan.getFächer().get(position).getColor()));
        }

        holder.fachName.setText(stundenplan.getFächer().get(position).getFach());//Hier die Fächerliste einzeln aufrufen
        holder.farbeWaehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(,"Das ist die Nachricht von Element: "+position,Toast.LENGTH_SHORT).show();
//                zeige die Auswahl an
            }
        });

    }

    @Override
    public int getItemCount() {

        return stundenplan.getFächer().size();
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        TextView fachName;
        Button farbeWaehlen;

        public ViewHolderKlasse(View itemView){

            super(itemView);

            fachName = itemView.findViewById(R.id.colorSettingsFach);
            farbeWaehlen = itemView.findViewById(R.id.colorSettingsFarbe);

        }
    }
}
/*
public class StundenListAdapter extends RecyclerView.Adapter<StundenListAdapter.MyViewHolder> {
    private StundenplanParser.Wochentag wochentag;

    public StundenListAdapter(StundenplanParser.Wochentag w) {
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
        symbolView.setText(stunde.getFach().getKurs().contains("LK") ? stunde.getFach().getFach() + "LK" : stunde.getFach().getFach());
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
}*/
