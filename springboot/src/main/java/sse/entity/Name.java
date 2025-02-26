package sse.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Name {

    private String nconst;
    private String name;
    private Integer birthYear;
    private Integer deathYear;
    private List<String> primaryProfession;
    private List<String> knownForTitles;

    public Name(String nconst, String name, Integer birthYear, Integer deathYear, List<String> primaryProfession, List<String> knownForTitles) {
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

    public Integer getBirthYear() {
        return birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public List<String> getPrimaryProfession() {
        return primaryProfession;
    }

    public List<String> getKnownForTitles() {
        return knownForTitles;
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