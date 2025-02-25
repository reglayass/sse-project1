package sse.entity;

import java.util.HashMap;
import java.util.Map;

public class Names {

    private String id;
    private String name;

    public Names(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // to_dict equivalent
    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}