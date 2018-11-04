package com.fifthperiodstudios.glapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderKlasse> {
    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View einFach = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_settings_element,null);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder{

        TextView fachName;
        Button farbeWaehlen;

        public ViewHolderKlasse(View itemView){

            super(itemView);

            fachName = itemView.findViewById(R.id.colorSettingsFach);
            farbeWaehlen = itemView.findViewById(R.id.colorSettingsFarbe);
            farbeWaehlen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
