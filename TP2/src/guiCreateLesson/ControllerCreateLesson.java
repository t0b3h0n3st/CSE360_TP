package guiCreateLesson;

import entityClasses.User;
import guiDeleteUser.ViewDeleteUser;
import guiLessonsHome.ViewLessonsHome;
import entityClasses.LessonsLearned;

import java.util.Optional;

import database.Database;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * <p> Title: ControllerCreateLesson </p>
 *
 * <p> Description: The controller for the Create Lesson page. It reads the values entered in
 * {@link ViewCreateLesson}, validates them through {@link ModelCreateLesson}, and stores
 * new lessons in the database.</p>
 *
 * @author who do i put here?
 * @version 2.0
 */

public class ControllerCreateLesson {
	
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * Handles the Confirm button. The title and text are read from the form and validated; if
	 * either is invalid an alert listing the errors is shown and nothing is saved. Otherwise
	 * the user is asked to confirm, and on confirmation a new
	 * {@link entityClasses.LessonsLearned} is written to the database with the current user
	 * as its author. After a successful save the form fields are cleared.
	 *
	 * @param theUser the currently logged-in user, who becomes the author of the lesson
	 */
	
	protected static void confirmCreation(User theUser) {
		// PARAMETERS FROM TEXT FIELDS:
		String title = ViewCreateLesson.lessonTitle.getText().trim();
		String text = ViewCreateLesson.lessonText.getText().trim();
		String author = theUser.getUserName(); // automatically set
		
		// VALIDATE ALL TEXT FIELDS NEEDED (LENGTH CHECKS)
		String titleErr = ModelCreateLesson.validateTitle(title);
		String textErr = ModelCreateLesson.validateText(text);
		
		// check for an error:
		if (!titleErr.equals("") || !textErr.equals("")) {
			// display an alert:
			ViewCreateLesson.alertMsg.setContentText(titleErr + "\n" + textErr);
			ViewCreateLesson.alertMsg.showAndWait();
			return;
		}
				
		// prompt the User for final confirmation:
		ViewCreateLesson.alertConfirmation.setContentText("Are you sure you want to create this lesson?"); 
		Optional<ButtonType> result = ViewCreateLesson.alertConfirmation.showAndWait();
				
		// if the user clicks not "OK", return.
		if (result.get() == ButtonType.NO) {
			return;
		}
		
		// create the lesson object
		LessonsLearned lesson = new LessonsLearned(author, title, text);
		
		// send it to the database:
		database.addLesson(lesson);
		
		// alert the user of a success:
		ViewCreateLesson.resultMsg.setContentText("Successfully added the lesson.");
		
		// clear on a success:
		ViewCreateLesson.lessonTitle.clear();
		ViewCreateLesson.lessonText.clear();
	}
	
	/**
	 * Handles the Return button by navigating back to the Lessons Learned Home page. Any
	 * text left in the form is not saved.
	 */
	
	protected static void performReturn() {	
		// DO WE WANT THIS BEHAVIOR? delete when you return, or just have it save during the local session?
//		ViewCreateLesson.lessonTitle.clear();
//		ViewCreateLesson.lessonText.clear();
		ViewLessonsHome.displayLessonsHome(ViewCreateLesson.theStage, ViewCreateLesson.theUser);
	}
}
