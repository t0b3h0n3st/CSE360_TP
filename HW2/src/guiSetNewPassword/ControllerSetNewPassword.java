package guiSetNewPassword;

import database.Database;
import passwordRecognizer.PasswordRecognizer;

/** Validation and persistence for the consumed one-time password flow. */
final class ControllerSetNewPassword {
	private static final Database database = applicationMain.FoundationsMain.database;

	private ControllerSetNewPassword() { }

	/** Return null only after the new password has been saved successfully. */
	static String save(String username, String usedOneTimePassword,
			String newPassword, String confirmedPassword) {
		if (newPassword == null || confirmedPassword == null)
			return "Enter and confirm a new password.";
		// Check both text field limits before comparing or evaluating their contents.
		if (newPassword.length() > PasswordRecognizer.MAX_LENGTH
				|| confirmedPassword.length() > PasswordRecognizer.MAX_LENGTH)
			return "Password fields must be at most "
					+ PasswordRecognizer.MAX_LENGTH + " characters.";
		if (!newPassword.equals(confirmedPassword))
			return "The passwords do not match.";
		if (newPassword.equals(usedOneTimePassword))
			return "Choose a new password different from the one-time password.";
		String error = PasswordRecognizer.evaluatePassword(newPassword);
		if (!error.isEmpty()) return error;
		if (!database.saveResetPassword(username, newPassword))
			return "The reset could not be completed. Ask an admin for a new one-time password.";
		return null;
	}
}
