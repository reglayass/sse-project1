package sse.entity;

import java.util.HashMap;
import java.util.Map;

public class Titles {

    private String id;
    private String title;

    public Titles(String id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // to_dict equivalent
    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        return map;
    }
}