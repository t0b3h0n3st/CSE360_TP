package applicationMain;

import guiNewAccount.ModelNewAccount;
import guiAdminHome.ModelAdminHome;

public class InputValidationTestbed {
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		   // userNameTestCases();
		   // passwordTestCases();
		   emailTestCases();
		
		
		
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
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		
		/* -----
		 * [TP1]: HARRISON: switch statement for validation type
		   -----*/
		String lowercaseType = inputType.toLowerCase();
		String resultText;
		
		switch (lowercaseType) {
		case "username":
			resultText = ModelNewAccount.checkForValidUserName(inputText);
			break;
		case "password":
			resultText = ModelNewAccount.evaluatePassword(inputText);
			break;
		case "email":
			resultText = ModelAdminHome.checkEmailAddress(inputText);
			break;
		default:
			System.out.println("Unknown validation type passed in.");
			return;
		}
		
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
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
//		displayEvaluation(lowercaseType);
	}
	
	/* 
	 * 
	 */
	private static void userNameTestCases() {
		/* -------------
		 * USERNAMES:
		   ------------- */
		
		// POSITIVE TESTS:
		System.out.println("____________________________________________________________________________\n\nPerforming test case for Usernames");
		
		performTestCase(1, "redfish1", "username", true); 
		performTestCase(2, "B1-nk", "username", true); // testing '-'
		performTestCase(3, "d&4sk", "username", true); // testing '&'
		performTestCase(4, "R_d.y9", "username", true); // testing '-', '.'
		performTestCase(5, "Exactly32_CharactersInLengthTest", "username", true); // exact max length
		performTestCase(6, "four", "username", true); // exact min length
		
		// NEGATIVE TESTS:
		performTestCase(7, "1asdf", "username", false); // cannot start with number
		performTestCase(8, "o&&n", "username", false); // each & must be in between two number/letters
		performTestCase(9, "a", "username", false); // < min length
		performTestCase(10, "kab@ng", "username", false); // cannot have random character like @
		performTestCase(11, "dry1ce&", "username", false); // & must be in between two number/letters
		performTestCase(12, "MoreThan32_CharactersInLengthTest", "username", false); // > max length
	}
	
	private static void passwordTestCases() {
		/* -------------
		 * PASSWORDS:
		   ------------- */
		System.out.println("____________________________________________________________________________\n\nPerforming test case for Passwords");
		
		// POSITIVE TESTS:

		performTestCase(1, "Test123!", "password", true); 
		performTestCase(2, "Test123#~", "password", true); // testing multiple special characters
		performTestCase(3, "Exctly8!", "password", true); // exact min length
		performTestCase(4, "Exactly32!CharactersInLengthTest", "password", true); // exact max length
		
		// NEGATIVE TESTS:
		performTestCase(5, "all_lowercase", "password", false); 
		performTestCase(6, "ALL_UPPERCASE", "password", false);
		performTestCase(7, "noSpecialChar", "password", false);
		performTestCase(8, "", "password", false); // empty string
		performTestCase(9, "space 1nBetween!", "password", false);
		performTestCase(10, "1Ivan", "password", false); // no special char, less than min length
		performTestCase(11, "Bel0w8!", "password", false); // < min length
		performTestCase(12, "moreThan_ThirtyTwo_CharactersTest!", "password", false); // > max length
	}
	
	private static void emailTestCases() {
		/* -------------
		 * EMAILS:
		   ------------- */
		
		// POSITIVE TESTS:
		performTestCase(1, "test@asu.edu", "email", true);  // standard email domains
		performTestCase(2, "test@gmail.com", "email", true);
		performTestCase(3, "test@outlook.com", "email", true);
		performTestCase(4, "t3st@gmail.com", "email", true); // email can contain both numerics + letters
		performTestCase(5, "t345@gmail.com", "email", true); // email can contain both numerics + letters
		performTestCase(6, "305@gmail.com", "email", true); // local part can just contain only numerics
		
		// NEGATIVE TESTS:
		performTestCase(7, "@asu.edu", "email", false); // no local part
		performTestCase(8, "test@", "email", false); // no domain
		performTestCase(14, "testgmail.com", "email", false); // no @
		performTestCase(9, "5", "email", false); // no @ and domain
		performTestCase(10, "a", "email", false); // no @ and domain
		performTestCase(11, "", "email", false); // empty string
		performTestCase(12, "test@random.com", "email", false); // correct format, but domain is not accepted.
		performTestCase(13, "test@@gmail.com", "email", false); // only one @ allowed.
		
		
	}
}
	
