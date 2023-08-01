package groupProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class CombinedChatBot extends JFrame {

    private StanfordCoreNLP pipeline;
    private JTextField userInput; // text field for user input
    private JTextArea chatArea; // text area for the conversation
    private JButton sendButton; // button to send the message
    private Map<String, String> responses; // map to store predefined responses

    public CombinedChatBot() {
        // Initialize the responses map and populate it
        responses = new HashMap<>();
        populateResponses();

        // Set the layout manager for this frame
        setLayout(new BorderLayout());
        

        // Setup the chat area
        chatArea = new JTextArea();
        
        
     // Set the background color for the chat area
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.BLUE);
        
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

     // Setup the input area
        Box box = Box.createHorizontalBox();
        userInput = new JTextField();
        
     // Set the background color for the input area
        userInput.setBackground(Color.WHITE);
        userInput.setForeground(Color.BLACK);
        
     // Set action on userInput to trigger when Enter is pressed
        userInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call the sendButton's click action
                sendButton.doClick();
            }
        });
        
        box.add(userInput);
        sendButton = new JButton("Send");

        // Set the background color for the send button
        sendButton.setBackground(Color.BLACK);
        sendButton.setForeground(Color.BLACK);
        box.add(sendButton);
        add(box, BorderLayout.SOUTH);

        // Initialize Stanford CoreNLP with an annotator for tokenization, ssplit, pos, lemma, ner, parse, and sentiment
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);

        // Add the action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the user's input from the text field
                String text = userInput.getText().trim();

                // Append the user's input to the chat area and a new line character
                chatArea.append("User: " + text + "\n");

                // Analyze the user's input and get the bot's response
                String response = analyzeText(text);
                
                // Append the current time
                LocalTime timeNow = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            
                // Append the bot's response and the current time to the chat area and a new line character
                chatArea.append("Bot (" + timeNow.format(formatter) + "): " + response + "\n");

                // Clear the text field
                userInput.setText("");
            }
        });
        
        // Add the action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            // Action to be performed when the send button is pressed
            public void actionPerformed(ActionEvent e) {
                // Get the input from the user
                String input = userInput.getText().trim();
                if (!input.isEmpty()) {
                    // Append the user's message to the chat
                    chatArea.append("User: " + input + "\n");
                    // Get the bot's response and append it to the chat
                    String response = processInput(input.toLowerCase());
                    
                    // Append the current time
                    LocalTime timeNow = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    chatArea.append("Bot (" + timeNow.format(formatter) + "): " + response + "\n");
                    // Clear the input field for the next message
                    userInput.setText("");
                }
            }
        });

        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to populate the responses map
    private void populateResponses() {
        // Add predefined responses
        responses.put("hello", "Hi, how can I help you?");
        responses.put("what is your name?", "I'm a chatbot, I don't have a name.");
        responses.put("how are you?", "I'm a bot, I don't have feelings, but thanks for asking!");
    }

    
    // Method to process the user input and get the bot's response
    private String processInput(String input) {
        // Check for exit commands
        if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("i'm done")) {
            System.exit(0); // Close the application
        }
     // If user asks for current date
        else if (input.equalsIgnoreCase("what is the date today") || input.equalsIgnoreCase("what's the date today")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            return "Today's date is " + dtf.format(now);
            }
        
     // If user asks for current time
        else if (input.equalsIgnoreCase("what is the time now") || input.equalsIgnoreCase("what's the time now")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            return "Current time is " + dtf.format(now);
        }
        
        // Check if the user wants to perform a mathematical operation
        else {
            Pattern p = Pattern.compile("([0-9]+)\\s*([+\\-*/])\\s*([0-9]+)");
            Matcher m = p.matcher(input);
            if (m.find()) {
                int num1 = Integer.parseInt(m.group(1));
                int num2 = Integer.parseInt(m.group(3));
                String operator = m.group(2);
                switch (operator) {
                    case "+":
                        return "The result is " + (num1 + num2);
                    case "-":
                        return "The result is " + (num1 - num2);
                    case "*":
                        return "The result is " + (num1 * num2);
                    case "/":
                        if (num2 != 0)
                            return "The result is " + ((double) num1 / num2);
                        else
                            return "Division by zero is not allowed.";
                }
            }
        }
        
        // Find the closest match in predefined responses
        String responseKey = responses.keySet().stream().min((k1, k2) -> {
            return editDistance(k1, input) - editDistance(k2, input);
        }).orElse(null);

        // If the closest match is close enough, return the response
        if (responseKey != null && editDistance(responseKey, input) <= 3) {
            return responses.get(responseKey);
        }

        // If no close match, return a default response
        return "I'm sorry, I didn't understand that.";
    }

    // Compute the Levenshtein distance (edit distance) between two strings
    private int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] :
                                        dp[i - 1][j - 1] + 1,
                                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }

        return dp[a.length()][b.length()];
    }
    
    public String analyzeText(String text) {
        // Create a document object and annotate it
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        StringBuilder output = new StringBuilder();
        StringBuilder namedEntities = new StringBuilder();

        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            output.append("Sentence: ").append(sentence.toString()).append("\n");

            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            output.append("Sentiment: ").append(sentiment).append("\n");

            // Iterate over all tokens in a sentence
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the output
                String word = token.get(TextAnnotation.class);
                String lemma = token.get(LemmaAnnotation.class);
                String pos = token.get(PartOfSpeechAnnotation.class);
                String ner = token.get(NamedEntityTagAnnotation.class);

                output.append("Word: ").append(word).append("\n");
                output.append("Lemma: ").append(lemma).append("\n");
                output.append("POS: ").append(pos).append("\n");
                output.append("NER: ").append(ner).append("\n");

                // If a named entity is recognized, add it to the namedEntities StringBuilder
                if (!"O".equals(ner)) {
                    namedEntities.append(word).append(" (").append(ner).append("), ");
                }
            }
        }
        if (namedEntities.length() > 0) {
            // Remove the last comma and space, and add a period
            namedEntities.setLength(namedEntities.length() - 2);
            namedEntities.append(".");
            output.append("\nNamed entities recognized: ").append(namedEntities.toString()).append("\n");
        }
        return output.toString();
    }

    public static void main(String[] args) {
        // Create a new instance of UnifiedChatbot, which will show the GUI
        new CombinedChatBot().setVisible(true);
    }
}
