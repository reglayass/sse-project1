package sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sse.entity.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class Controller {

    private List<Name> namesList = new ArrayList<>();
    private List<Title> titlesList = new ArrayList<>();
    private List<Rating> ratingList = new ArrayList<>();

    public Controller() {
        parseRatingsFromTSV("/data/title.ratings.tsv");
        parseNamesFromTSV("/data/name.basics.tsv");
        parseTitlesFromTSV("/data/title.basics.tsv");
    }

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/names")
    public List<Map<String, Object>> getNames() {
        return namesList.stream().map(Name::toDict).collect(Collectors.toList());
    }

    @GetMapping("/titles")
    public List<Map<String, Object>> getTitles() {
        return titlesList.stream().map(Title::toDict).collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<Map<String, Object>> filterTitles(
            @RequestParam String genre,
            @RequestParam int rating,
            @RequestParam int votes) {

        List<String> resTitles = titlesList.stream()
                .filter(t -> Arrays.asList(t.getGenres()).contains(genre) &&
                        ratingList.stream()
                                .filter(r -> r.getTconst().equals(t.getTconst()))
                                .anyMatch(r -> r.getAverageRating() >= rating && r.getNumVotes() >= votes))
                .map(Title::getTconst)
                .collect(Collectors.toList());

        return namesList.stream()
                .filter(n -> Arrays.asList(n.getKnownForTitles()).stream().anyMatch(resTitles::contains))
                .map(Name::toDict)
                .collect(Collectors.toList());
    }

    private void parseNamesFromTSV(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                if (values.length >= 6) {
                    String nconst = values[0];
                    String primaryName = values[1];
                    Integer birthYear = values[2].equals("\\N") ? null : Integer.parseInt(values[2]);
                    Integer deathYear = values[3].equals("\\N") ? null : Integer.parseInt(values[3]);
                    String[] primaryProfession = values[4].split(",");
                    String[] knownForTitles = values[5].split(",");
    
                    namesList.add(new Name(nconst, primaryName, birthYear, deathYear, primaryProfession, knownForTitles));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseTitlesFromTSV(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
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

                    titlesList.add(new Title(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear,
                            endYear, runtimeMinutes, genres));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRatingsFromTSV(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                if (values.length >= 3) {
                    String tconst = values[0];
                    double averageRating = Double.parseDouble(values[1]);
                    int numVotes = Integer.parseInt(values[2]);
    
                    ratingList.add(new Rating(tconst, averageRating, numVotes));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}