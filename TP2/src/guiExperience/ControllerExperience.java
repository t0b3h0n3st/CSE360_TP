package guiExperience;

import java.sql.SQLException;
import entityClasses.LessonExperience;

/*******
 * <p> Title: ControllerExperience </p>
 * <p> Description: Handles history refresh, adding attempts, and selected-record details.
 * Validation errors preserve form input; failed SQL writes are never reported as saved.</p>
 * @author Huichang
 */
public final class ControllerExperience {
	/** Static event-handler class; no instances are needed. */
	private ControllerExperience() { }

	/** Reloads history and clears stale results if access or storage fails. */
	protected static void refresh() {
		try {
			ViewExperience.history.getItems().setAll(ViewExperience.theModel.list());
			ViewExperience.status.setText(ViewExperience.history.getItems().size() + " saved attempt(s).");
		} catch (IllegalArgumentException error) {
			ViewExperience.history.getItems().clear();
			ViewExperience.status.setText(error.getMessage());
		} catch (SQLException error) {
			ViewExperience.history.getItems().clear();
			storageError(error);
		}
	}

	/** Saves one attempt after validation; form contents are cleared only after an insert succeeds. */
	protected static void add() {
		try {
			long id = ViewExperience.theModel.add(ViewExperience.whatDone.getText(),
					ViewExperience.howDone.getText(), ViewExperience.elapsed.getText(),
					ViewExperience.effort.getText());
			ViewExperience.clear();
			refresh();
			for (LessonExperience experience : ViewExperience.history.getItems()) {
				if (experience.getId() == id) {
					ViewExperience.history.getSelectionModel().select(experience);
					ViewExperience.status.setText("Experience " + id + " saved. Earlier attempts are unchanged.");
					return;
				}
			}
			ViewExperience.status.setText("Experience " + id + " was saved, but history could not be reloaded. Try Refresh.");
		} catch (IllegalArgumentException error) { ViewExperience.status.setText(error.getMessage()); }
		catch (SQLException error) { storageError(error); }
	}

	/**
	 * Shows a saved snapshot without populating the new-attempt editor (avoids overwriting history).
	 * @param experience selected attempt, or null when the selection is cleared
	 */
	protected static void show(LessonExperience experience) {
		ViewExperience.details.setText(experience == null ? "" :
				"What was done:\n" + experience.getWhatDone() + "\n\nHow it was done:\n"
				+ experience.getHowDone() + "\n\nElapsed minutes: " + experience.getElapsedMinutes()
				+ "\n\nMember effort (minutes):\n" + experience.getMemberEffort());
	}

	/** Logs diagnostic details while keeping SQL details out of the user-facing message. */
	private static void storageError(SQLException error) {
		error.printStackTrace();
		ViewExperience.status.setText("Could not complete the experience database operation. Try again.");
	}
}
