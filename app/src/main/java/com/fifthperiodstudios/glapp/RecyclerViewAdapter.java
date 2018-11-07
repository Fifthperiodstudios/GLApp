package com.fifthperiodstudios.glapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Login.MainActivity;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderKlasse> {

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View einFach = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_settings_element,null);

        return new ViewHolderKlasse(einFach);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse holder, final int position) {

        holder.fachName.setText("Fachname Nr:"+position);//Hier die Fächerliste einzeln aufrufen
//        holder.farbeWaehlen.setBackgroundColor();//Farbe der Einzelnen Fächer anzeigen/ eine zufällige Farbe erzeugen/nehmen, wenn es keine gibt.
        holder.farbeWaehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                zeige die Auswahl an
            }
        });

    }

    @Override
    public int getItemCount() {

        return 16;//Arraylist.getSize() länge der Liste ausgeben lassen
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
