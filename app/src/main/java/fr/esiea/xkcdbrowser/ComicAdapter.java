package fr.esiea.xkcdbrowser;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {
    private ArrayList<Comic> comics;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView dateView;
        public SimpleDraweeView imageView;
        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.title);
            dateView = (TextView) v.findViewById(R.id.publication_date);
            imageView = (SimpleDraweeView) v.findViewById(R.id.image);
        }

    }

    public ComicAdapter(ArrayList<Comic> comics) {
        this.comics = comics;
    }

    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comic_list, parent, false);
        return new ComicAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comic comic = this.comics.get(position);
        holder.titleView.setText(comic.getTitle());
        holder.dateView.setText(comic.getPublicationDate());
        holder.imageView.setImageURI(comic.getImageUri());
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }
}