package sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sse.entity.Names;
import sse.entity.Titles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Controller {

    private List<Names> namesList = new ArrayList<>();
    private List<Titles> titlesList = new ArrayList<>();

    public Controller() {
        parseNamesFromTSV("/data/name.basics.tsv");
        parseTitlesFromTSV("/data/title.basics.tsv");
    }

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/names")
    public List<Map<String, Object>> getNames() {
        return namesList.stream().map(Names::toDict).collect(Collectors.toList());
    }

    @GetMapping("/titles")
    public List<Map<String, Object>> getTitles() {
        return titlesList.stream().map(Titles::toDict).collect(Collectors.toList());
    }

    private void parseNamesFromTSV(String filePath) {
        try (InputStream is = getClass().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                if (values.length >= 2) {
                    String nconst = values[0];
                    String primaryName = values[1];
                    namesList.add(new Names(nconst, primaryName));
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
                if (values.length >= 2) {
                    String nconst = values[0];
                    String primaryName = values[1];
                    titlesList.add(new Titles(nconst, primaryName));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}