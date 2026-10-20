package guiLessonsHome;

import guiContributor.ViewContributorHome;	
import guiCreateLesson.ViewCreateLesson;
import guiDeleteUser.ViewDeleteUser;
import guiViewLesson.ViewViewLesson;

import java.util.Optional;

import database.Database;
import entityClasses.User;
import entityClasses.LessonsLearned;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ControllerLessonsHome {
	private static Database database = applicationMain.FoundationsMain.database;
	
	protected static void goToCreation(Stage theStage, User theUser) {
		ViewCreateLesson.displayCreateLesson(theStage, theUser);
	}
	
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
				
		
		if (result.get() == ButtonType.NO) {
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
	
	// Return to the Contributor Home Page:
	/*
	 * [LATER]: need to fix this to account for the curator as well.
	 */
	protected static void performReturn() {
		ViewContributorHome.displayContributorHome(ViewLessonsHome.theStage, ViewLessonsHome.theUser);
	}
}
