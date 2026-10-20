package guiUserUpdate;

import java.util.Arrays;
import java.util.List;

public class Model {
	//Everything here was added and modified from the EmailAddressValidator
		//Email Validator
		public static String emailAddressErrorMessage = "";	// The error message text
		public static String emailAddressInput = "";		// The input being processed
		public static int emailAddressIndexofError = -1;	// The index where the error was located
		private static int email_state = 0;						// The current state value
		private static int email_nextState = 0;					// The next state value
		private static boolean email_finalState = false;			// Is this state a final state?
		private static String email_inputLine = "";				// The input line
		private static char email_currentChar;					// The current character in the line
		private static int email_currentCharNdx;					// The index of the current character
		private static boolean email_running;						// The flag that specifies if the FSM is 
															// running
		private static int domainPartCounter = 0;			// A domain name may not exceed 63 characters
		
		//NEW
		private static List<String> validDomains = Arrays.asList("gmail.com", "asu.edu", "outlook.com");  // Valid List of Domains
		//NEW
		
		private static String displayInput(String input, int currentCharNdx) {
			// Display the entire input line
			String result = input.substring(0,currentCharNdx) + "?\n";

			return result;
		}
		
		private static void displayDebuggingInfo() {
			// Display the current state of the FSM as part of an execution trace
			if (email_currentCharNdx >= email_inputLine.length())
				// display the line with the current state numbers aligned
				System.out.println(((email_state > 99) ? " " : (email_state > 9) ? "  " : "   ") + email_state + 
						((email_finalState) ? "       F   " : "           ") + "None");
			else
				System.out.println(((email_state > 99) ? " " : (email_state > 9) ? "  " : "   ") + email_state + 
						((email_finalState) ? "       F   " : "           ") + "  " + email_currentChar + " " + 
						((email_nextState > 99) ? "" : (email_nextState > 9) || (email_nextState == -1) ? "   " : "    ") + 
						email_nextState + "     " + domainPartCounter);
		}
		
		private static void moveToNextCharacter() {
			email_currentCharNdx++;
			if (email_currentCharNdx < email_inputLine.length())
				email_currentChar = email_inputLine.charAt(email_currentCharNdx);
			else {
				System.out.println("End of input was found!");
				email_currentChar = ' ';
				email_running = false;
			}
		}
		
		
		public static String checkEmailAddress(String input) {
			// The following are the local variable used to perform the Finite State Machine simulation
			email_state = 0;							// This is the FSM state number
			email_inputLine = input;					// Save the reference to the input line as a global
			email_currentCharNdx = 0;					// The index of the current character

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state

			emailAddressInput = input;			// Save a copy of the input

			// Let's ensure there is input
			if (input.length() <= 0) {
				emailAddressErrorMessage = "There was no email address found.\n";
				return emailAddressErrorMessage + displayInput(input, 0);
			}
			email_currentChar = input.charAt(0);		// The current character from the above indexed position

			// Let's ensure the address is not too long
			if (input.length() > 255) {
				emailAddressErrorMessage = "A valid email address must be no more than 255 characters.\n";
				return emailAddressErrorMessage + displayInput(input, 255);
			}
			
			//TP1
			if (!input.contains("@")) {
				emailAddressErrorMessage = "A valid email address must contain a @" ;
				return emailAddressErrorMessage;
			}
			
			email_running = true;						// Start the loop
			System.out.println("\nCurrent Final Input  Next  DomainName\nState   State Char  State  Size");

			// The Finite State Machines continues until the end of the input is reached or at some 
			// state the current character does not match any valid transition to a next state
			while (email_running) {
				// The switch statement takes the execution to the code for the current state, where
				// that code sees whether or not the current character is valid to transition to a
				// next state
				email_nextState = -1;						// Default to there is no next state		
				
				switch (email_state) {
				case 0: 
					// State 0 has just 1 valid transition.
					// The current character is must be checked against 62 options. If any are matched
					// the FSM must go to state 1
					// The first and the second check for an alphabet character the third a numeric
					if ((email_currentChar >= 'A' && email_currentChar <= 'Z')|| 		// Upper case
							(email_currentChar >= 'a' && email_currentChar <= 'z') ||	// Lower case
							(email_currentChar >= '0' && email_currentChar <= '9')) {	// Digit
						email_nextState = 1;
					}
									
					// If it is none of those characters, the FSM halts
					else { 
						email_running = false;
					}
					
					break;				
					// The execution of this state is finished
				
				case 1: 
					// State 1 has three valid transitions. 
					// Replace this with the required code
					if ((email_currentChar >= 'A' && email_currentChar <= 'Z')|| 		// Upper case
							(email_currentChar >= 'a' && email_currentChar <= 'z') ||	// Lower case
							(email_currentChar >= '0' && email_currentChar <= '9')) {	// Digit
						email_nextState = 1;
					} 
					else if (email_currentChar == '@') {
						email_nextState = 2;
					}
					
					else if (email_currentChar == '.') {
						email_nextState = 0;
					}
					
					else {
						email_running = false;
					}

					break;
					// The execution of this state is finished
								
				case 2: 
					// State 2 has one valid transition.
					
					// Replace this with the required code

					if ((email_currentChar >= 'A' && email_currentChar <= 'Z')|| 		// Upper case
							(email_currentChar >= 'a' && email_currentChar <= 'z')) {	// Lower case

						email_nextState = 3;
					} 

					else {
						email_running = false;
					}

					// The execution of this state is finished
					break;
		
				case 3:
					// State 3 has three valid transition.
					
					// Replace this with the required code

					if ((email_currentChar >= 'A' && email_currentChar <= 'Z')|| 		// Upper case
							(email_currentChar >= 'a' && email_currentChar <= 'z') ||	// Lower case
							(email_currentChar >= '0' && email_currentChar <= '9')) {	// Digit
						email_nextState = 3;
					} 
					else if (email_currentChar == '.') {
						email_nextState = 2;
					}
					
					else {
						email_running = false;
					}
					// The execution of this state is finished
					break;

				

				}
				
				if (email_running) {
					displayDebuggingInfo();
					// When the processing of a state has finished, the FSM proceeds to the next character
					// in the input and if there is one, it fetches that character and updates the 
					// currentChar.  If there is no next character the currentChar is set to a blank.
					
					moveToNextCharacter();
					
					// Move to the next state
					email_state = email_nextState;
					email_nextState = -1;
				}
				// Should the FSM get here, the loop starts again

			}
			displayDebuggingInfo();
			
			System.out.println("The loop has ended.");

			emailAddressIndexofError = email_currentCharNdx;		// Copy the index of the current character;
			
			// When the FSM halts, we must determine if the situation is an error or not.  That depends
			// of the current state of the FSM and whether or not the whole string has been consumed.
			// This switch directs the execution to separate code for each of the FSM states and that
			// makes it possible for this code to display a very specific error message to improve the
			// user experience.
			switch (email_state) {
			case 0:
				// State 0 is not a final state, so we can return a very specific error message
				emailAddressIndexofError = email_currentCharNdx;		// Copy the index of the current character;
				emailAddressErrorMessage = "May only be alphanumberic before the @.\n";
				return emailAddressErrorMessage;

			case 1:
				// State 1 is not a final state, so we can return a very specific error message
				
				// Replace this with the required code
				emailAddressErrorMessage = "May only be alphanumeric or @. \n";
				return emailAddressErrorMessage;
			case 2:
				// State 2 is not a final state, so we can return a very specific error message
							
				// Replace this with the required code

				emailAddressErrorMessage = "After an @, the domain may only be alphanumeric and has to be a valid dommain. \n";
				return emailAddressErrorMessage + "Ex. of Valid Domains: gmail.com, asu.edu, outlook.com";
			case 3:
				// State 3 is a Final State, so this is not an error if the input is empty, otherwise
				// we can return a very specific error message.
				
				
				
				//TP1: NEW
				   String emailDomain = input.substring(input.indexOf("@") +1);
				   Boolean domainIsValid = false;
				   
				   for(int i=0; i < validDomains.size(); i++) {
					   if (emailDomain.equals(validDomains.get(i))) {
						   domainIsValid = true;
					   }
				   }
				
				   if (!domainIsValid) {
					   int domainStart = input.indexOf("@") + 1;
					   emailAddressIndexofError = domainStart;		// Copy the index of the current character;
						emailAddressErrorMessage = "Email is invalid, invalid Domain: ";
						return emailAddressErrorMessage + emailDomain + "\n Ex. of Valid Domains: gmail.com, asu.edu";
				   }
				   
				   //End of NEW
				   
				if (email_currentCharNdx<input.length()) {
					// If not all of the string has been consumed, we point to the current character
					// in the input line and specify what that character must be in order to move
					// forward.
					emailAddressIndexofError = email_currentCharNdx;		// Copy the index of the current character;
					emailAddressErrorMessage = "This must be the end of the input.\n";
					return emailAddressErrorMessage + displayInput(input, email_currentCharNdx);
				}
				else 
				{
					emailAddressIndexofError = -1;
					emailAddressErrorMessage = "";
					return emailAddressErrorMessage;
				}

			

			default:
				return "";
			}
		}

}