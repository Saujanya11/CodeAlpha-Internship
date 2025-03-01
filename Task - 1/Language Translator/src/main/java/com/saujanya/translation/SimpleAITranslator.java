package com.saujanya.translation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONObject; // Import the JSON library

public class SimpleAITranslator {

    // LibreTranslate API endpoint (public instance)
    private static final String API_URL = "http://localhost:5000/translate";

    // Available languages (Hindi moved to the top)
    private static final String[][] LANGUAGES = {
        {"Hindi", "hi"}, // Hindi is now the first option
        {"English", "en"},
        {"Spanish", "es"},
        {"French", "fr"},
        {"German", "de"},
        {"Italian", "it"},
        {"Portuguese", "pt"},
        {"Russian", "ru"},
        {"Chinese", "zh"}
    };

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Display welcome message
            System.out.println("======================================");
            System.out.println("   AI NEURAL TRANSLATION TOOL");
            System.out.println("======================================");
            System.out.println("Using neural machine translation\n");

            boolean running = true;

            while (running) {
                // Display available languages
                System.out.println("Available languages:");
                for (int i = 0; i < LANGUAGES.length; i++) {
                    System.out.println((i + 1) + ". " + LANGUAGES[i][0]);
                }

                // Get source language
                System.out.print("\nSelect source language (number or 0 to exit): ");
                int sourceIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // consume newline

                if (sourceIndex == -1) {
                    running = false;
                    break;
                }

                if (sourceIndex < 0 || sourceIndex >= LANGUAGES.length) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                // Get target language
                System.out.print("Select target language (number): ");
                int targetIndex = scanner.nextInt() - 1;
                scanner.nextLine(); // consume newline

                if (targetIndex < 0 || targetIndex >= LANGUAGES.length) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                // Get text to translate
                System.out.print("Enter text to translate: ");
                String textToTranslate = scanner.nextLine();

                if (textToTranslate.isEmpty()) {
                    System.out.println("No text entered. Please try again.");
                    continue;
                }

                // Perform translation
                try {
                    System.out.println("\nTranslating using neural machine translation...");
                    String translatedText = translateText(
                        textToTranslate,
                        LANGUAGES[sourceIndex][1],
                        LANGUAGES[targetIndex][1]
                    );

                    System.out.println("\n------ Translation Result ------");
                    System.out.println("From: " + LANGUAGES[sourceIndex][0]);
                    System.out.println("To: " + LANGUAGES[targetIndex][0]);
                    System.out.println("\nOriginal: " + textToTranslate);
                    System.out.println("Translated: " + translatedText);
                    System.out.println("-------------------------------\n");
                } catch (Exception e) {
                    System.out.println("Error during translation: " + e.getMessage());
                    // Fallback to a simple message when API is unavailable
                    if (e.getMessage().contains("Connection refused") ||
                        e.getMessage().contains("timed out") ||
                        e.getMessage().contains("HTTP error")) {
                        System.out.println("\nAPI connection issue. This can happen if:");
                        System.out.println("1. Your internet connection is unavailable");
                        System.out.println("2. The translation service is experiencing high traffic");
                        System.out.println("3. The service is temporarily down for maintenance");
                        System.out.println("\nPlease try again later.");
                    }
                }

                // Ask to continue
                System.out.print("Translate another text? (y/n): ");
                String continueChoice = scanner.nextLine().trim().toLowerCase();
                running = continueChoice.equals("y") || continueChoice.equals("yes");
                System.out.println();
            }

            System.out.println("Thank you for using the AI Neural Translation Tool!");
        }
    }

    /**
     * Translate text using LibreTranslate API
     *
     * @param text           The text to translate
     * @param sourceLanguage The source language code
     * @param targetLanguage The target language code
     * @return The translated text
     * @throws IOException If an error occurs during the API call
     */
    private static String translateText(String text, String sourceLanguage, String targetLanguage) throws IOException {
        try {
            // Create URL and open connection
            URL url = new URI(API_URL).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000); // 10 seconds timeout
            connection.setReadTimeout(10000);    // 10 seconds read timeout

            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Prepare the request parameters
            String params = "q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                            "&source=" + sourceLanguage +
                            "&target=" + targetLanguage +
                            "&format=text" +
                            "&api_key=";  // Most instances don't require API key, but parameter is needed

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            // Read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Parse JSON response using org.json
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("translatedText")) {
                    String translatedText = jsonResponse.getString("translatedText");
                    return translatedText;
                }

                throw new IOException("Failed to parse translation response: " + jsonResponse.toString());
            }
        } catch (URISyntaxException e) {
            throw new IOException("Invalid API URL: " + API_URL, e);
        }
    }
}