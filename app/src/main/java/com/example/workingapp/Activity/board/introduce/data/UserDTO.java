package com.example.workingapp.Activity.board.introduce.data;

import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class UserDTO {
    private String title;
    private String content;
    private int startCount = 0;

    private Map<String, Boolean> stars = new HashMap<>();

    public UserDTO() {
    }

    public UserDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStartCount() {
        return startCount;
    }

    public void setStartCount(int startCount) {
        this.startCount = startCount;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }
}