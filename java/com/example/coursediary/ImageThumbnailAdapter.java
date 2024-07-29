package com.example.coursediary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageThumbnailAdapter extends RecyclerView.Adapter<ImageThumbnailAdapter.ImageThumbnailViewHolder> {
    private final List<String> imageUrls;

    public ImageThumbnailAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_thumbnail_item, parent, false);
        return new ImageThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageThumbnailViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Picasso.get().load(imageUrl).into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageThumbnailViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewThumbnail;

        public ImageThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }
}
