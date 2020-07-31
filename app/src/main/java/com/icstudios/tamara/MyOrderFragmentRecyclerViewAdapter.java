package com.icstudios.tamara;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link orderListItem}.
 */
public class MyOrderFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<orderListItem.ScanItem> mValues;
    private ItemClickListener mClickListener;
    List<orderListItem.ScanItem> mValuesCopy;

    public MyOrderFragmentRecyclerViewAdapter(List<orderListItem.ScanItem> items) {
        mValues = items;
        mValuesCopy = new ArrayList<>(mValues);
    }

    public void filter(String text) {
        mValues.clear();
        if(text.isEmpty()){
            mValues.addAll(mValuesCopy);
        } else{
            for(orderListItem.ScanItem item: mValuesCopy ){
                if(item.getAddress().contains(text)){
                    mValues.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getAddress());
        holder.mMACView.setText(mValues.get(position).getDate());
        holder.mRSSIView.setText(mValues.get(position).getRssi());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mMACView;
        public final TextView mRSSIView;
        public orderListItem.ScanItem mItem;
        ImageButton menu;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mMACView = (TextView) view.findViewById(R.id.mac);
            mRSSIView = (TextView) view.findViewById(R.id.rssi);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    // allows clicks events to be caught
    void setClickListener(MyOrderFragmentRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}