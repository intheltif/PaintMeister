package edu.cs.wcu.weball1.paintmeister;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * MyAdapter.java
 *
 * The adapter to assist in generating the RecyclerView holding file names for loading
 *
 * @author Evert Ball
 * @author Chris Wolf
 * @version 1.0.0 (May 4, 2020)
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
        implements RecyclerView.OnClickListener {
    /** The list of file names */
    private List<String> data;
    /** The activity that will handle loading the file that was clicked */
    private ItemWasClicked iwcImp;

    /**
     * Generate a new adapter
     * @param data the list of file names
     * @param iwc the activity that will handle loading the file that was clicked
     */
    MyAdapter(List<String> data, ItemWasClicked iwc) {
        this.data = data;
        this.iwcImp = iwc;
    } // end constructor

    /**
     * Determines the number of paintings that are in storage
     * @return the number of paintings that are in storage
     */
    public int getItemCount() {
        return this.data.size();
    } // end getItemCount

    /**
     * Behavior to be performed when the holder is bound tot he activity
     * @param holder the view holder for this file name
     * @param position where this file is in the list of files
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String filePath = this.data.get(position);
        String[] filePieces = filePath.split("/");
        holder.fileName.setText(filePieces[filePieces.length - 1]);
        holder.root.setTag(filePieces[filePieces.length - 1]);
    } // end onBindViewHolder

    /**
     * Behavior to be performed when the view holder is created
     * @param parent the "wrapper" around this view holder
     * @param viewType what type of view holder this is
     * @return the ViewHolder that has been created
     */
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_painting, parent, false);
        TextView fileName = layout.findViewById(R.id.file_name);

        layout.setOnClickListener(this);
        return new MyViewHolder(layout, fileName);
    } // end onCreateViewHolder

    /**
     * Behavior to be performed when a file name is clicked
     * @param v the view representing the file that was clicked
     */
    @Override
    public void onClick(View v) {
        String filePath = v.getTag().toString();
        this.iwcImp.itemClicked(filePath);
    } // end onClick

    /**
     * Interface for the activity that will handle clicking behavior
     */
    public interface ItemWasClicked {
        void itemClicked(String text);
    } // end ItemWasClicked

    /**
     * The view that will hold the representation of this file name
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        /** The name of the file that this view holder will represent */
        TextView fileName;
        /** the view that will be holding this view holder */
        View root;

        /**
         * Creates a new View Holder
         * @param root the view that will be holding this view holder
         * @param fileName the name of the file that this view holder will present
         */
        MyViewHolder(View root, TextView fileName) {
            super(root);
            this.root = root;
            this.fileName = fileName;
        } // end constructor
    } // end MyViewHolder
} // end MyAdapter
