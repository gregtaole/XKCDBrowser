package fr.esiea.xkcdbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ComicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ComicAdapter";

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private ArrayList<Comic> comics;
    private boolean showLoader;
    private static RecyclerViewItemClickListener clickListener;

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView titleView;
        private TextView dateView;
        private SimpleDraweeView imageView;

        public ItemViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.title);
            dateView = (TextView) v.findViewById(R.id.publication_date);
            imageView = (SimpleDraweeView) v.findViewById(R.id.image);
            titleView.setOnClickListener(this);
            dateView.setOnClickListener(this);
            imageView.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public FooterViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.load_progress);
        }
    }

    public ComicAdapter(ArrayList<Comic> comics) {
        this.comics = comics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_list, parent, false);
            return new ItemViewHolder(itemView);
        }
        else if (viewType == TYPE_FOOTER) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_list_footer, parent, false);
            return  new ComicAdapter.FooterViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Comic comic = this.comics.get(position);
            itemViewHolder.titleView.setText(comic.getTitle());
            itemViewHolder.dateView.setText(comic.getPublicationDate());
            itemViewHolder.imageView.setImageURI(comic.getImageUri());
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            if (showLoader) {
                footerViewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                footerViewHolder.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType (int position) {
        if (isPositionFooter(position))
            return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (comics == null || comics.size() == 0)
            return 0;
        return comics.size() + 1;
    }

    private boolean isPositionFooter (int position) {
        return position == comics.size();
    }

    public void setClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.clickListener = recyclerViewItemClickListener;
    }

    public void setShowLoader(boolean status) {
        showLoader = status;
    }
}