import edu.stanford.nlp.simple.*;
/* Matt Gregorek CMSC*/
import java.util.Scanner;
import java.util.Date;
import java.util.List;

public class ChatbotApp {
	
	private static String userName; // var for Users' Name.
	
    public static void main(String[] args) {
        // Create a scanner to read user input
        Scanner scanner = new Scanner(System.in);
        // Greet the user
        System.out.println("Hello, my name is ChadBot, what's your Name?");
        userName = scanner.nextLine();
        
        System.out.println("Hello! " + userName + ". How can I assist you today?");
        // Start the conversation loop
        while (true) {
            // Read user input
            String userInput = scanner.nextLine();
            // Process user input and generate a response
            String response = generateResponse(userInput);
            // Display the response
            System.out.println(response);
            // Exit the loop if the user says "bye"
            if (userInput.equalsIgnoreCase("bye")) {
                break;
            }
        }
        // Close the scanner
        scanner.close();
    }
    private static String generateResponse(String input) {
        // Create a Sentence object for processing user input
        Sentence sentence = new Sentence(input);
        
        // Get the tokens from the user input
        List<String> tokens = sentence.words();
        if (input.contains("hello")) {
            return "Hi there! " + userName;
        } else if (input.contains("how are you")) {
            return "I'm doing well, thank you!";
        } else if (input.contains("today")) {
            // Example of using CoreNLP for date-related information
            String date = new java.util.Date().toString();
            return "Today is " + date;
        } else if (input.contains("states") && input.contains("United States")) {
            // Example of using CoreNLP for entity recognition
            return "There are 50 states in the United States.";
        } else if(input.contains("goodbye") || input.contains("exit")){
        	return "Thanks for using me, Goodbye! " + userName;
        } else {
            return "I'm sorry, I didn't understand. Can you please rephrase your question?";
        }
    }
}