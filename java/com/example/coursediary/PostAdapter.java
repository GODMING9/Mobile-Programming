package com.example.coursediary;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.textViewPostSnippet.setText(post.getContent());

        if (!post.getImageUrls().isEmpty()) {
            holder.recyclerViewPostThumbnails.setVisibility(View.VISIBLE);
            ImageThumbnailAdapter thumbnailAdapter = new ImageThumbnailAdapter(post.getImageUrls());
            holder.recyclerViewPostThumbnails.setAdapter(thumbnailAdapter);
            holder.recyclerViewPostThumbnails.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        } else {
            holder.recyclerViewPostThumbnails.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
            intent.putExtra("post", post);  // Post 객체를 전달
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPostSnippet;
        RecyclerView recyclerViewPostThumbnails;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPostSnippet = itemView.findViewById(R.id.textViewPostSnippet);
            recyclerViewPostThumbnails = itemView.findViewById(R.id.recyclerViewPostThumbnails);
        }
    }
}







