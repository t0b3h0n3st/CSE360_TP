package guiDeleteUser;

import java.sql.SQLException;	
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import database.Database;
import entityClasses.User;
import guiAddRemoveRoles.ViewAddRemoveRoles;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * <p> Description: This is the class that contains various methods that trigger whenever the User does certain actions within the View.
 * 					The two actions are when a button is clicked and when text is entered in a TextField.
 */
public class ControllerDeleteUser {
	
	// The default constructor never gets used.
	private ControllerDeleteUser() {}
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * <p> Method: performDeletion(User user)
	 * <p> Description: takes the selected text from the ListView and will prompt a message to delete it.
	 * There is no need to check if the given user exists, since all elements within the list are from the database.</p>
	 * @param user is the User object you are attempting to delete from the database.
	 */
	protected static void performDeletion(User user) {
		// collect both the text's string and the current user's name.
		String currentUser = user.getUserName();
		String selectedUser = ViewDeleteUser.list.getSelectionModel().getSelectedItem(); // this line is what gets the selected row String.
		
		// if the text input is null, deliver a specific alert telling them to enter an input.
		if (selectedUser == null) {
			ViewDeleteUser.alertMsg.setContentText("Please select a user among the list to be deleted.\n");
			ViewDeleteUser.alertMsg.showAndWait();
			return;
		}

		// admin users cannot delete themselves. the must get someone else to delete them.
		else if (database.isAdmin(selectedUser) && currentUser.equals(selectedUser)) {
			ViewDeleteUser.alertMsg.setContentText("You cannot remove yourself. Another admin must remove you.\n");
			ViewDeleteUser.alertMsg.showAndWait();
			return;
		}
		
		// before the deletion method goes through, send a confirmation alert to the user.
		ViewDeleteUser.alertConfirmation.setContentText("Are you sure you want to delete user \"" + selectedUser + "\"?");
		Optional<ButtonType> result = ViewDeleteUser.alertConfirmation.showAndWait();
		
		// if the user clicks not "OK", return.
		if (result.get() == ButtonType.NO) {
			return;
		}
		
		// perform the method:
		
		String message = "";
				
		// will only delete if it's successful.
		if (database.deleteUser(selectedUser)) {
			ViewDeleteUser.list.getItems().remove(selectedUser); // remove it from the list, so that it doesn't show in the UI:
			message = String.format("You were successful in deleting the user \"%s\".\n", selectedUser);
		}
		else {
			message = String.format("The user \"%s\" does not seem to appear in the database or a run-time error occured. Please try again.\n", selectedUser);
		}
		
		// returning String alert:
		System.out.println(message);
		ViewDeleteUser.resultMsg.setContentText(message);
		ViewDeleteUser.resultMsg.showAndWait();
		
	}
	
	/* -------
	 * filterList() reads from text from searchFilter every time text is entered.
	 * If it is null or empty, just display all users.
	 * If there's a keyword, filter it by whether it contains that keyword.
	   -------*/
	/** 
	 * Method: filterList(ListView<String> list)
	 * <p> Description: reads from text from searchFilter every time text is entered.
	 * If it is null or empty, just display all users.
	 * If there's a keyword, filter it by whether it contains that keyword.</p>
	 * @param list the current ListView in the View class that displays all Users within the database.
	 */
	protected static void filterList(ListView<String> list) {
		String filterKey = ViewDeleteUser.searchFilter.getText();
		if (filterKey == null || filterKey.isEmpty()) {
			ViewDeleteUser.fillList(list, database.getUserList());
			return;
		}
		
		List<String> filtered = new ArrayList<String>(); // bugs when you use ListView and remove items, so better to use a arraylist.
		for (String userName : database.getUserList()) {
			if (userName.contains(filterKey) && !userName.equals("<Select a User>")) {
				filtered.add(userName);
			}
		}
		list.getItems().setAll(filtered);
	}
	
	/** 
	 * <p> Method: performReturn()
	 * <p> Description: allows the User to return to the Admin Home page.
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewDeleteUser.theStage,
				ViewDeleteUser.theUser);
		ViewDeleteUser.searchFilter.clear();
	}
	
	/* ----------
	 * TIANA's CODE: listUsers() button: 
	   --------- */
	/**
	 * <p> Method: listUsers()
	 * <p> Description: displays a separate GUI page that includes a list of all Users within the database and their information.
	 */
	protected static void listUsers() {
		List<User> userAccountsList = database.getUserAccountsList();

		        if (userAccountsList == null || userAccountsList.isEmpty()) {
		            ViewDeleteUser.alertNotImplemented.setTitle("List All Users");
		            ViewDeleteUser.alertNotImplemented.setHeaderText("No User Accounts Found");
		            ViewDeleteUser.alertNotImplemented.setContentText(
		                    "There are currently no user accounts in the database.");
		            ViewDeleteUser.alertNotImplemented.showAndWait();
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

		        ViewDeleteUser.displayUserAccountsList(sb.toString(), userAccountsList.size());
		    }
	
	/**
	 * <p> Method: String formatFullname(User u)
	 * <p> Description: formats each User's name nicely by appending Strings
	 * @param u the current User in the List.
	 * @return a formated String with the User's name information.
	 */
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
	
	/**
	 * <p> Method: String formatRoles(User u)
	 * <p> Description: formats each User's roles nicely by appending Strings
	 * @param u the current User in the List.
	 * @return a formated String with the User's role information.
	 */
    protected static String formatRoles(User u) {
        StringBuilder roles = new StringBuilder();
        if (u.getAdminRole()) roles.append("Admin, ");
        // [harrison]: edited the roles so that it shows the right one: 
        if (u.getContributorRole()) roles.append("Contributor, ");
        if(u.getCuratorRole()) roles.append("Curator, ");
        if(roles.length() == 0) return "None";
        return roles.substring(0, roles.length() -2);
    }
    
    /** 
     * Method: String nonEmpty(String s)
     * Description: helper method that checks if a string is empty or not
     * @param s the given String to check 
     * @return an empty String if it's null, else, return the original String.
     */
    protected static String nonEmpty(String s) {
        return s == null ? "" : s;
    }
	
}