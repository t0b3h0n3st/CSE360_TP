package guiViewLesson;

public class ModelViewLesson {
	/** 
	 * validation for title:
	 * @return
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
