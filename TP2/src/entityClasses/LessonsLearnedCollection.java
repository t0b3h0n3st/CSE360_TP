package entityClasses;

import java.util.List;
import java.util.ArrayList;

/**
 * <p> Title: LessonsLearnedCollection </p>
 *
 * <p> Description: A simple wrapper around a {@link java.util.List} of {@link LessonsLearned}
 * objects. It is used to pass groups of lessons (for example, all lessons written by one
 * author) between the database layer and the GUI.</p>
 *
 * @version 2.0
 * @see LessonsLearned
 */

public class LessonsLearnedCollection {
	private List<LessonsLearned> lessonsList;
	
	/**
	 * Creates an empty collection of lessons.
	 */
	
	public LessonsLearnedCollection() {
		lessonsList = new ArrayList<LessonsLearned>();
	}
	
	/**
	 * Adds a lesson to the end of this collection.
	 *
	 * @param lesson the lesson to add
	 */
	
	public void addLesson(LessonsLearned lesson) {
		lessonsList.add(lesson);
	}
	
	/**
	 * Removes the first lesson whose title matches the given title, ignoring case. If several
	 * lessons share the same title, only the first match is removed.
	 *
	 * @param title the title of the lesson to remove
	 * @return {@code true} if a lesson was found and removed, {@code false} if no lesson
	 *         had a matching title
	 */
	
	public boolean removeLessonByTitle(String title) {
		// iterate through the List
		for (LessonsLearned lesson: lessonsList) {
			// if there's a match in lesson titles:
			if (lesson.getLessonTitle().equalsIgnoreCase(title)) {
				// remove through the type (the lesson in the orig arrayList)
				lessonsList.remove(lesson);
				// stop:
				// LATER : perhaps deal with duplicates?
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the underlying list of lessons. The list is not copied, so changes to the
	 * returned list are reflected in this collection.
	 *
	 * @return the list of lessons in this collection
	 */
	
	public List<LessonsLearned> getLessons() {
		return this.lessonsList;
	}
	
	/**
	 * Builds a list containing the title of every lesson in this collection, in the same
	 * order as the lessons themselves.
	 *
	 * @return a new list of lesson titles (empty if the collection is empty)
	 */
	
	public List<String> returnTitles() {	
		// create a new titles array to return:
		List<String> titles = new ArrayList<String>();
		// iterate all lessons:
		for (LessonsLearned lesson : lessonsList) {
			// add in each title:
			titles.add(lesson.getLessonTitle());
		}
		return titles;
	}
	
}
