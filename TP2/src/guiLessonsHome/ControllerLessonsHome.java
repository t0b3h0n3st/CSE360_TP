package guiLessonsHome;

import guiContributor.ViewContributorHome;	
import guiCurator.ViewCuratorHome;
import guiCreateLesson.ViewCreateLesson;
import guiDeleteUser.ViewDeleteUser;
import guiViewLesson.ViewViewLesson;

import java.util.Optional;

import database.Database;
import entityClasses.User;
import entityClasses.LessonsLearned;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


/**
 * <p> Title: ControllerLessonsHome </p>
 *
 * <p> Description: The controller for the Lessons Learned Home page. It handles navigation to
 * the create and view pages, deletion of the selected lesson, and returning to the
 * contributor home page. Lessons are shown in the list as {@code "[title] (ID: [id])"}, and
 * the ID is parsed back out of the selected row to look up the lesson.</p>
 *
 * @author who do i put here?
 * @version 2.0
 * @see ViewLessonsHome
 */
public class ControllerLessonsHome {
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * Navigates to the Create Lesson page.
	 *
	 * @param theStage the JavaFX stage on which to display the page
	 * @param theUser the currently logged-in user
	 */
	protected static void goToCreation(Stage theStage, User theUser) {
		ViewCreateLesson.displayCreateLesson(theStage, theUser);
	}
	
	/**
	 * Opens the lesson selected in the list on the View / Edit Lesson page. If no row is
	 * selected, an alert is shown instead. The lesson ID is parsed from the selected row text
	 * and used to load the full lesson from the database.
	 *
	 * @param theStage the JavaFX stage on which to display the page
	 * @param theUser the currently logged-in user
	 */
	
	protected static void goToView(Stage theStage, User theUser) {
		String selectedRow = ViewLessonsHome.list.getSelectionModel().getSelectedItem(); // this line is what gets the selected row String.]
		
		// no row selected, return & show error alert:
		if (selectedRow == null) {
			ViewLessonsHome.viewAlert.setContentText("You must select a row in order to view a lesson.");
			ViewLessonsHome.viewAlert.showAndWait();
			return;
		}
		
		// Rows in the form of "[title] (ID: [id])"
		int start = selectedRow.lastIndexOf("(ID: ") + 5; // get the last index of the opening parantheses, then the int value is 5 indices more
		int end = selectedRow.lastIndexOf(")"); // get the index of the last closing parantheses, we will not include this in the substring
		int id = Integer.parseInt(selectedRow.substring(start, end)); // parse the substring which returns the ID value in a string (into an integer)
		
		LessonsLearned lesson = database.getLessonByID(id);
		ViewViewLesson.displayViewLesson(theStage, theUser, lesson);
	}

	/**
	 * Deletes the lesson selected in the list. If no row is selected, an alert is shown. The
	 * user is asked to confirm before the lesson is removed from the database; on success the
	 * row is also removed from the list, and an alert reports the outcome (including database
	 * failures).
	 */
	protected static void performDeletion() {
		String selectedRow = ViewLessonsHome.list.getSelectionModel().getSelectedItem(); // this line is what gets the selected row String.
		if (selectedRow == null) {
			ViewLessonsHome.deleteAlert.setContentText("You must select a row in order to delete.");
			ViewLessonsHome.deleteAlert.showAndWait();
			return;
		}
		
		// before the deletion method goes through, send a confirmation alert to the user.
		ViewLessonsHome.deleteConfirm.setContentText("Are you sure you want to delete lesson: " + selectedRow + "?");
		Optional<ButtonType> result = ViewLessonsHome.deleteConfirm.showAndWait();
				
		
		if (result.get() == ButtonType.NO || result.isEmpty()) {
			return;
		}
		
		// PERFORM THE DELETION (should work unless given database issues)
		
		// Rows in the form of "[title] (ID: [id])"
		int start = selectedRow.lastIndexOf("(ID: ") + 5; // get the last index of the opening parantheses, then the int value is 5 indices more
		int end = selectedRow.lastIndexOf(")"); // get the index of the last closing parantheses, we will not include this in the substring
		int id = Integer.parseInt(selectedRow.substring(start, end)); // parse the substring which returns the ID value in a string (into an integer)
		
		// PERFORM DELETION:
		if (database.deleteLesson(id)) {
			ViewLessonsHome.list.getItems().remove(selectedRow); // remove it from the list, so that it doesn't show in the UI:
			
			if (ViewLessonsHome.list.getItems().isEmpty()) {
				ViewLessonsHome.message.setText("Please create a lesson to see it in the list view.");
			}
			
			ViewLessonsHome.deleteAlert.setContentText("Lesson successfully deleted.");
			ViewLessonsHome.deleteAlert.showAndWait();
		}
		else {
			ViewLessonsHome.deleteAlert.setContentText("Database error. Please try again");
			ViewLessonsHome.deleteAlert.showAndWait();
		}
		
	}
	
	/**
	 * Returns to the appropriate home page based on the user's active role.
	 * Curators return to the Curator Home page, while Contributors return
	 * to the Contributor Home page.
	 */
	protected static void performReturn() {
	    if (applicationMain.FoundationsMain.activeHomePage == 3) {
	        ViewCuratorHome.displayCuratorHome(
	            ViewLessonsHome.theStage, ViewLessonsHome.theUser);
	    }
	    else {
	        ViewContributorHome.displayContributorHome(
	            ViewLessonsHome.theStage, ViewLessonsHome.theUser);
	    }
	}
}	
