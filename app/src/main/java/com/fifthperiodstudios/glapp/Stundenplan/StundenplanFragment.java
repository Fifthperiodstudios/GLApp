package com.fifthperiodstudios.glapp.Stundenplan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.DownloadStundenplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.StundenplanDownloader;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
import com.fifthperiodstudios.glapp.R;


public class StundenplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadStundenplanStatusListener, OnUpdateListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private Farben farben;
    private Stundenplan stundenplan;
    private StundenplanDownloader stundenplanDownloader;

    public StundenplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stundenplan_fragment, container, false);

        Bundle args = getArguments();
        farben = (Farben) args.getSerializable("farben");

        stundenplanDownloader = new StundenplanDownloader(getActivity(), args.getString("mobilKey"), this);
        stundenplanDownloader.downloadStundenplan();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        relativeLayout = rootView.findViewById(R.id.stundenplanview);

        return rootView;
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung(Stundenplan stundenplan) {

        setupView(stundenplan);
        Toast.makeText(getContext(), "Keine Internetverbindung, offline Daten werden angezeigt", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void fertigHeruntergeladen(Stundenplan stundenplan) {

        relativeLayout.removeAllViews();
        setupView(stundenplan);

    }

    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), "Etwas ist schiefgelaufen :/", Toast.LENGTH_SHORT).show();
    }

    public void setupView(Stundenplan stundenplan) {
        this.stundenplan = stundenplan;
        int buffer = 5;

        int painting_width = relativeLayout.getWidth() - 2 * relativeLayout.getPaddingLeft() - 10 * (buffer);
        int painting_height = relativeLayout.getHeight() - 2 * relativeLayout.getPaddingTop() - 10 * (buffer);

        int stunden_width = (int) painting_width / 5;
        int stunden_height = (int) painting_height / 10;

        for (int i = 0; i < stundenplan.getWochentage().size(); i++) {
            for (int j = 0; j < stundenplan.getWochentage().get(i).getStunden().size(); j++) {
                final Stunde stunde = stundenplan.getWochentage().get(i).getStunden().get(j);

                Button button = new Button(getActivity());
                RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                int nte = Integer.parseInt(stunde.getStunde()) - 1;
                rl.leftMargin = buffer + i * (stunden_width + buffer + 2 * buffer);
                rl.topMargin = buffer + nte * (stunden_height + buffer + 2 * buffer);
                rl.width = stunden_width - 2 * buffer;
                rl.height = stunden_height - 2 * buffer;
                button.setLayoutParams(rl);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Bei " + stunde.getFach().getLehrer() + " in " + stunde.getRaum(), Toast.LENGTH_SHORT).show();
                    }
                });

                button.setBackgroundResource(R.drawable.stunde_rechteck);
                Drawable background = button.getBackground();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(Color.parseColor(farben.getFarbeFach(stunde.getFach())));
                }
                button.setText(stunde.getFach().getVollenName());
                button.setTextColor(Color.WHITE);
                relativeLayout.addView(button);
            }
        }
    }

    @Override
    public void updateData(Farben farben) {
        this.farben = farben;
        relativeLayout.removeAllViews();
        setupView(stundenplan);
    }

    public Farben getFarben() {
        return farben;
    }

    public Stundenplan getStundenplan() {
        return stundenplan;
    }
}
