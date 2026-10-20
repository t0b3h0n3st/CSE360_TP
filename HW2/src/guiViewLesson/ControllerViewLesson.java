package guiViewLesson;

import java.util.Optional;

import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.User;
import guiCreateLesson.ModelCreateLesson;
import guiCreateLesson.ViewCreateLesson;
import guiLessonsHome.ViewLessonsHome;
import javafx.scene.control.ButtonType;

public class ControllerViewLesson {
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * 
	 * @param lesson the original lesson, contains the original id as well
	 * @param theUser
	 */
	protected static void performSave(LessonsLearned lesson, User theUser) {
		// read the text inputs:
		String title = ViewViewLesson.titleText.getText().trim();
		String text = ViewViewLesson.lessonText.getText().trim();
		String author = lesson.getAuthor(); // preset by whoever made it:
		
		// VALIDATE ALL TEXT FIELDS NEEDED (LENGTH CHECKS)
		String titleErr = ModelCreateLesson.validateTitle(title);
		String textErr = ModelCreateLesson.validateText(text);
		
		// check for an error:
		if (!titleErr.equals("") || !textErr.equals("")) {
			// display an alert:
			ViewViewLesson.saveAlert.setContentText(titleErr + "\n" + textErr);
			ViewViewLesson.saveAlert.showAndWait();
			return;
		}
		
		// if it was the same as original, then dont save and show alert:
		if (lesson.getLessonTitle().trim().equals(title) && lesson.getLessonText().trim().equals(text)) {
			ViewViewLesson.saveAlert.setContentText("You did not make any changes, so there is no need to save.");
			ViewViewLesson.saveAlert.showAndWait();
			return;
		}
		
		// confirm if user would like to save:
		ViewViewLesson.saveConfirm.setContentText("Are you sure you want to save these changes to the lesson?"); 
		Optional<ButtonType> result = ViewViewLesson.saveConfirm.showAndWait();
						
		// if the user clicks not "OK", return.
		if (result.get() == ButtonType.NO) {
					return;
		}
		
		// create a new Lessons object out of the data extracted from text fields that they updated. 
		LessonsLearned editedLesson = new LessonsLearned(author, title, text);
		
		if (database.updateLesson(lesson.getLessonID(), editedLesson)) {
			ViewViewLesson.saveAlert.setContentText("Changes changed successfully! Returning to Lessons Learned Home.");
			ViewViewLesson.saveAlert.showAndWait();
			ViewLessonsHome.displayLessonsHome(ViewViewLesson.theStage, ViewViewLesson.theUser);
		}
		else {
			ViewViewLesson.saveAlert.setContentText("There was an error with the database. Please try again.");
			ViewViewLesson.saveAlert.showAndWait();
		}
	}
	
	protected static void performReturn() {
		ViewLessonsHome.displayLessonsHome(ViewViewLesson.theStage, ViewViewLesson.theUser);
	}
}
