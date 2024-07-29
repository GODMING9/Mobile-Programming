package com.example.coursediary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private final List<Uri> imageUris = new ArrayList<>();
    private final List<String> locations = new ArrayList<>();
    private LinearLayout imageContainer;
    private EditText editTextPostContent;
    private EditText editTextLocation;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private ImageAdapter imageAdapter;
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        editTextPostContent = findViewById(R.id.editTextPostContent);
        editTextLocation = findViewById(R.id.editTextLocation);
        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        Button buttonAddLocation = findViewById(R.id.buttonAddLocation);
        Button buttonSubmitPost = findViewById(R.id.buttonSubmitPost);
        imageContainer = findViewById(R.id.imageContainer);

        buttonSelectImage.setOnClickListener(v -> openFileChooser());
        buttonAddLocation.setOnClickListener(v -> addLocation());
        buttonSubmitPost.setOnClickListener(v -> submitPost());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        imageAdapter = new ImageAdapter(imageUris);
        recyclerView.setAdapter(imageAdapter);

        RecyclerView recyclerViewLocations = findViewById(R.id.recycler_view_locations);
        recyclerViewLocations.setLayoutManager(new LinearLayoutManager(this));
        locationAdapter = new LocationAdapter(locations);
        recyclerViewLocations.setAdapter(locationAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imageUris.clear();
            imageContainer.removeAllViews();
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageURI(imageUri);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                    imageView.setPadding(10, 10, 10, 10);
                    imageContainer.addView(imageView);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                ImageView imageView = new ImageView(this);
                imageView.setImageURI(imageUri);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(250, 250));
                imageView.setPadding(10, 10, 10, 10);
                imageContainer.addView(imageView);
            }
            imageAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addLocation() {
        String location = editTextLocation.getText().toString().trim();
        if (!location.isEmpty()) {
            locations.add(location);
            editTextLocation.setText("");
            locationAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }

    private void submitPost() {
        if (!imageUris.isEmpty() && !editTextPostContent.getText().toString().isEmpty()) {
            List<String> uploadedImageUrls = new ArrayList<>();
            for (Uri imageUri : imageUris) {
                String fileName = System.currentTimeMillis() + "." + getFileExtension(imageUri);
                StorageReference fileReference = storage.getReference("posts").child(fileName);
                fileReference.putFile(imageUri)
                        .continueWithTask(task -> {
                            if (!task.isSuccessful() && task.getException() != null) {
                                throw task.getException();
                            }
                            return fileReference.getDownloadUrl();
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                uploadedImageUrls.add(task.getResult().toString());
                                if (uploadedImageUrls.size() == imageUris.size()) {
                                    savePostDataToFirestore(uploadedImageUrls);
                                }
                            } else {
                                Toast.makeText(PostActivity.this, "Failed to upload image: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Toast.makeText(this, "Please add at least one image and some text", Toast.LENGTH_LONG).show();
        }
    }

    private void savePostDataToFirestore(List<String> imageUrls) {
        Map<String, Object> post = new HashMap<>();
        post.put("content", editTextPostContent.getText().toString());
        post.put("imageUrls", imageUrls);
        post.put("userId", Objects.requireNonNull(auth.getCurrentUser()).getUid());
        post.put("timeStamp", System.currentTimeMillis());
        post.put("locations", locations);

        db.collection("posts").add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Post submitted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error submitting post", Toast.LENGTH_SHORT).show());
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}







