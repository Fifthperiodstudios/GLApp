package com.fifthperiodstudios.glapp.FarbAuswahlDialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fifthperiodstudios.glapp.R;

import java.util.ArrayList;

public class FarbRecyclerViewAdapter extends RecyclerView.Adapter<FarbRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Button lastChecked;

    // data is passed into the constructor
    FarbRecyclerViewAdapter(Context context, ArrayList<String> colors) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = colors;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.farb_element, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drawable background = holder.button.getBackground();
        StateListDrawable drawable = (StateListDrawable) holder.button.getBackground();
        DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
        Drawable[] drawableItems = dcs.getChildren();
        GradientDrawable gradientDrawable = (GradientDrawable) drawableItems[1]; // item 1
        gradientDrawable.setColor(Color.parseColor(mData.get(position)));
        /*if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(mData.get(position)));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.parseColor(mData.get(position)));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(Color.parseColor(mData.get(position)));
        }*/
        //Set color
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatButton button;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.farbe);
            button.setOnClickListener(this);
        }

        private void select(View view) {
            if (lastChecked == null) {
                lastChecked = (Button) view;
                lastChecked.setSelected(true);
            } else {
                lastChecked.setSelected(false);
                lastChecked = (Button) view;
                lastChecked.setSelected(true);
            }
            setColor(view);
        }

        private void setColor (View view){
            StateListDrawable drawable = (StateListDrawable) lastChecked.getBackground();
            DrawableContainer.DrawableContainerState dcs = (DrawableContainer.DrawableContainerState) drawable.getConstantState();
            Drawable[] drawableItems = dcs.getChildren();
            GradientDrawable gradientDrawable = (GradientDrawable) drawableItems[0]; // item 1
            gradientDrawable.setColor(Color.parseColor(mData.get(getAdapterPosition())));
        }

        @Override
        public void onClick(View view) {
            select(view);

            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}