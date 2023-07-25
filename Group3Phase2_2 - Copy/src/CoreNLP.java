
/*  Matthew Gregorek CMSC495 Group 3 Project
 *  Phase 2 code
 *  Use User input from SimpleChatbot and NLP libraries 
 *  to perform Analysis
 */
import edu.stanford.nlp.*;
import java.util.*;

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


public class CoreNLP {
	// call Core NLP's init Pipeline
	/*
	 * Phase II - add CoreNLP properties to GUI SimpleChatbot object to Analyze user
	 * input text , run through Pipeline, and analyze Text.
	 * 
	 */
	public CoreNLP analyzeBot;
	public StanfordCoreNLP pipeline;

	public CoreNLP() {
		// Initialize Stanford CoreNLP with an annotator for
		// tokenization, ssplit, pos, lemma, ner and parse

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,sentiment");
		this.pipeline = new StanfordCoreNLP(props);
	}

	public String analyzeText(String text) {
	    // Create a document object and annotate it
	    Annotation document = new Annotation(text);
	    pipeline.annotate(document);

	    StringBuilder output = new StringBuilder();
	    StringBuilder namedEntities = new StringBuilder();

	    // Iterate over all of the sentences found
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for (CoreMap sentence : sentences) {
	        output.append("Sentence: ").append(sentence.toString()).append("\n");
	        
	        String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
	        output.append("Sentiment: ").append(sentiment).append("\n");

	        // Iterate over all tokens in a sentence
	        for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
	            // Retrieve and add the lemma for each word into the output
	            String word = token.get(TextAnnotation.class);
	            String lemma = token.get(LemmaAnnotation.class);
	            String pos = token.get(PartOfSpeechAnnotation.class);
	            String ner = token.get(NamedEntityTagAnnotation.class);
	            String sent = token.get(SentimentCoreAnnotations.SentimentClass.class);
	            
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

	    static HashMap<String, String> nlpDefinitions;
	    // set up definitions of CoreNLP's properties for User
	    static {
	        nlpDefinitions = new HashMap<>();
	        nlpDefinitions.put("lemma", "Lemma is the base form of a word. For example, the lemma of 'was' is 'be'.");
	        nlpDefinitions.put("pos", "POS stands for Part of Speech. This indicates whether a word is a noun, verb, adjective, etc.");
	        nlpDefinitions.put("ner", "NER stands for Named Entity Recognition. This helps to classify named entities in text into predefined categories such as names of persons, organizations, locations, etc.");
	        nlpDefinitions.put("word", "Word refers to the individual words in the text.");
	        nlpDefinitions.put("sentiment", "Sentiment analysis is the interpretation and classification of emotions within text data using text analysis techniques.");
	        nlpDefinitions.put("sent", "Sentiment analysis is the interpretation and classification of emotions within text data using text analysis techniques.");
	        // add more definitions as needed
	    }

	    public static String getDefinition(String key) {
	        if (nlpDefinitions != null) {
	            return nlpDefinitions.getOrDefault(key.toLowerCase(), "No definition found, Please Try Again.");
	        } else {
	            throw new RuntimeException("NLP definitions are not initialized.");
	        }
	    }
	}
} 


// Still need to add Sentiment Analysis, and Dependency parser . The Pipeline is able 
  // To take in the user input , and parse through the words to create "properties" that
  // we use with Analyze Text method, By creating new Instance of CoreNLP to the 
 // SimpleChatbot class, we are then able to display what these "properties" analyze , 
// and the NER can recognize Countries, Cities, Celebrities... we should do more testing/
// Documentation to show for Phase II . 
