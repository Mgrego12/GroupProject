import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;
import java.util.HashMap;
/*
 * Group 3 Final Project - StanfordCoreNLPJavaBot
 * ChatBotProcesses Class , to hold the properties of the Chatbot's 
 * Methods that will be called when building the ChatBot GUI
 */

public class Processes {

	private static Map<String, String> responses;
	private static CoreNLP coreNLP;
	private static CoreNLP.NLP_CORE_DEFINITIONS definition;

// Method to populate the responses map

	Processes(){
		// Constructor to initialize the responses map with predefined responses.
				Processes.responses = new HashMap<>();
				populateResponses(null);
		
	}
	public void populateResponses(Object object) {
		/*
		 * method used for for keyword responses,
		 * For chat bot interface to give corresponding response based off 
		 * initial input keywords / phrases
		 */ 
		responses.put("hello", "Hi, how can I help you?");
		responses.put("what is your name?", "I'm a chatbot, I don't have a name.");
		responses.put("how are you?", "I'm a bot, I don't have feelings, but thanks for asking!");
		responses.put("what's the weather?", "I'm sorry, I can't provide real-time information.");
		responses.put("tell me a joke", "Why don't we ever tell secrets on a farm? "
					+ "Because the potatoes have eyes, the corn has ears, and the beans stalk.");
	}
	// Method to process user input and provide appropriate responses
	public static String processInput(String input) {
		/*
		 * processInput to use conditional if/else statements
		 * to help the bot recognize specific input from user
		 * and provide correct Responses  (Time/Date/CoreNLP Annotations,Math)
		 */
		
		// Convert input to lower case once and use it throughout the method
		input = input.toLowerCase();

		// Check for exit commands
		if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("i'm done")) {
			System.exit(0); // Close the application
		}
		// If user asks for current date
		else if (input.contains("today's date") || input.contains("what day is it")) { // use contains() instead of																			// equalsIgnoreCase()
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			LocalDateTime now = LocalDateTime.now();
			return "Today's date is " + dtf.format(now);
		}
		// If user asks for current time
		else if (input.contains("the time") || input.contains("what time is it")) { // use contains() instead of																		// equalsIgnoreCase()
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			return "Current time is " + dtf.format(now);
		}
		else if (input.contains("lemma")) {
			return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition("lemma");
			
		}
		else if (input.contains("word")) {
			return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition("word");
		}
		else if (input.contains("sentiment")) {
			return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition("sentiment");
		}
		else if (input.contains("pos")) {
			return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition("pos");
		}
		else if (input.contains("ner")) {
			return CoreNLP.NLP_CORE_DEFINITIONS.getDefinition("ner");
			
		}
		// Check if the user wants to perform a mathematical operation
		else{
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
			// Find the closest match in predefined responses
			final String finalInput = input; // Add this line to create a final variable
			String responseKey = responses.keySet().stream().min((k1, k2) -> {
				return editDistance(k1, finalInput) - editDistance(k2, finalInput); // Use the final variable here
			}).orElse(null);

			// If the closest match is close enough, return the response
			if (responseKey != null && editDistance(responseKey, input) <= 3) {
				return responses.get(responseKey);
			}
		}
		// If no close match, return a default response
		return "...";
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
}
