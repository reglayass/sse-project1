package sse.entity;

import java.util.HashMap;
import java.util.Map;

public class Name {

    private String nconst;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private String[] primaryProfession;
    private String[] knownForTitles;

    public Name(String nconst, String name, Integer birthYear, Integer deathYear, String[] primaryProfession, String[] knownForTitles) {
        this.nconst = nconst;
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.primaryProfession = primaryProfession;
        this.knownForTitles = knownForTitles;
    }

    public String getNconst() {
        return nconst;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public String[] getPrimaryProfession() {
        return primaryProfession;
    }

    public void setPrimaryProfession(String[] primaryProfession) {
        this.primaryProfession = primaryProfession;
    }

    public String[] getKnownForTitles() {
        return knownForTitles;
    }

    public void setKnownForTitles(String[] knownForTitles) {
        this.knownForTitles = knownForTitles;
    }

    // to_dict equivalent
    public Map<String, Object> toDict() {
        Map<String, Object> map = new HashMap<>();
        map.put("nconst", nconst);
        map.put("name", name);
        map.put("birthYear", birthYear);
        map.put("deathYear", deathYear);
        map.put("primaryProfession", primaryProfession);
        map.put("knownForTitles", knownForTitles);
        return map;
    }
}