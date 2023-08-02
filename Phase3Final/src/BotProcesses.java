import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;
/*
 * Group 3 Phase 3
 * ChatBotProcesses Class , to hold the properties of the Chatbot's 
 * Methods that will be called when building the ChatBot GUI
 */


public class BotProcesses{
	
    private Map<String, String> responses;
    private CoreNLP coreNLP;
    

// Method to populate the responses map
    
    public BotProcesses() {
        // Constructor to initialize the responses map with predefined responses.
        // Constructor to initialize the responses map with predefined responses.
        this.responses = new HashMap<>();
        populateResponses(null);
    }
 
	public void populateResponses(Map<String, String> responses2) {
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
	public String processInput(String input) {
	    // Convert input to lowercase once and use it throughout the method
	    input = input.toLowerCase();

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
	    return "I'm sorry, I didn't understand that.";
	}

    // Compute the Levenshtein distance (edit distance) between two strings
    public int editDistance(String a, String b) {
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
}