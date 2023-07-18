import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// The chatbot application main class extending from JFrame for GUI
public class SimpleChatbot extends JFrame {

    private JTextField userInput; // text field for user input
    private JTextArea chatArea; // text area for the conversation
    private JButton sendButton; // button to send the message
    private Map<String, String> responses; // map to store predefined responses

    // Constructor method to setup the GUI and initialize responses
    public SimpleChatbot() {
        super("ChatbotApp");

        // Initialize the responses map and populate it
        responses = new HashMap<>();
        populateResponses();

        // Set the layout manager for this frame
        setLayout(new BorderLayout());

        // Setup the chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Setup the input area
        Box box = Box.createHorizontalBox();
        userInput = new JTextField();
        box.add(userInput);
        sendButton = new JButton("Send");
        box.add(sendButton);
        add(box, BorderLayout.SOUTH);

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
                    chatArea.append("Bot: " + response + "\n");
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
        // Add more predefined responses here
    }

    // Method to process the user input and get the bot's response
    private String processInput(String input) {
        // Check for exit commands
        if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("i'm done")) {
            System.exit(0); // Close the application
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

    // The application entry point
    public static void main(String[] args) {
        // Create and display the chatbot
        new SimpleChatbot().setVisible(true);
    }
}
