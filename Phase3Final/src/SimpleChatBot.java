
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/*
 * Group 3 ChatBot Java Program - Phase III CMSC495
 * Memebers - Matthew Gregorek, NFamouusa Naite jr. , Bryan Duque
 * CoreNLP Java SimpleChatBot app 
 * 
 * Phase 3 Goals
 * 	- Make Chatbot-User interaction more Robust with more capable Answers
 * 	- Make the GUI more Appealing for the User. 
 *  - Clean up CoreNLP Analysis Area, add new JButton "CoreNLP" for User to press
 *  	to display the Real Time Analysis
 *  - Provide More Information about how CoreNLP works and Make the Definitions more accessible
 *  - If time permits, start providing more Secure Variable/Methods/Classes , add exception handling 
 *  	and try/catch methods where viable. 
 */

public class SimpleChatBot extends JFrame {

	/** SimpleChatbot GUI for Basic ChatBot */
	private static final long serialVersionUID = 1L;
	private JTextField userInput; // text field for input
	private JTextArea chatArea; // text area for bot-user response
	private JButton sendButton; // button to send the user's message
	private Map<String, String> responses; // map to store predefined responses
	private JTextArea analysisArea; // text area for CoreNLP analysis
	private CoreNLP analyzeBot; // CoreNLP analyzer for text analysis
	private final BotProcesses chatBotProcesses; // Add 'final' keyword here
	private JButton startAnalysisButton;
	private JButton closeAnalysisButton;
	
	public SimpleChatBot() {
		/*
		 * SimpleChatbot constructor method. Used for creating the GUI for "ChatbotApp"
		 * initliazes components : textfields, button, areas, event listeners It will
		 * have 2 separate areas , a chatArea and Analysis area. The sendButton uses
		 * ActionListener to get User Input and add it to appropiate chat Area.
		 */
	    super("ChatbotApp"); // super JFrame title "ChatbotApp"

        // Initialize the responses map and populate it
        responses = new HashMap<>();
        chatBotProcesses = new BotProcesses();
        chatBotProcesses.populateResponses(responses);

        setLayout(new BorderLayout()); // Layout of JFrame to BorderLayout

        // Setup the chat area
        chatArea = new JTextArea();
        chatArea.setBackground(Color.BLACK);
        chatArea.setForeground(Color.BLUE);
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Setup the input area
        Box box = Box.createHorizontalBox();
        userInput = new JTextField();
        userInput.setBackground(Color.WHITE);
        userInput.setForeground(Color.BLACK);
        userInput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick(); // Call the sendButton's click action
            }
        });
        box.add(userInput);
        sendButton = new JButton("Send");
        sendButton.setBackground(Color.BLACK);
        sendButton.setForeground(Color.WHITE);
        box.add(sendButton);
        add(box, BorderLayout.SOUTH);

        // Add the action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the input from the user
                String input = userInput.getText().trim();
                if (!input.isEmpty()) {
                    // Append the user's message to the chat
                    chatArea.append("User: " + input + "\n");
                    // Get the bot's response and append it to the chat
                    String response = chatBotProcesses.processInput(input.toLowerCase());

                    // Append the current time
                    LocalTime timeNow = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    chatArea.append("Bot (" + timeNow.format(formatter) + "): " + response + "\n");
                    // Clear the input field for the next message
                    userInput.setText("");
                }
            }
        });

        // Add the action listener to the "Show Analysis" button
        startAnalysisButton = new JButton("Show Analysis");
        startAnalysisButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = userInput.getText().trim();
                if (!input.isEmpty()) {
                    if (!input.isEmpty()) {
                        String analysisResult = analyzeBot.analyzeText(input);

                        // Update the analysis area with the analysis result
                        analysisArea.setText(analysisResult);
                        }
                    }
                }
            });

        // Add the action listener to the "Close Analysis" button
        closeAnalysisButton = new JButton("Close Analysis");
        closeAnalysisButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Find the JFrame with the title "CoreNLP Analysis" and close it
                for (Window window : JFrame.getWindows()) { // Change 'Window' to 'JFrame' here
                    if (window instanceof JFrame && window.getName().equals("CoreNLP Analysis")) {
                        window.dispose();
                        break;
                    }
                }
            }
        });

        // Add the buttons to the box and the box to the frame
        box.add(startAnalysisButton);
        box.add(closeAnalysisButton);

        setSize(800, 600); // 800x600 size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // close on exit
    }

	// The application entry point
	public static void main(String[] args) {
		/*
		 * SimpleChatbot instance will be created, and then called to setVisible(true)
		 * when User runs the Program . We use invokeLater() from SwingUtilities because
		 * it is recommended to run Swing GUI operations on the Event Dispatch Thread.
		 */
		SwingUtilities.invokeLater(() -> {
			SimpleChatBot chatbot = new SimpleChatBot();
			chatbot.setVisible(true);
		});
	}
}