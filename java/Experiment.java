import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Experiment {
    public static void main(String[] args) {
        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Build the GET request for GitHub's API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com"))
                .GET()
                .build();

        try {
            // Send the request and get the response as a String
            for (int i = 0; i < 100; i++) {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
                // Print out the status code and response body
                System.out.println("Status Code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
