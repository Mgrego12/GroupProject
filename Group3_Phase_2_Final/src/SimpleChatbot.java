import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
/*
 * Group 3 ChatBot Java Program - Phase II CMSC495
 * Memebers - Matthew Gregorek, NFamouusa Naite jr. , Bryan Duque
 * CoreNLP 
 */

public class SimpleChatbot extends JFrame {

    /** SimpleChatbot GUI for Basic ChatBot */
    private static final long serialVersionUID = 1L;
    private JTextField userInput; // text field for input
    private JTextArea chatArea; // text area for bot-user response
    private JButton sendButton; // button to send the user's message
    private Map<String, String> responses; // map to store predefined responses
    private JTextArea analysisArea; // text area for CoreNLP analysis
    private CoreNLP analyzeBot; // CoreNLP analyzer for text analysis

    public SimpleChatbot() {
    	/* SimpleChatbot constructor method. 
    	 * Used for creating the GUI for "ChatbotApp" 
    	 * initliazes components : textfields, button, areas, event listeners
    	 * It will have 2 separate areas , a chatArea and Analysis area. 
    	 * The sendButton uses ActionListener to get User Input and add it to 
    	 * appropiate chat Area. 
    	 * */
    	
        super("ChatbotApp"); // super JFrame title "ChatbotApp"
        analyzeBot = new CoreNLP(); // Init CoreNLP analyzer

        responses = new HashMap<>(); // Hashmap to store predefined Responses
        populateResponses();

        setLayout(new BorderLayout()); // Layout of JFrame to BorderLayout

        // Create the welcome message from the Chatbot 
        // So that user see's a Message when they Open GUI
        String welcomeMessage = "Bot: Hello, how can I help you?\n";
        chatArea = new JTextArea(welcomeMessage);
        chatArea.setEditable(false);

        // Instantiate the analysisArea with a new JTextArea object
        analysisArea = new JTextArea(); // new JTextField for Analysis 
        analysisArea.setEditable(false);

        JScrollPane chatScrollPane = new JScrollPane(chatArea); // ScrollPanes for Chat/Analysis areas
        JScrollPane analysisScrollPane = new JScrollPane(analysisArea);

        // Create a JSplitPane to display the chat and analysis areas side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScrollPane, analysisScrollPane);
        splitPane.setDividerLocation(0.5); // Set the initial position of the divider

        add(splitPane, BorderLayout.CENTER); // split the two areas in the center

        // Create a box to hold the user input field and send button
        Box box = Box.createHorizontalBox(); 
        userInput = new JTextField(); // userInput text field
        box.add(userInput); // add userInput to the box
        sendButton = new JButton("Send"); // make JButton for Sending Input
        box.add(sendButton); // add sendButton to box
        add(box, BorderLayout.SOUTH); // add to bottom of frame

        // ActionListener for the sendButton
        sendButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                String input = userInput.getText().trim(); // create input String to get user input from TextField
                if (!input.isEmpty()) { // make sure it's not empty
                    chatArea.append("User: " + input + "\n"); // append all user Input to ChatArea
                    analysisArea.append("Analysis:\n" + analyzeBot.analyzeText(input) + "\n"); // Append Analysis of that Input to AnlysisArea
                    String response = processInput(input.toLowerCase());  // chatbot's response based of input ( still working on response System)
                    chatArea.append("Bot: " + response + "\n"); // Send Chatbot's response to the ChatArea
                    userInput.setText(""); // clear user input
                }
            }
        });

        setSize(800, 600); // 800x600 size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // close on exit
    }

    // Method to populate the responses map
    private void populateResponses() {
    	/* populateResponses from Phase 1, we will continue to make this
    	 * more robust for Phase 3 */
        responses.put("hello", "Hi, how can I help you?"); 
        responses.put("what is your name?", "I'm a chatbot, I don't have a name.");
        responses.put("how are you?", "I'm a bot, I don't have feelings, but thanks for asking!");
        responses.put("what's the weather?", "I'm sorry, I can't provide real-time information.");
        responses.put("tell me a joke", "Why don't we ever tell secrets on a farm? Because the potatoes have eyes, the corn has ears, and the beans stalk.");
        // Add more predefined responses for future reference
    }

    // Method to process user input and provide appropriate responses
    private String processInput(String input) {
        /* In this method, we have added some if statements to check for certain input.
         * if quite/im done is entered, the GUI will close. For CoreNLP 
         * Annotations, the user has to enter "what does {lemma,pos,ner...etc } mean"
         * the corresponding definitions from CoreNLP NLP_CORE_DEFINITIONS will be called
         * we intend to make this more robust in Phase 3 also. 
         * */
        if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("i'm done")) {
            System.exit(0); // Close the application
        }
        // Check if the user is asking for a definition
        if (input.startsWith("what does ")) {
            String key = input.substring("what does ".length());
            if (key.endsWith(" mean")) {
                key = key.substring(0, key.length() - " mean".length());
            }
            return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition(key); // if key starts with "what does, and ends with mean, return corresponding Definition
        }
        // Find the closest match in predefined responses
        String responseKey = responses.keySet().stream().min((k1, k2) -> {
            return editDistance(k1, input) - editDistance(k2, input);
        }).orElse(null);

        // If the closest match is close enough, return the response
        if (responseKey != null && editDistance(responseKey, input) <= 3) { // editDistance to help find closest match in predifined Responses
            return responses.get(responseKey);
        }
        // If no close match, return a default response
        return "I'm sorry, I didn't understand that.";
    }

    // Compute the Levenshtein distance (edit distance) between two strings
    private static int editDistance(String a, String b) {
    	/* This is from Phase 1, used to help calculate distance between 2 strings
    	 * which will better help with identifying matches for predefined Responses */
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
    	/*  SimpleChatbot instance will be created, and then called to setVisible(true) 
    	 * when User runs the Program . We use invokeLater() from SwingUtilities
    	 *  because it is recommended to run Swing GUI operations on the Event Dispatch Thread.  */ 
        SwingUtilities.invokeLater(() -> {
            SimpleChatbot chatbot = new SimpleChatbot();
            chatbot.setVisible(true);
        });
    }
}
