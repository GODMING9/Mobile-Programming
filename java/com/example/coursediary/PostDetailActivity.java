package com.example.coursediary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {
    private TextView textViewPostTimestamp;
    private TextView textViewPostContent;
    private LinearLayout imageContainer;
    private LinearLayout locationContainer;
    private RecyclerView recyclerViewImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        textViewPostTimestamp = findViewById(R.id.textViewPostTimestamp);
        textViewPostContent = findViewById(R.id.textViewPostContent);
        imageContainer = findViewById(R.id.imageContainer);
        locationContainer = findViewById(R.id.locationContainer);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);

        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("post");

        if (post != null) {
            // 작성 시간을 형식에 맞게 변환하여 설정
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String formattedDate = sdf.format(new Date(post.getTimeStamp()));
            textViewPostTimestamp.setText(formattedDate);

            textViewPostContent.setText(post.getContent());

            List<String> imageUrls = post.getImageUrls();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                recyclerViewImages.setVisibility(View.VISIBLE);
                imageContainer.setVisibility(View.GONE);
                ImageSliderAdapter adapter = new ImageSliderAdapter(this, imageUrls);
                recyclerViewImages.setAdapter(adapter);
                recyclerViewImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            } else {
                recyclerViewImages.setVisibility(View.GONE);
                imageContainer.setVisibility(View.VISIBLE);
            }

            List<String> locations = post.getLocations();
            if (locations != null && !locations.isEmpty()) {
                for (String location : locations) {
                    TextView textView = new TextView(this);
                    textView.setText(location);
                    locationContainer.addView(textView);
                }
            }
        }
    }
}
