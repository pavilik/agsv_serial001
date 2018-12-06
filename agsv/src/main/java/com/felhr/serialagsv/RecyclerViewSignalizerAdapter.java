package com.felhr.serialagsv;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewSignalizerAdapter extends RecyclerView.Adapter<RecyclerViewSignalizerAdapter.ViewHolder> {

    private final ArrayList<SignalizerItem> itemValues = new ArrayList<>();
    private LayoutInflater mInflater;

    // Data is passed into the constructor
    public RecyclerViewSignalizerAdapter(Context context, ArrayList<SignalizerItem> items) {
        this.mInflater = LayoutInflater.from(context);
        this.itemValues.addAll(items);

    }

    // Inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item_signalizer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binds the data to the textview in each cell

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String numberStrSignalizer = itemValues.get(position).getNumberSignalizer().toString();
        boolean statusItem = itemValues.get(position).isVisibleStaus();
        Integer itemCondition = itemValues.get(position).getConditionCode();
        holder.myTextView.setText(numberStrSignalizer);
        if (statusItem) {
            holder.myTextView.setBackgroundColor(Color.GREEN);
        } else {
            if (itemCondition != 0)
                holder.myTextView.setBackgroundColor(Color.RED);
        }

        //just for test
//        if (position%2==0) {
//            holder.myTextView.setBackgroundColor(Color.GREEN);
//        }

    }

    // Total number of cells
    @Override
    public int getItemCount() {
        return itemValues.size();
    }

    public void setClickListener(SignalizerActivity signalizerActivity) {
    }

    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
        }
    }

    // Convenience method for getting data at click position
    public SignalizerItem getItem(int id) {
        return itemValues.get(id);
    }

    // Method that executes your code for the action received
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + getItem(position).getNumberSignalizer().toString() + ", which is at cell position " + position);
    }

    public  void changeInformationSignazer (ArrayList<SignalizerItem> changedItemSignalizers) {
        itemValues.clear();
        itemValues.addAll(changedItemSignalizers);

    }
}
