package guiViewLesson;

import java.util.Optional;

import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.Category;
import entityClasses.Category;
import entityClasses.User;
import guiCreateLesson.ModelCreateLesson;
import guiCreateLesson.ViewCreateLesson;
import guiLessonsHome.ViewLessonsHome;
import javafx.scene.control.ButtonType;

/**
 * <p> Title: ControllerViewLesson </p>
 *
 * <p> Description: The controller for the View / Edit Lesson page. It validates and saves edits
 * to a lesson, toggles the title and description locks that a curator can apply, and returns
 * to the Lessons Learned Home page.</p>
 *
 * @version 2.0
 * @see ViewViewLesson
 * @see ModelViewLesson
 */

public class ControllerViewLesson {
	private static Database database = applicationMain.FoundationsMain.database;
	
	/**
	 * Handles the Save Edits button. The edited title and text are validated; if either is
	 * invalid, or if neither differs from the original, an alert is shown and nothing is
	 * saved. Otherwise the user is asked to confirm, and the lesson is updated in the
	 * database. The original author is preserved. On success the user is returned to the
	 * Lessons Learned Home page; on a database failure an error alert is shown.
	 *
	 * @param lesson the original lesson, which supplies the lesson ID and author
	 * @param theUser the currently logged-in user
	 */
	
	protected static void performSave(LessonsLearned lesson, User theUser) {
		// read the text inputs:
		String title = ViewViewLesson.titleText.getText().trim();
		String text = ViewViewLesson.lessonText.getText().trim();
		String author = lesson.getAuthor(); // preset by whoever made it:
		Category category = ViewViewLesson.category.getValue(); // get the value selected in the combo box:
		
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
		// updated condition to make it so if they dont update the category too, it doesnt count as a change:
		if (lesson.getLessonTitle().trim().equals(title) && lesson.getLessonText().trim().equals(text) && lesson.getCategory() == category) {
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
		LessonsLearned editedLesson = new LessonsLearned(author, title, text, category);
		
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
	
	//TP2
		// I should also buttons to remove the lock
		/**
		 * Toggles the lock on the lesson's title. If the button reads "Lock Title" the title is
		 * locked in the database and in the {@code lesson} object, the title field becomes
		 * read-only, and the button text changes to "Unlock Title". If it reads "Unlock Title"
		 * the reverse happens. An alert confirms the change.
		 *
		 * @param lesson the lesson whose title lock is being toggled
		 */
		protected static void performTitleLock(LessonsLearned lesson) {
			int id = lesson.getLessonID(); // ID of lesson
			String author = lesson.getAuthor(); //Author of Lesson
			
			author = database.getAuthorByID(id);
			
			if (ViewViewLesson.lockTitleButton.getText() == "Lock Title") {
				database.lockTitle(author, id); //Update the data in the database
			lesson.setIsTitleLocked(true); //updates in the LessonsLearned
			
			ViewViewLesson.titleText.setEditable(false);
			
			ViewViewLesson.lockTitleButton.setText("Unlock Title");
			
			ViewViewLesson.lockAlert.setContentText("Succesfully Locked the title/description.");
			ViewViewLesson.lockAlert.showAndWait();
			return;
			}
			
			if (ViewViewLesson.lockTitleButton.getText() == "Unlock Title") {
				database.unlockTitle(author, id); //Update the data in the database
				lesson.setIsTitleLocked(false); //updates in the LessonsLearned
				
				ViewViewLesson.titleText.setEditable(true);
				
				ViewViewLesson.lockTitleButton.setText("Lock Title");
				
				ViewViewLesson.lockAlert.setContentText("Succesfully Unlocked the title/description.");
				ViewViewLesson.lockAlert.showAndWait();
				}
		}
		
		/**
		 * Toggles the lock on the lesson's description. If the button reads "Lock Description"
		 * the description is locked in the database and in the {@code lesson} object, the text
		 * area becomes read-only, and the button text changes to "Unlock Description". If it
		 * reads "Unlock Description" the reverse happens. An alert confirms the change.
		 *
		 * @param lesson the lesson whose description lock is being toggled
		 */
		protected static void performDescriptionLock(LessonsLearned lesson) {
			int id = lesson.getLessonID(); // ID of lesson
			
			String author = "";
			
			author = database.getAuthorByID(id);
			
			
			
			if (ViewViewLesson.lockDescriptionButton.getText() == "Lock Description") {
				database.lockDescription(author, id); //Update the data in the database
			lesson.setIsDescriptionLocked(true); //updates in the LessonsLearned
			
			ViewViewLesson.lessonText.setEditable(false);
			
			ViewViewLesson.lockDescriptionButton.setText("Unlock Description");
			
			ViewViewLesson.lockAlert.setContentText("Succesfully Locked the title/description.");
			ViewViewLesson.lockAlert.showAndWait();
			 return;
			}
			
			if (ViewViewLesson.lockDescriptionButton.getText() == "Unlock Description") {
				database.unlockDescription(author, id); //Update the data in the database
				lesson.setIsDescriptionLocked(false); //updates in the LessonsLearned
				
				ViewViewLesson.lessonText.setEditable(true);
				
				ViewViewLesson.lockDescriptionButton.setText("Lock Description");
				
				ViewViewLesson.lockAlert.setContentText("Succesfully Unlocked the title/description.");
				ViewViewLesson.lockAlert.showAndWait();
				}
			
		}
		
		
		
		
		
		
		//End of TP2
		/**
		 * Handles the Return button by navigating back to the Lessons Learned Home page without
		 * saving any pending edits.
		 */
		protected static void performReturn() {
			ViewLessonsHome.displayLessonsHome(ViewViewLesson.theStage, ViewViewLesson.theUser);
		}
	}

