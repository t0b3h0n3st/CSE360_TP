package guiExperience;

import java.sql.SQLException;
import java.util.List;
import database.Database;
import entityClasses.LessonExperience;

/*******
 * <p> Title: ModelExperience </p>
 * <p> Description: Binds experience operations to the signed-in account and selected
 * lesson. These identities cannot be changed by editing form fields.</p>
 * @author Huichang
 */
public final class ModelExperience {
	/** Existing Foundations persistence boundary, shared with the lesson screens. */
	private final Database database;
	/** Signed-in user and selected lesson define the scope for every operation. */
	private final String contributor;
	/** Existing lesson whose attempts are being recorded. */
	private final int lessonId;

	/**
	 * Binds this page to its navigation context; the database rechecks access per request.
	 * @param database existing application database
	 * @param contributor signed-in username
	 * @param lessonId selected existing lesson
	 */
	public ModelExperience(Database database, String contributor, int lessonId) {
		this.database = database;
		this.contributor = contributor;
		this.lessonId = lessonId;
	}

	/**
	 * Reads the current lesson history through the same persistence boundary as add.
	 * @return saved attempts, including an empty history
	 * @throws SQLException on storage failure
	 * @throws IllegalArgumentException if access is no longer allowed
	 */
	public List<LessonExperience> list() throws SQLException {
		return database.getExperiences(contributor, lessonId);
	}

	/**
	 * Appends a new attempt; existing attempts and lesson text remain unchanged.
	 * @param whatDone activity performed
	 * @param howDone procedure used
	 * @param elapsedMinutes positive elapsed minutes
	 * @param memberEffort one named member and minutes per line
	 * @return generated attempt ID
	 * @throws SQLException on storage failure
	 * @throws IllegalArgumentException for invalid input or access
	 */
	public long add(String whatDone, String howDone, String elapsedMinutes, String memberEffort)
			throws SQLException {
		return database.addExperience(contributor, lessonId, whatDone, howDone, elapsedMinutes, memberEffort);
	}
}
