package guiCreateLesson;

/**
 * <p> Title: ModelCreateLesson </p>
 *
 * <p> Description: The model for the Create Lesson page. It holds the input validation rules
 * for the title and text of a lesson. Each validator returns an error message, or an empty
 * string when the input is acceptable, so callers can test {@code isEmpty()} to decide
 * whether the input passed.</p>
 * 
 * @version 2.0
 * @see ControllerCreateLesson
 * @see ViewCreateLesson
 */

public class ModelCreateLesson {
	
	/**
	 * Validates the title of a lesson. A title is valid when it is not null or blank, is at
	 * most 100 characters long, and fits on a single line (contains no newline).
	 *
	 * @param input the title entered by the user
	 * @return an empty string if the title is valid; otherwise a message describing the
	 *         first rule that was violated
	 */
	public static String validateTitle(String input) {
		final int MAX_LENGTH = 100;
		String msg = "";
		
		// EMPTY INPUT:
		if (input == null || input.isBlank()) {
			msg = "Title cannot be blank. Please enter in a title.";
		}
		
		// LENGTH TOO LONG:
		else if (input.length() > MAX_LENGTH) {
			msg = String.format("Title is too long at %d characters. The max length is %d characters.", input.length(), MAX_LENGTH);
		}
		
		// MULTI LINE:
		else if (input.contains("\n")) {
			msg = String.format("Title must be a single line.");
		}
		
		return msg;
	}
	
	/**
	 * Validates the body text of a lesson. The text is valid when it is not null or blank and
	 * its length is between 10 and 255 characters inclusive.
	 *
	 * @param input the lesson text entered by the user
	 * @return an empty string if the text is valid; otherwise a message describing the
	 *         first rule that was violated
	 */
	
	public static String validateText(String input) {
		final int MAX_LENGTH = 255;
		final int MIN_LENGTH = 10; // should at least be 10 chars
		
		String msg = "";
		
		// EMPTY INPUT:
		if (input == null || input.isBlank()) {
			msg = "Text cannot be blank. Please enter in text.";
		}
		
		// LENGTH TOO LONG:
		else if (input.length() > MAX_LENGTH) {
			msg = String.format("Your text is too long at %d characters. The max length is %d characters.", input.length(), MAX_LENGTH);
		}
		
		else if (input.length() < MIN_LENGTH) {
			msg = String.format("Text is too short at %d characters. The minimum length is %d characters.", input.length(), MIN_LENGTH);
		}

		return msg;
	}
}
