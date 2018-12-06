package com.fifthperiodstudios.glapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fifthperiodstudios.glapp.FarbAuswahlDialog.FarbAuswahlDialog;
import com.fifthperiodstudios.glapp.Stundenplan.Fach;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderKlasse> implements FarbAuswahlDialog.NoticeDialogListener {
    Stundenplan stundenplan;
    FragmentManager mFragmentManager;
    Farben farben;
    FarbAuswahlDialog farbAuswahlDialog;

    public RecyclerViewAdapter(Stundenplan stundenplan, Farben farben, FragmentManager fragmentManager) {
        this.stundenplan = stundenplan;
        this.farben = farben;
        mFragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolderKlasse onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View einFach = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_settings_element, null);

        return new ViewHolderKlasse(einFach);
    }

    public void setColor (Button b, int position){
        Drawable background = b.getBackground();
        String farbe = farben.getFarbeFach(stundenplan.getFächer().get(position));
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(farbe));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.parseColor(farbe));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(Color.parseColor(farbe));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderKlasse holder, int position) {

        setColor(holder.farbeWaehlen, position);

        final int pos = position;
        holder.fachName.setText(stundenplan.getFächer().get(position).getFach());//Hier die Fächerliste einzeln aufrufen
        holder.farbeWaehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farbAuswahlDialog = new FarbAuswahlDialog();
                farbAuswahlDialog.setOnNoticeListener (RecyclerViewAdapter.this);
                // Add action buttons
                Bundle bundle = new Bundle();
                bundle.putSerializable("fach", stundenplan.getFächer().get(pos));
                bundle.putSerializable("farben", farben);
                bundle.putInt("position", pos);
                farbAuswahlDialog.setArguments(bundle);
                farbAuswahlDialog.show(mFragmentManager, "TAG");
            }
        });

    }

    @Override
    public int getItemCount() {
        return stundenplan.getFächer().size();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String farbe, Fach fach, int pos) {
        if(farbAuswahlDialog.getFarbe() != null){
            farben.getFarbenFaecher().put(fach, farbe);
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    public class ViewHolderKlasse extends RecyclerView.ViewHolder {

        TextView fachName;
        Button farbeWaehlen;

        public ViewHolderKlasse(View itemView) {

            super(itemView);

            fachName = itemView.findViewById(R.id.colorSettingsFach);
            farbeWaehlen = itemView.findViewById(R.id.colorSettingsFarbe);

        }
    }
}