package com.fifthperiodstudios.glapp.FarbAuswahlDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.fifthperiodstudios.glapp.R;

import java.util.ArrayList;

public class FarbAuswahlDialog extends DialogFragment implements FarbRecyclerViewAdapter.ItemClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.farbauswahl_dialog, null);
        RecyclerView recyclerView = view.findViewById(R.id.colors_recyc);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), numberOfColumns));

        FarbRecyclerViewAdapter adapter = new FarbRecyclerViewAdapter(this.getContext(), getColors());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FarbAuswahlDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private ArrayList<String> getColors() {
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("#1abc9c");
        colors.add("#16a085");
        colors.add("#f1c40f");
        colors.add("#f39c12");

        colors.add("#2ecc71");
        colors.add("#27ae60");
        colors.add("#e67e22");
        colors.add("#d35400");

        colors.add("#3498db");
        colors.add("#2980b9");
        colors.add("#e74c3c");
        colors.add("#c0392b");

        colors.add("#9b59b6");
        colors.add("#8e44ad");
        colors.add("#ecf0f1");
        colors.add("#bdc3c7");

        colors.add("#34495e");
        colors.add("#2c3e50");
        colors.add("#95a5a6");
        colors.add("#7f8c8d");

        colors.add("#B33771");
        colors.add("#A64B48");
        colors.add("#A0A68B");
        colors.add("#454442");
        return colors;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
