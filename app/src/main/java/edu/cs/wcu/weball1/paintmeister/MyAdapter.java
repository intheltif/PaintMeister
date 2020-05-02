package edu.cs.wcu.weball1.paintmeister;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  implements RecyclerView.OnClickListener {
    private List<String> data;
    private ItemWasClicked iwcImp;

    MyAdapter(List<String> data, ItemWasClicked iwc) {
        this.data = data;
        this.iwcImp = iwc;
    }

    public int getItemCount() {
        return this.data.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        String filePath = this.data.get(position);
        String[] filePieces = filePath.split("/");
        holder.fileName.setText(filePieces[filePieces.length - 1]);
        holder.root.setTag(filePieces[filePieces.length - 1]);
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_painting, parent, false);
        TextView fileName = layout.findViewById(R.id.file_name);

        layout.setOnClickListener(this);
        return new MyViewHolder(layout, fileName);
    }

    @Override
    public void onClick(View v) {
        String filePath = v.getTag().toString();
        this.iwcImp.itemClicked(filePath);
    }

    public interface ItemWasClicked {
        void itemClicked(String text);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        View root;

        MyViewHolder(View root, TextView fileName) {
            super(root);
            this.root = root;
            this.fileName = fileName;
        }
    }
}
