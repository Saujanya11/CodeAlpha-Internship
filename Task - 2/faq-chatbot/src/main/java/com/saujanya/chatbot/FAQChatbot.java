package com.saujanya.chatbot;

import edu.stanford.nlp.simple.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class FAQChatbot {

    private List<FAQ> faqList;
    private final double SIMILARITY_THRESHOLD = 0.5; // Lowered threshold for better matching

    private static class FAQ {

        private String question;
        private String answer;
        private Set<String> keywords;

        public FAQ(String question, String answer) {
            this.question = question;
            this.answer = answer;
            this.keywords = extractKeywords(question);
        }

        private Set<String> extractKeywords(String text) {
            Set<String> keywords = new HashSet<>();
            Sentence sentence = new Sentence(text);

            // Extract lemmas and filter out stopwords
            List<String> lemmas = sentence.lemmas();
            List<String> posTags = sentence.posTags();

            for (int i = 0; i < lemmas.size(); i++) {
                String lemma = lemmas.get(i).toLowerCase();
                String pos = posTags.get(i);

                // Keep only content words (nouns, verbs, adjectives, adverbs)
                if (
                    pos.startsWith("NN") ||
                    pos.startsWith("VB") ||
                    pos.startsWith("JJ") ||
                    pos.startsWith("RB")
                ) {
                    keywords.add(lemma);
                }
            }

            return keywords;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }

        public Set<String> getKeywords() {
            return keywords;
        }
    }

    public FAQChatbot(String faqJsonPath) throws IOException {
        loadFAQs(faqJsonPath);
        initializeDefaultResponses();
    }

    private void initializeDefaultResponses() {
        // Add some basic responses for common questions
        faqList.add(new FAQ("what is your name", "I'm an FAQ Chatbot designed to help you find information."));
        faqList.add(new FAQ("who are you", "I'm an FAQ Chatbot designed to help you find information."));
        faqList.add(new FAQ("how are you", "I'm functioning well, thank you for asking. How can I help you today?"));
        faqList.add(new FAQ("what can you do", "I can answer questions from my FAQ database."));
        faqList.add(new FAQ("help", "You can ask me questions and I'll try to provide answers. Type 'exit' to quit."));
    }

    private void loadFAQs(String faqJsonPath) throws IOException {
        faqList = new ArrayList<>();
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(faqJsonPath)));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray faqArray = jsonObject.getJSONArray("faqs");

            for (int i = 0; i < faqArray.length(); i++) {
                JSONObject faq = faqArray.getJSONObject(i);
                String question = faq.getString("question");
                String answer = faq.getString("answer");
                faqList.add(new FAQ(question, answer));
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load FAQ file: " + e.getMessage());
            System.err.println("Starting with empty FAQ database. Default responses will be available.");
        }
    }

    public String processQuery(String query) {
        try {
            // Check if it's a greeting
            if (isGreeting(query)) {
                return "Hello! How can I help you today?";
            }
            
            Set<String> queryKeywords = extractKeywords(query);

            FAQ bestMatch = findBestMatch(queryKeywords);

            if (bestMatch != null) {
                double similarity = calculateSimilarity(queryKeywords, bestMatch.getKeywords());
                if (similarity >= SIMILARITY_THRESHOLD) {
                    return bestMatch.getAnswer();
                }
            }
            
            // If no match is found in FAQs, return a generic response
            return "I don't have information about that. Could you try asking a different question?";
        } catch (Exception e) {
            // Catch all exceptions to prevent crashes
            System.err.println("Error processing query: " + e.getMessage());
            return "I'm having trouble processing your question. Could you try asking in a different way?";
        }
    }

    private boolean isGreeting(String query) {
        String lowerQuery = query.toLowerCase().trim();
        return lowerQuery.equals("hello") || 
               lowerQuery.equals("hi") || 
               lowerQuery.equals("hey") || 
               lowerQuery.equals("greetings");
    }

    private Set<String> extractKeywords(String query) {
        Set<String> keywords = new HashSet<>();
        Sentence sentence = new Sentence(query);

        // Extract lemmas and filter out stopwords
        List<String> lemmas = sentence.lemmas();
        List<String> posTags = sentence.posTags();

        for (int i = 0; i < lemmas.size(); i++) {
            String lemma = lemmas.get(i).toLowerCase();
            String pos = posTags.get(i);

            // Keep only content words (nouns, verbs, adjectives, adverbs)
            if (
                pos.startsWith("NN") ||
                pos.startsWith("VB") ||
                pos.startsWith("JJ") ||
                pos.startsWith("RB")
            ) {
                keywords.add(lemma);
            }
        }

        return keywords;
    }

    private FAQ findBestMatch(Set<String> queryKeywords) {
        FAQ bestMatch = null;
        double highestSimilarity = 0;

        for (FAQ faq : faqList) {
            double similarity = calculateSimilarity(queryKeywords, faq.getKeywords());

            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                bestMatch = faq;
            }
        }

        return bestMatch;
    }

    private double calculateSimilarity(
        Set<String> queryKeywords,
        Set<String> faqKeywords
    ) {
        if (queryKeywords.isEmpty() || faqKeywords.isEmpty()) {
            return 0.0;
        }

        Set<String> intersection = new HashSet<>(queryKeywords);
        intersection.retainAll(faqKeywords);

        Set<String> union = new HashSet<>(queryKeywords);
        union.addAll(faqKeywords);

        return (double) intersection.size() / union.size();
    }

    public static void main(String[] args) {
        // Suppress the CoreNLP pipeline conflict error
        System.setProperty("edu.stanford.nlp.simple.Sentence.disablePipeline", "true");

        try {
            // Use relative path with fallback
            String faqPath = "faqs.json";
            if (args.length > 0) {
                faqPath = args[0]; // Allow passing path as command line argument
            }
            
            FAQChatbot chatbot = new FAQChatbot(faqPath);

            Scanner scanner = new Scanner(System.in);
            System.out.println("FAQ Chatbot initialized. Type 'exit' to quit.");

            while (true) {
                System.out.print("You: ");
                String userInput = scanner.nextLine().trim();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                String response = chatbot.processQuery(userInput);
                System.out.println("Bot: " + response);
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Critical error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}