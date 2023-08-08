
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
 * Group 3 ChatBot Java Program - Final Project CMSC495
 * Memebers - Matthew Gregorek, NFamouusa Naite jr. , Bryan Duque
 * CoreNLP Java ChatBot 
 * 
 * Goals
 * 	- Make Chatbot-User interaction more Robust with more capable Answers
 * 	- Make the GUI more Appealing for the User. 
 *  - Clean up CoreNLP Analysis Area, add new JButton "CoreNLP" for User to press
 *  	to display the Real Time Analysis
 *  - Provide More Information about how CoreNLP works and Make the Definitions more accessible
 *  - If time permits, start providing more Secure Variable/Methods/Classes , add exception handling 
 *  	and try/catch methods where viable. 
 *  - Provide more Functionality for the bot to answer (Date/Time/Math) 
 */

public class ChatBot extends JFrame {

	/** SimpleChatbot GUI for Basic ChatBot */
	private static final long serialVersionUID = 1L;
	private JTextField userInput; // text field for input
	private JTextArea chatArea; // text area for bot-user response
	private JButton sendButton; // button to send the user's message
	private JTextArea analysisArea; // text area for CoreNLP analysis
	private CoreNLP analyzeBot; // CoreNLP analyzer for text analysis
	private CoreNLP.NLP_CORE_DEFINITIONS coreDefinitions;
	private final Processes chatBotProcesses; // Add 'final' keyword here
	private JButton startAnalysisButton;
	private JSplitPane splitPane;

	public ChatBot() {
		this.chatBotProcesses = new Processes(); // initialize BotProcesses variable
		analyzeBot = new CoreNLP(); // initialize CoreNLP
		this.coreDefinitions = new CoreNLP.NLP_CORE_DEFINITIONS();

		setLayout(new BorderLayout());

		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setBackground(Color.darkGray);
		chatArea.setForeground(Color.GREEN);
		chatArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatArea);

		analysisArea = new JTextArea();
		analysisArea.setLineWrap(true);
		analysisArea.setWrapStyleWord(true);
		analysisArea.setBackground(Color.GRAY);
		analysisArea.setForeground(Color.BLACK);
		analysisArea.setEditable(false);
		JScrollPane analysisScrollPane = new JScrollPane(analysisArea);
		
		JPanel chatAnalysisContainer = new JPanel(new BorderLayout());
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {
			/**
			 * ensures that the splitPane will be recognized 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void addNotify() {
				super.addNotify();
				SwingUtilities.invokeLater(() -> {
					setDividerLocation(0.5);
				});
			}
		};

		splitPane.setLeftComponent(chatScrollPane);
		splitPane.setRightComponent(analysisScrollPane);
		chatAnalysisContainer.add(splitPane, BorderLayout.CENTER);

		userInput = new JTextField();
		sendButton = new JButton("Send");
		startAnalysisButton = new JButton("Show Analysis");

		Box box = Box.createHorizontalBox();
		userInput.setBackground(Color.GRAY);
		userInput.setForeground(Color.BLACK);
		sendButton.setBackground(Color.BLACK);
		sendButton.setForeground(Color.WHITE);;
		box.add(userInput);
		box.add(sendButton);
		box.add(startAnalysisButton);
		add(chatAnalysisContainer, BorderLayout.CENTER);
		add(box, BorderLayout.SOUTH);



		userInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = userInput.getText().trim();
				if (!input.isEmpty()) {
					chatArea.append("User: " + input + "\n");
					String response = Processes.processInput(input);
					String analyze = analyzeBot.analyzeText(input);
					chatArea.append("Bot : " + response + " \n");
					analysisArea.append(analyze);
					userInput.setText("");
					
				}
			}
		});
		// analysis Area for the GUI , should show CoreNLP annotation
		startAnalysisButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (analysisArea.isVisible()) {
					analysisArea.setVisible(false);
					startAnalysisButton.setText("Show Analysis");
				} else {
					analysisArea.setVisible(true);
					startAnalysisButton.setText("Hide Analysis");
				}
			}
		});

		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			ChatBot chatbot = new ChatBot();

			String message = "Welcome to the ChatBot!\n\n"
					+ "Type your message in the input field and press 'Send' to chat with the bot.\n\n"
					+ "To see CoreNLP analysis, type 'analysis' and click 'Show Analysis'.\n\n"
					+ "To get definitions of the annotators, type 'lemma', 'word', 'sentiment', 'pos', or 'ner'.";

			JOptionPane.showMessageDialog(null, message, "ChatBot Information", JOptionPane.INFORMATION_MESSAGE);
			chatbot.setVisible(true);
		});
	}
}