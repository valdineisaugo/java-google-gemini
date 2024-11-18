package com.saugo.gemini;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/gemini")
public class GeminiController {
    private static final String API_KEY = "INSIRA AQUI SUA CHAVE DE API";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent?key=" + API_KEY;

    @GetMapping("/{consulta}")
    public String consulta(@PathVariable String consulta){
        JSONObject json = new JSONObject();
        json.put("contents", new JSONArray()
                .put(new JSONObject()
                        .put("parts", new JSONArray()
                                .put(new JSONObject()
                                        .put("text", consulta)
                                )
                        )
                )
        );
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray candidates = jsonObject.getJSONArray("candidates");
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            JSONObject firstPart = parts.getJSONObject(0);
            String text = firstPart.getString("text");
            System.out.println(text);
            return  "Resposta da IA: " + text;
        } catch (Exception e) {
            e.printStackTrace();
            return "Falha";
        }

    }
}
