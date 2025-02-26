package sse.entity;

import java.util.HashMap;
import java.util.Map;

public class Rating {

    private String tconst;
    private double averageRating;
    private int numVotes;

    public Rating(String tconst, double averageRating, int numVotes) {
        this.tconst = tconst;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    // Getters and setters

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(int numVotes) {
        this.numVotes = numVotes;
    }

    // to_dict equivalent
    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("tconst", tconst);
        map.put("averageRating", averageRating);
        map.put("numVotes", numVotes);
        return map;
    }
}