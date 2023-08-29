This is Group 3 Final Version of the Stanford Core NLP Java ChatBot Program
August 8, 2023
UMGC CMSC495-6981
Group 3 Members:
	Matt Gregorek
	Nfamoussa Naite jr. 
	Bryan Duque

This project is Used and was Tested with Windows 10 , Eclipse IDE , Java JDK 17
StanfordCoreNLP external jar libraries


Links
https://stanfordnlp.github.io/CoreNLP/
https://www.eclipse.org/
https://www.oracle.com/java/technologies/downloads/

refer to slideshow for Project Setup if having diffuclties adding CoreNLP .jar files to your Project FilePath
  Remember to add the English .jar file as well. 

CoreNLP
  - Focuses on creating CoreNLP pipeline annotators to analyze user input and analyze each Token to determine what type of pipeline property that Token (word) is. 
  - analyzeText is the method that uses StringBuilder to append the annotator outputs, as well as NamedEntities (ner) outputs. 
  - we have also added a sentiment evaluator using CoreNLP sentiment annotation class to determine user input sentiment. 
  NLP_CORE_DEFINITIONS
    - these hold key-value definitions so that when the User types in "pos", "ner", "sentiment"... etc, the values will be returned to them. 
    - This way the user can better understand what each annotator is doing. 
    
Processes 
 - This file holds the major processes for analyzing, searching, and providing apporpriate responses to User Input. 
 - the populateResponse method holds objects that also have a key-value system, when certain "keywords" are found, like
	"hello", the chatbot will use the value for the appropriate response. 
 - processInput is similiar but we use conditional if, else-if statements to search for specific String inputs, the user can quit, 
	ask for the day's date, time, and all the annotators get their responses from the NLP_CORE_DEFINITION class . 
	There is also a simple math function for adding, subtracting, multiplying , and division. 
- the editDistance method is used to help with typos and mistakes form user input, which makes the bot response system more
	durable. 

ChatBot
 - This is where the GUI functionality was programmed, we used JFrame to hold a chatBot area, and an Analysis area, at the bottom 
	there is a textField for User Input and send/analyze Buttons. The GUI is split by using splitPane function , the user has the option
	to close Analysis area if they wish.
 - the send actionListener is where we collect user input and append it to the chatArea, we also use processInput and analyzeText from previous classes
	to analyze each line of input, and give the proper response from the Bot. The Analysis is on the Right-Hand side which will show
	annotations and sentiment, and Named Entity Recognitions each time they appear. 
  MAIN 
	the main method uses invokeLater to make sure that all other GUI functions are called first , and a messageDialog with instructions to the User on 
	how the chatbot Works. 

