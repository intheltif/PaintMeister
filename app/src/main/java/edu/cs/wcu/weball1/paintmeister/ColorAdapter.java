package edu.cs.wcu.weball1.paintmeister;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ColorAdapter.java
 *
 * The adapter to assist in generating the RecyclerView holding colors to choose from.
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>
        implements RecyclerView.OnClickListener{

    /** The list of color names */
    private int[] data;
    /** Handles the click event on a color */
    private ColorAdapter.ColorWasClicked cwcImp;

    /**
     * Generate a new adapter
     * @param data the list of colors.
     * @param cwc the listener for click events on a color.
     */
    ColorAdapter(int[] data, ColorAdapter.ColorWasClicked cwc) {
        this.data = data;
        this.cwcImp = cwc;
    } // end constructor

    /**
     * Determines the number of colors to choose from.
     * @return the number of colors to choose from.
     */
    public int getItemCount() {
        return this.data.length;
    } // end getItemCount

    /**
     * Behavior to be performed when the holder is bound to the activity
     * @param holder the view holder for the colors
     * @param position where the color is in the list of colors.
     */
    public void onBindViewHolder(ColorAdapter.ColorViewHolder holder, int position) {
        int color = this.data[position];
        holder.root.setTag("" + color);
        holder.colorHex.setText("#" + Integer.toHexString(color));
        holder.swatch.setBackgroundColor(color);
    } // end onBindViewHolder

    /**
     * Behavior to be performed when the view holder is created
     * @param parent the "wrapper" around this view holder
     * @param viewType what type of view holder this is
     * @return the ViewHolder that has been created
     */
    @NonNull
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.color_selection, parent, false);
        TextView hex = layout.findViewById(R.id.color_hex);
        TextView swatch = layout.findViewById(R.id.color_swatch);

        layout.setOnClickListener(this);
        return new ColorViewHolder(layout, hex, swatch);
    } // end onCreateViewHolder

    /**
     * Behavior to be performed when a color is clicked
     * @param v the view representing the color that was clicked
     */
    @Override
    public void onClick(View v) {
        String color = v.getTag().toString();
        this.cwcImp.colorClicked(color);
    } // end onClick

    /**
     * Interface for the activity that will handle clicking behavior
     */
    public interface ColorWasClicked {
        void colorClicked(String text);
    } // end ItemWasClicked

    /**
     * The view that will hold the representation of this color
     */
    static class ColorViewHolder extends RecyclerView.ViewHolder {
        /** The hex value of the color to draw with */
        TextView colorHex;
        /** The TextView that acts as a color swatch to display the color to the user. */
        TextView swatch;
        /** the view that will be holding this view holder */
        View root;

        /**
         * Creates a new View Holder
         * @param root the view that will be holding this view holder
         * @param colorHex The hex value of the color to draw with.
         * @param swatch The paint swatch to show the user.
         */
        ColorViewHolder(View root, TextView colorHex, TextView swatch) {
            super(root);
            this.root = root;
            this.colorHex = colorHex;
            this.swatch = swatch;
        } // end constructor
    } // end MyViewHolder

} // end ColorAdapter class
