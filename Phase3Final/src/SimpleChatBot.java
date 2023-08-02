
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
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
	private JButton closeAnalysisButton; // fix 
	private String processResponses; // fix 
	
	public SimpleChatBot() {
		this.chatBotProcesses = new BotProcesses();

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(0.5);

		setLayout(new BorderLayout());

		chatArea = new JTextArea();
		chatArea.setBackground(Color.darkGray);
		chatArea.setForeground(Color.GREEN);
		chatArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		splitPane.setLeftComponent(chatScrollPane);

		analysisArea = new JTextArea();
		analysisArea.setBackground(Color.GRAY);
		analysisArea.setForeground(Color.BLACK);
		analysisArea.setEditable(false);
		JScrollPane analysisScrollPane = new JScrollPane(analysisArea);
		splitPane.setRightComponent(analysisScrollPane);

		add(splitPane, BorderLayout.CENTER);

		Box box = Box.createHorizontalBox();
		userInput = new JTextField();
		userInput.setBackground(Color.GRAY);
		userInput.setForeground(Color.BLACK);
		userInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});
		box.add(userInput);
		sendButton = new JButton("Send");
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.WHITE);
		box.add(sendButton);

		CoreNLP analyzeBot = new CoreNLP();
		
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = userInput.getText().trim();
				if (!input.isEmpty()) {
					// local variable for time and date
		            LocalTime currentTime = LocalTime.now();
		            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		            String formattedTime = currentTime.format(timeFormatter);

		            // Get the current day and format it to yyyy-MM-dd
		            LocalDate currentDate = LocalDate.now();
		            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            String formattedDate = currentDate.format(dateFormatter);
					
					chatArea.append("User: " + input + "\n");
					String response = chatBotProcesses.processInput(input);
					String processed = chatBotProcesses.populateResponses(input);
					chatArea.append("Bot : " + processed + " \n");
					chatArea.append("Bot: " + response + " \n");
					userInput.setText("");
					
					if (analysisArea.isVisible()) {
						String analysisResult = analyzeBot.analyzeText(input); // Analyze the user's input only
						analysisArea.setText(analysisResult); 
					}
					chatArea.append("Time: " + formattedTime + " | Date: " + formattedDate + "\n");
				}
			}
		});
		analysisArea.setVisible(false);
		startAnalysisButton = new JButton("Show Analysis");
		startAnalysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (analysisArea.isVisible()) {
					analysisArea.setVisible(false);
				} else {
					analysisArea.setVisible(true);
				}
			}
		});
		box.add(startAnalysisButton);
		add(box, BorderLayout.SOUTH);

		setSize(800, 600); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			SimpleChatBot chatbot = new SimpleChatBot();
			chatbot.setVisible(true);
		});
	}
}
