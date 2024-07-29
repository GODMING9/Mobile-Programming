package com.example.coursediary;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {
    private String content;
    private List<String> imageUrls;
    private String userId;
    private long timeStamp;
    private List<String> locations; // 추가된 부분

    public Post() {
        // 빈 생성자는 Firebase가 필요로 합니다.
    }

    public Post(String content, List<String> imageUrls, String userId, long timeStamp, List<String> locations) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.userId = userId;
        this.timeStamp = timeStamp;
        this.locations = locations;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}




