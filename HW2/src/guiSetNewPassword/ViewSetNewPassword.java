package guiSetNewPassword;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** The only page reachable after a successful one-time password login. */
public final class ViewSetNewPassword {
	private final Stage stage;
	private final String username;
	private String usedOneTimePassword;
	private final PasswordField newPassword = new PasswordField();
	private final PasswordField confirmPassword = new PasswordField();
	private final Label errorMessage = new Label();

	public static void displaySetNewPassword(Stage stage, String username,
			String usedOneTimePassword) {
		new ViewSetNewPassword(stage, username, usedOneTimePassword).display();
	}

	private ViewSetNewPassword(Stage stage, String username, String usedOneTimePassword) {
		this.stage = stage;
		this.username = username;
		this.usedOneTimePassword = usedOneTimePassword;
	}

	private void display() {
		Label title = new Label("Set New Password");
		title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
		Label instructions = new Label("The one-time password has been used. Set a new password for "
				+ username + ".");
		instructions.setWrapText(true);
		Label rules = new Label("Use 8 to " + passwordRecognizer.PasswordRecognizer.MAX_LENGTH
				+ " characters, including uppercase, lowercase, a digit, and a special character.");
		rules.setWrapText(true);
		newPassword.setPromptText("New Password");
		newPassword.setMaxWidth(330);
		confirmPassword.setPromptText("Confirm Password");
		confirmPassword.setMaxWidth(330);
		errorMessage.setStyle("-fx-text-fill: #b00020;");
		errorMessage.setWrapText(true);
		errorMessage.setMaxWidth(500);

		Button save = new Button("Save New Password");
		save.setDefaultButton(true);
		save.setOnAction(event -> save());
		Button cancel = new Button("Cancel");
		cancel.setOnAction(event -> cancel());

		VBox root = new VBox(18, title, instructions, rules, newPassword,
				confirmPassword, errorMessage, save, cancel);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		stage.setTitle("Set New Password");
		stage.setScene(new Scene(root, applicationMain.FoundationsMain.WINDOW_WIDTH,
				applicationMain.FoundationsMain.WINDOW_HEIGHT));
		stage.show();
	}

	private void save() {
		String error = ControllerSetNewPassword.save(username, usedOneTimePassword,
				newPassword.getText(), confirmPassword.getText());
		if (error != null) {
			errorMessage.setText(error);
			return;
		}
		endTemporarySession();
		guiUserLogin.ViewUserLogin.displayUserLogin(stage,
				"Password saved. Log in again using your new password.");
	}

	private void cancel() {
		endTemporarySession();
		guiUserLogin.ViewUserLogin.displayUserLogin(stage,
				"One-time password used. Ask an admin for another one to restart the reset.");
	}

	private void endTemporarySession() {
		newPassword.clear();
		confirmPassword.clear();
		usedOneTimePassword = null;
		applicationMain.FoundationsMain.database.clearCurrentUser();
		applicationMain.FoundationsMain.activeHomePage = 0;
	}
}
