package com.fifthperiodstudios.glapp.Stundenplan;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.GLAPPPresenter;
import com.fifthperiodstudios.glapp.GLAPPViews;
import com.fifthperiodstudios.glapp.R;

public class StundenplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GLAPPViews, GLAPPViews.StundenplanView {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private GLAPPPresenter glappPresenter;
    private Stundenplan stundenplan;
    private SharedPreferences sharedPreferences;

    public StundenplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stundenplan_fragment, container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        relativeLayout = rootView.findViewById(R.id.stundenplanview);

        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        glappPresenter.downloadStundenplan();
    }

    @Override
    public void onRefresh() {
        glappPresenter.downloadStundenplan();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung(final Stundenplan stundenplan, final Farben farben) {

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {
                setupView(stundenplan, farben);
            }
        });

    }

    @Override
    public void fertigHeruntergeladen(Stundenplan stundenplan, Farben farben) {
        sharedPreferences.edit().putString("Stundenplandatum", stundenplan.getDatum()).commit();
        setupView(stundenplan, farben);

    }

    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), R.string.some_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(@NonNull GLAPPPresenter presenter) {
        this.glappPresenter = presenter;
    }

    public void setupView(Stundenplan stundenplan, Farben farben) {
        relativeLayout.removeAllViews();
        this.stundenplan = stundenplan;
        int buffer = 5;//(relativeLayout.getMeasuredWidth() - 2 * relativeLayout.getPaddingLeft()) / (160);
        int painting_width = relativeLayout.getMeasuredWidth() - 2 * relativeLayout.getPaddingLeft() - 10 * (buffer);
        int painting_height = relativeLayout.getMeasuredHeight() - 2 * relativeLayout.getPaddingTop() - 10 * (buffer);

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

                int c = Color.parseColor(farben.getFarbeFach(stunde.getFach()));
                button.setBackgroundResource(R.drawable.stunde_rechteck);
                Drawable background = button.getBackground();
                if (background instanceof ShapeDrawable) {
                    ((ShapeDrawable) background).getPaint().setColor(c);
                } else if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(c);
                } else if (background instanceof ColorDrawable) {
                    ((ColorDrawable) background).setColor(c);
                }
                button.setText(stunde.getFach().getVollenName());
                button.setTextColor(Color.WHITE);
                relativeLayout.addView(button);
            }
        }
    }

    @Override
    public void updateFarben(Farben farben) {
        setupView(stundenplan, farben);
    }
}
