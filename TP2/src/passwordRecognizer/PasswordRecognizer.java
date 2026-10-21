package passwordRecognizer;

/**
 * Password rules from PasswordEvaluationTestbed-F26's
 * passwordPopUpWindow.Model.evaluatePassword, kept independent of its JavaFX UI.
 */
public final class PasswordRecognizer {
	/** Application-level limit used consistently by password and OTP input screens. */
	public static final int MAX_LENGTH = 32;
	private static final String SPECIAL_CHARACTERS = "~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/";

	private PasswordRecognizer() { }

	/** Return an empty string when the password satisfies every rule. */
	public static String evaluatePassword(String input) {
		if (input == null || input.isEmpty()) return "The password is empty.";
		if (input.length() > MAX_LENGTH)
			return "The password must be at most " + MAX_LENGTH + " characters.";

		boolean upper = false;
		boolean lower = false;
		boolean digit = false;
		boolean special = false;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c >= 'A' && c <= 'Z') upper = true;
			else if (c >= 'a' && c <= 'z') lower = true;
			else if (c >= '0' && c <= '9') digit = true;
			else if (SPECIAL_CHARACTERS.indexOf(c) >= 0) special = true;
			else return "The password contains an invalid character at position " + (i + 1) + ".";
		}

		StringBuilder missing = new StringBuilder();
		if (!upper) missing.append("an uppercase letter, ");
		if (!lower) missing.append("a lowercase letter, ");
		if (!digit) missing.append("a digit, ");
		if (!special) missing.append("a special character, ");
		if (input.length() < 8) missing.append("at least 8 characters, ");
		if (missing.length() == 0) return "";
		missing.setLength(missing.length() - 2);
		return "The password needs " + missing + ".";
	}
}
