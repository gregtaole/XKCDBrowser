package fr.esiea.xkcdbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {
    private Comic[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public SimpleDraweeView mDraweeView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById();
            mDraweeView = (SimpleDraweeView) v.findViewById()
        }

    }

    public ComicAdapter(Comic[] mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}