package guiViewLesson;

/**
 * <p> Title: ModelViewLesson </p>
 *
 * <p> Description: The model for the View / Edit Lesson page. It provides the same title and
 * text validation rules used when creating a lesson. An empty string returned from a
 * validator means the input is valid.</p>
 *
 * @version 2.0
 * @see ControllerViewLesson
 * @see ViewViewLesson
 */

public class ModelViewLesson {
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
			msg = ""; // if it's empty, it'll still pass, so it's optional.
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
	
	public static String validateCuratorText(String input) {
		final int MAX_LENGTH = 255;
		final int MIN_LENGTH = 5; // should at least be 10 chars
		
		String msg = "";
		
		// EMPTY INPUT:
		if (input == null || input.isBlank()) {
			msg = "";
		}
		
		// LENGTH TOO LONG:
		else if (input.length() > MAX_LENGTH) {
			msg = String.format("Your curator info text is too long at %d characters. The max length is %d characters.", input.length(), MAX_LENGTH);
		}
		
		else if (input.length() < MIN_LENGTH) {
			msg = String.format("Curator info text is too short at %d characters. The minimum length is %d characters.", input.length(), MIN_LENGTH);
		}

		return msg;
	}
}
