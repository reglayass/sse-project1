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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class Controller {

    private Map<String, Name> namesList = new HashMap<>();
    private Map<String, Title> titlesList = new HashMap<>();
    private Map<String, Rating> ratingList = new HashMap<>();

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
        return namesList.values().stream().map(Name::toDict).collect(Collectors.toList());
    }

    @GetMapping("/titles")
    public List<Map<String, Object>> getTitles() {
        return titlesList.values().stream().map(Title::toDict).collect(Collectors.toList());
    }

    @GetMapping("/filter")
    public List<Map<String, Object>> filterTitles(
            @RequestParam String genre,
            @RequestParam int rating,
            @RequestParam int votes) {

        // Create a set of titles that match the genre, rating, and votes criteria
        Set<String> resTitles = new HashSet<>();
        for (Title title : titlesList.values()) {
            if (title.getGenres().contains(genre)) {
                Rating titleRating = ratingList.get(title.getTconst());
                if (titleRating != null && titleRating.getAverageRating() >= rating
                        && titleRating.getNumVotes() >= votes) {
                    resTitles.add(title.getTconst());
                }
            }
        }

        // Filter the names based on the known titles
        return namesList.values().stream()
                .filter(n -> n.getKnownForTitles().stream().anyMatch(resTitles::contains))
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
                    List<String> primaryProfession = Arrays.asList(values[4].split(","));
                    List<String> knownForTitles = Arrays.asList(values[5].split(","));

                    namesList.put(nconst,
                            new Name(nconst, primaryName, birthYear, deathYear, primaryProfession, knownForTitles));
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
                Title title = Title.fromTSV(line);
                if (title != null) {
                    titlesList.put(title.getTconst(), title);
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

                    ratingList.put(tconst, new Rating(tconst, averageRating, numVotes));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}