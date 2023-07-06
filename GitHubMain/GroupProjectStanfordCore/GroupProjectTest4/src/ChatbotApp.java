
import edu.stanford.*;
import edu.stanford.nlp.simple.*;


public class ChatbotApp {
	
    public static void main(String[] args) {
        // Create a sentence
        String sentence = "Hello, how are you doing today?";

        // Analyze the sentence with StanfordCoreNLP
        Sentence doc = new Sentence(sentence);
        
        // Retrieve sentence information
        System.out.println("Text: " + doc.text());
        System.out.println("Tokens: " + doc.words());
        System.out.println("Part-of-speech tags: " + doc.posTags());
        System.out.println("Named entities: " + doc.nerTags());
        System.out.println("Sentiment: " + doc.sentiment());
    }
}