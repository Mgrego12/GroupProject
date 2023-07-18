
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// The chatbot application main class extending from JFrame for GUI
public class ChatbotGUI extends JFrame {

    private JTextField userInput; // text field for user input
    private JTextArea chatArea; // text area for the conversation
    private JButton sendButton; // button to send the message
    private Map<String, String> responses; // map to store predefined responses

    // Constructor method to setup the GUI and initialize responses
    public ChatbotGUI() {
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
  public static void main(String[] args) {
        // Create and display the chatbot
        new ChatbotGUI().setVisible(true);
    }
}
