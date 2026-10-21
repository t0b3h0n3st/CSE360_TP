package applicationMain;
import guiCreateLesson.ModelCreateLesson;

/**
 * <p> Title: LessonsLearnedValidation </p>
 *
 * <p> Description: A console-based, semi-automated test driver for the lesson title and text
 * validators in {@link guiCreateLesson.ModelCreateLesson}. Each test case supplies an input,
 * the type of input ({@code "title"} or {@code "text"}), and whether the input is expected to
 * pass. The driver prints the result of every case followed by a pass/fail summary.</p>
 *
 * @author Harrison
 * @version 2.0
 */

public class LessonsLearnedValidation {

		static int numPassed = 0;	// Counter of the number of passed tests
		static int numFailed = 0;	// Counter of the number of failed tests
		
		static String title = "title";
		static String text = "text";

		/*
		 * This mainline displays a header to the console, performs a sequence of
		 * test cases, and then displays a footer with a summary of the results
		 */
		
		public static void main(String[] args) {
			/************** Test cases semi-automation report header **************/
			System.out.println("______________________________________");
			System.out.println("\nTesting Automation");

			/************** Start of the test cases **************/
			
			// WHITESPACE:
			performTestCase(1, "", title, false);
			performTestCase(2, "", text, false);
			performTestCase(3, "   ", text, false);
			performTestCase(4, "   ", title, false);
			
			// MIN NOT REACHED:
			performTestCase(5, "asdfasf", text, false);
			
			// MAX EXCEEDED:
			performTestCase(6, "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
					+ "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", title, false);
			performTestCase(7, "lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum\"\r\n"
					+ "					+ \"lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", text, false);
			
			
			// TRUE CASES:
			
			performTestCase(8, "Issue on Line 59", title, true);
			performTestCase(9, "There was an issue at this point in the code\n I'm not sure what it was though but just be wary", text, true);
			performTestCase(10, "Hi", title, true);
			performTestCase(11, "This is a normal description.", text, true);
			
			/************** End of the test cases **************/
			
			/************** Test cases semi-automation report footer **************/
			System.out.println("____________________________________________________________________________");
			System.out.println();
			System.out.println("Number of tests passed: "+ numPassed);
			System.out.println("Number of tests failed: "+ numFailed);
		}
		
		/*
		 * This method sets up the input value for the test from the input parameters,
		 * displays test execution information, invokes precisely the same recognizer
		 * that the interactive JavaFX mainline uses, interprets the returned value,
		 * and displays the interpreted result.
		 */
		private static void performTestCase(int testCase, String inputText, String inputType, boolean expectedPass) {
					
			/************** Display an individual test case header **************/
			System.out.println("____________________________________________________________________________\n\nPerforming test case for input type : " + inputType);
			System.out.println("\nTest case: " + testCase);
			System.out.println("Input: \"" + inputText + "\"");
			System.out.println("______________");
			
			/************** Call the recognizer to process the input **************/
			

			String lowercaseType = inputType.toLowerCase();
			String resultText;
			
			switch (lowercaseType) {
			case "title":
				resultText = ModelCreateLesson.validateTitle(inputText);
				break;
			case "text":
				resultText = ModelCreateLesson.validateText(inputText);
				break;
			default:
				System.out.println("Unknown validation type passed in.");
				return;
			}
			
			
			/************** Interpret the result and display that interpreted information **************/
			System.out.println();
			
			// if it is not empty, it rejected input:	
			if (!resultText.isEmpty()) {
				 // If the test case expected the test to pass then this is a failure
				if (expectedPass) {
					System.out.println("***Failure*** The input <" + inputText + "> is invalid." + 
							"\nBut it was supposed to be valid, so this is a failure!\n");
					System.out.println("Error message: " + resultText);
					numFailed++;
				}
				// If the test case expected the test to fail then this is a success
				else {			
					System.out.println("***Success*** The input <" + inputText + "> is invalid." + 
							"\nBut it was supposed to be invalid, so this is a pass!\n");
					System.out.println("Error message: " + resultText);
					numPassed++;
				}
			}
			
			// If the resulting text is empty, the recognizer accepted the input
			else {	
				// If the test case expected the test to pass then this is a success
				if (expectedPass) {	
					System.out.println("***Success*** The password <" + inputText + 
							"> is valid, so this is a pass!");
					numPassed++;
				}
				// If the test case expected the test to fail then this is a failure
				else {
					System.out.println("***Failure*** The password <" + inputText + 
							"> was judged as valid" + 
							"\nBut it was supposed to be invalid, so this is a failure!");
					numFailed++;
				}
			}
//			displayEvaluation(lowercaseType);
		}
		
		/* 
		 * 
		 */

}
		
