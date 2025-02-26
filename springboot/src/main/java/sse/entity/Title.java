package sse.entity;

import java.util.HashMap;
import java.util.Map;

public class Title {

    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;
    private String[] genres;

    public Title(String tconst, String titleType, String primaryTitle, String originalTitle, boolean isAdult, Integer startYear, Integer endYear, Integer runtimeMinutes, String[] genres) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;
    }

    // Getters and setters

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    // to_dict equivalent
    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("tconst", tconst);
        map.put("titleType", titleType);
        map.put("primaryTitle", primaryTitle);
        map.put("originalTitle", originalTitle);
        map.put("isAdult", isAdult);
        map.put("startYear", startYear);
        map.put("endYear", endYear);
        map.put("runtimeMinutes", runtimeMinutes);
        map.put("genres", genres);
        return map;
    }

    // Static method to parse a line from the TSV file and create a Title object
    public static Title fromTSV(String line) {
        String[] values = line.split("\t");
        if (values.length >= 9) {
            String tconst = values[0];
            String titleType = values[1];
            String primaryTitle = values[2];
            String originalTitle = values[3];
            boolean isAdult = values[4].equals("1");
            Integer startYear = values[5].equals("\\N") ? null : Integer.parseInt(values[5]);
            Integer endYear = values[6].equals("\\N") ? null : Integer.parseInt(values[6]);
            Integer runtimeMinutes = values[7].equals("\\N") ? null : Integer.parseInt(values[7]);
            String[] genres = values[8].split(",");

            return new Title(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres);
        }
        return null;
    }
}