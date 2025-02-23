package sse.application;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class HttpClientService implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            getRequest(client);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRequest(HttpClient client) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/"))
                    .GET()
                    .build();

            for (int i = 0; i < 100; i++) {
                // Send the request and get the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Print the response
                System.out.println("Response status code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}