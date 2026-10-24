package guiAdminHome;

import database.Database;	
import java.util.List;
import entityClasses.User;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import passwordRecognizer.PasswordRecognizer;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		
		
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue(); // this is either "Admin", "Contributor", or "Curator"
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations() {

	    List<String> invitations = theDatabase.getInvitationList();

	    if (invitations == null || invitations.isEmpty()) {
	        new Alert(
	            Alert.AlertType.INFORMATION,
	            "There are no outstanding invitations.",
	            ButtonType.OK
	        ).showAndWait();
	        return;
	    }

	    StringBuilder message = new StringBuilder();

	    for (String invitation : invitations) {
	        message.append(invitation).append("\n");
	    }

	    new Alert(
	        Alert.AlertType.INFORMATION,
	        message.toString(),
	        ButtonType.OK
	    ).showAndWait();
	    
	    // helpful for printing codes in the console:
	    System.out.println(message);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void setOnetimePassword () {
		if (ViewAdminHome.theUser == null || !ViewAdminHome.theUser.getAdminRole()) return;
		List<String> users = theDatabase.getUserList();
		if (users == null || users.size() <= 1) {
			new Alert(AlertType.ERROR, "No users are available to reset.", ButtonType.OK)
					.showAndWait();
			return;
		}

		Dialog<Void> dialog = new Dialog<>();
		dialog.initOwner(ViewAdminHome.theStage);
		dialog.setTitle("Set a One-Time Password");
		dialog.setHeaderText("Select a user and enter a temporary password.");
		ButtonType setButton = new ButtonType("Set Password", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(setButton, ButtonType.CANCEL);

		ComboBox<String> selectUser = new ComboBox<>(FXCollections.observableArrayList(users));
		selectUser.getSelectionModel().select(0);
		selectUser.setPrefWidth(260);
		PasswordField temporaryPassword = new PasswordField();
		temporaryPassword.setPromptText("One-time password");
		GridPane fields = new GridPane();
		fields.setHgap(12);
		fields.setVgap(12);
		fields.setPadding(new Insets(12));
		fields.add(new Label("User:"), 0, 0);
		fields.add(selectUser, 1, 0);
		fields.add(new Label("Temporary password:"), 0, 1);
		fields.add(temporaryPassword, 1, 1);
		dialog.getDialogPane().setContent(fields);

		dialog.getDialogPane().lookupButton(setButton).addEventFilter(ActionEvent.ACTION, event -> {
			String username = selectUser.getValue();
			String password = temporaryPassword.getText();
			String error = null;
			if (username == null || username.equals("<Select a User>"))
				error = "Select a user.";
			else if (password == null || password.isEmpty())
				error = "Enter a temporary password.";
			else if (password.length() > PasswordRecognizer.MAX_LENGTH)
				error = "The temporary password must be at most "
						+ PasswordRecognizer.MAX_LENGTH + " characters.";
			else if (!theDatabase.setOneTimePassword(username, password))
				error = "The password could not be saved. Check the user and try again.";
			if (error != null) {
				new Alert(AlertType.ERROR, error, ButtonType.OK).showAndWait();
				event.consume();
			} else {
				new Alert(AlertType.INFORMATION,
						"The one-time password is set for " + username + ".", ButtonType.OK)
						.showAndWait();
			}
		});
		dialog.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void deleteUser() {
		
	        guiDeleteUser.ViewDeleteUser.displayDeleteUser(ViewAdminHome.theStage, ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	
	//TP1
	protected static void listUsers() {
			List<User> userAccountsList = theDatabase.getUserAccountsList();

			        if (userAccountsList == null || userAccountsList.isEmpty()) {
			            ViewAdminHome.alertNotImplemented.setTitle("List All Users");
			            ViewAdminHome.alertNotImplemented.setHeaderText("No User Accounts Found");
			            ViewAdminHome.alertNotImplemented.setContentText(
			                    "There are currently no user accounts in the database.");
			            ViewAdminHome.alertNotImplemented.showAndWait();
			            return;
			        }

			        StringBuilder sb = new StringBuilder();
			        for(User u : userAccountsList) {
			            sb.append("Username: ").append(u.getUserName()).append("\n");
			            sb.append("Name: ").append(formatFullName(u)).append("\n");
			            sb.append("Email: ").append(nonEmpty(u.getEmailAddress())).append("\n");
			            sb.append("Roles: ").append(formatRoles(u)).append("\n");
			            sb.append("-------------------------------------------\n");
			        }

			        ViewAdminHome.displayUserAccountsList(sb.toString(), userAccountsList.size());
	}
	
	protected static String formatFullName(User u) {
        StringBuilder name = new StringBuilder();
        name.append(nonEmpty(u.getFirstName()));
        if (u.getMiddleName() != null && !u.getMiddleName().isEmpty())
            name.append(" ").append(u.getMiddleName());
        if (u.getLastName() != null && !u.getLastName().isEmpty())
            name.append(" ").append(u.getLastName());
        if (u.getPreferredFirstName() != null && !u.getPreferredFirstName().isEmpty())
            name.append(" (").append(u.getPreferredFirstName()).append(")");
        return name.toString().trim();
    }
    protected static String formatRoles(User u) {
        StringBuilder roles = new StringBuilder();
        if (u.getAdminRole()) roles.append("Admin, ");
        // [HARRISON]: edited so it uses the correct role names: 
        if (u.getContributorRole()) roles.append("Contributor, ");
        if(u.getCuratorRole()) roles.append("Curator, ");
        if(roles.length() == 0) return "None";
        return roles.substring(0, roles.length() -2);
    }

    protected static String nonEmpty(String s) {
        return s == null ? "" : s;
    }
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		String email_errMessage = ModelAdminHome.checkEmailAddress(emailAddress);
		if (!email_errMessage.equals("")) { // invalid username
			ViewAdminHome.alertEmailError.setContentText(email_errMessage);
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
