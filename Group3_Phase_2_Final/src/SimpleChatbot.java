import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.*;

public class SimpleChatbot extends JFrame {

    /** SimpleChatbot GUI for Basic ChatBot */
    private static final long serialVersionUID = 1L;
    private JTextField userInput; // text field for user input
    private JTextArea chatArea; // text area for the conversation with the user
    private JButton sendButton; // button to send the user's message
    private Map<String, String> responses; // map to store predefined responses
    private JTextArea analysisArea; // text area for CoreNLP analysis
    private CoreNLP analyzeBot; // CoreNLP analyzer for text analysis

    // Constructor method to setup the GUI and initialize responses
    public SimpleChatbot() {
        super("ChatbotApp");
        analyzeBot = new CoreNLP();

        responses = new HashMap<>();
        populateResponses();

        setLayout(new BorderLayout());

        // Create the welcome message from the Chatbot
        String welcomeMessage = "Bot: Hello, how can I help you?\n";
        chatArea = new JTextArea(welcomeMessage);
        chatArea.setEditable(false);

        // Instantiate the analysisArea with a new JTextArea object
        analysisArea = new JTextArea();
        analysisArea.setEditable(false);

        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        JScrollPane analysisScrollPane = new JScrollPane(analysisArea);

        // Create a JSplitPane to display the chat and analysis areas side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScrollPane, analysisScrollPane);
        splitPane.setDividerLocation(0.5); // Set the initial position of the divider

        add(splitPane, BorderLayout.CENTER);

        // Create a box to hold the user input field and send button
        Box box = Box.createHorizontalBox();
        userInput = new JTextField();
        box.add(userInput);
        sendButton = new JButton("Send");
        box.add(sendButton);
        add(box, BorderLayout.SOUTH);

        // ActionListener for the sendButton
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = userInput.getText().trim();
                if (!input.isEmpty()) {
                    chatArea.append("User: " + input + "\n");
                    analysisArea.append("Analysis:\n" + analyzeBot.analyzeText(input) + "\n");
                    String response = processInput(input.toLowerCase());
                    chatArea.append("Bot: " + response + "\n");
                    userInput.setText("");
                }
            }
        });

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // Method to populate the responses map
    private void populateResponses() {
        // Add predefined responses
        responses.put("hello", "Hi, how can I help you?");
        responses.put("what is your name?", "I'm a chatbot, I don't have a name.");
        responses.put("how are you?", "I'm a bot, I don't have feelings, but thanks for asking!");
        // Add more predefined responses here
    }

    // Method to process user input and provide appropriate responses
    private String processInput(String input) {
        // Check for exit commands
        if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("i'm done")) {
            System.exit(0); // Close the application
        }
        // Check if the user is asking for a definition
        if (input.startsWith("what does ")) {
            String key = input.substring("what does ".length());
            if (key.endsWith(" mean")) {
                key = key.substring(0, key.length() - " mean".length());
            }
            return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition(key);
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
    private static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] : dp[i - 1][j - 1] + 1,
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    // The application entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleChatbot chatbot = new SimpleChatbot();
            chatbot.setVisible(true);
        });
    }
}