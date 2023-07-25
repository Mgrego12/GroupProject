
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
/*
 * Group 3 ChatBot Java Program - Phase II CMSC495
 * Memebers - Matthew Gregorek, NFamouusa Naite jr. , Bryan Duque
 * CoreNLP java class
 */
public class CoreNLP {
	/* CoreNLP class for initializing Stanford CoreNLP Library 
	 *  Using Pipeline to set up Annotators that will analyze User Input.
	 *  We have also included an AnalyzeText method and NLP_CORE_DEFINITIONS Method
	 *  to help the User see how input is being analyzed , and given the ability to learn more 
	 *  about each annotation. 
	 * */
	
    private StanfordCoreNLP pipeline; // create pipeline variable
    

    public CoreNLP() {
    	 /**
         * Constructor to initialize Stanford CoreNLP pipeline
         *  with required annotators. 
         */
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String analyzeText(String text) {
        /**
         *  analyzeText Method will analyze the given text using Stanford CoreNLP 
         *  and return the analysis as a string.
         *
         * @param text The input text to analyze.
         * @return A string containing the analysis results.
         */
	    Annotation document = new Annotation(text);// Create a document object and annotate it
	    pipeline.annotate(document); // run it through pipeline 

	    StringBuilder output = new StringBuilder(); // Java StringBuilder object to output text analysis
	    StringBuilder namedEntities = new StringBuilder(); // specific NamedEntities object for (ner) detection

	    // Iterate over all of the sentences found
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class); // create sentences variable to store data from document
	    for (CoreMap sentence : sentences) { // use for-loop to iterate through each sentence 
	        output.append("Sentence: ").append(sentence.toString()).append("\n"); // add each sentence to output (StringBuilder) 
	        
	        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
	        output.append("Sentiment: ").append(sentiment).append("\n");

	        // Iterate over all tokens in a sentence
	        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
	            // create annotation variables to "get" correct token
	            String word = token.get(TextAnnotation.class);
	            String lemma = token.get(LemmaAnnotation.class);
	            String pos = token.get(PartOfSpeechAnnotation.class);
	            String ner = token.get(NamedEntityTagAnnotation.class);
	            String sent = token.get(SentimentCoreAnnotations.SentimentClass.class);
	            // when the correct Annotation Token is Found, it will be added to ouput with a new line
	            output.append("Word: ").append(word).append("\n");
	            output.append("Lemma: ").append(lemma).append("\n");
	            output.append("POS: ").append(pos).append("\n");
	            output.append("NER: ").append(ner).append("\n"); 
	            output.append("SENTIMENT").append(sent).append("\n");
	            
	            // If a named entity is recognized, add it to the namedEntities StringBuilder
	            if (!"O".equals(ner)) {
	                namedEntities.append(word).append(" (").append(ner).append("), ");
	            }
	        }
	    }
	    if (namedEntities.length() > 0) {
	        // Remove the last comma and space, and add a period
	        namedEntities.setLength(namedEntities.length() - 2);
	        namedEntities.append(".");
	        output.append("\nNamed entities recognized: ").append(namedEntities.toString()).append("\n");
	    }
	    return output.toString();
	}
    public static class NLP_CORE_DEFINITIONS {
        /**
         * Method to get the definition for the specified key.
         *
         * @param key The key for which the definition is requested.
         * @return The definition corresponding to the given key, or a default message if not found.
         */

        static HashMap<String, String> nlpDefinitions;// nlpDefinitions variable HashMap
        // when the key value is recognized, the corresponding definition will be Displayed to User. 
        static {
            nlpDefinitions = new HashMap<>();
            nlpDefinitions.put("lemma", "Lemma is the base form of a word. For example, the lemma of 'was' is 'be'.");
            nlpDefinitions.put("pos", "POS stands for Part of Speech. This indicates whether a word is a noun, verb, adjective, etc.");
            nlpDefinitions.put("ner", "NER stands for Named Entity Recognition. This helps to classify named entities in text into predefined "
            		+ "categories such as names of persons, organizations, locations, etc.");
            nlpDefinitions.put("word", "Word refers to the individual words in the text.");
            nlpDefinitions.put("sentiment", "Sentiment analysis is the interpretation and classification of emotions within text data using text analysis techniques.");
            nlpDefinitions.put("sent", "Sentiment analysis is the interpretation and classification of emotions within text data using text analysis techniques.");
            // Add more definitions as needed
        }
        // method to retrieve definition from nlpDefinitions HashMap, takes in Key, returns output, or throws Error. 
        public static String getDefinition(String key) {
            if (nlpDefinitions != null) {
                return nlpDefinitions.getOrDefault(key.toLowerCase(), "No definition found, Please Try Again.");
            } else {
                throw new RuntimeException("NLP definitions are not initialized.");
            }
        }
    }
}
