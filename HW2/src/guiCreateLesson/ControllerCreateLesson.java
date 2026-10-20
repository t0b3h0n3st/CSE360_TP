package guiCreateLesson;

import entityClasses.User;
import guiDeleteUser.ViewDeleteUser;
import guiLessonsHome.ViewLessonsHome;
import entityClasses.LessonsLearned;

import java.util.Optional;

import database.Database;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ControllerCreateLesson {
	
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * <p> confirm creation of the lesson, check validation using Model.
	 * @param theUser
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
	 * Return to the Lessons Learned Home Page:
	 */
	protected static void performReturn() {	
		// DO WE WANT THIS BEHAVIOR? delete when you return, or just have it save during the local session?
//		ViewCreateLesson.lessonTitle.clear();
//		ViewCreateLesson.lessonText.clear();
		ViewLessonsHome.displayLessonsHome(ViewCreateLesson.theStage, ViewCreateLesson.theUser);
	}
}
