import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

/*
 * Group 3 CoreNLP Java Bot
 * Unit Test to provide methods to help ensure 
 * that this program is running smoothly. 
 * 
 */
public class UnitTest {

	public class CoreNLPTest {
		// Test method for CoreNLP and the analyzeText Method
		// using J Unit Library
		@Test
		public void testAnalyzeText() {
			CoreNLP coreNLP = new CoreNLP();
			String analysis = coreNLP.analyzeText("This is a test sentence.");
			assertNotNull(analysis);
			assertTrue(analysis.contains("Sentence: This is a test sentence."));
		}
	}

	public class ProcessesTest {
		// Test for Processes
		@Test
		public void testProcesses() {
			Object testResponse = Processes.processInput("Hello, this is a Test.");
			assertEquals("Is this a Test?", testResponse);
		}
	}

	public class ChatBotTest {
		private Processes chatBotProcesses;

		@Before
		void setUp() {
			chatBotProcesses = new Processes();
		}

		@Test
		void testProcessInputForGreetings() {
			String response = chatBotProcesses.processInput("hello");
			assertEquals("Hi, how can I help you?", response);

			response = chatBotProcesses.processInput("how are you?");
			assertEquals("I'm a bot, I don't have feelings, but thanks for asking!", response);
		}

		@Test
		void testProcessInputForDateAndTime() {
			String response = chatBotProcesses.processInput("what's the time?");
			assertNotNull(response); // Assuming the response includes time information

			response = chatBotProcesses.processInput("today's date");
			assertNotNull(response); // Assuming the response includes date information
		}

		@Test
		void testProcessInputForMathOperations() {
			String response = chatBotProcesses.processInput("5 + 3");
			assertEquals("The result is 8", response);

			response = chatBotProcesses.processInput("10 * 2");
			assertEquals("The result is 20", response);
		}

		@Test
		void testProcessInputForUnknownInput() {
			String response = chatBotProcesses.processInput("random input");
			assertEquals("...", response); // The default response for unknown input
		}
	}
}
