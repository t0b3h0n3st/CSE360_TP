package entityClasses;

import java.util.List;
import java.util.ArrayList;

/** 
 * <p> LessonsLearnedCollection class
 */

public class LessonsLearnedCollection {
	private List<LessonsLearned> lessonsList;
	
	/** 
	 * <p> Default constructor is only used.
	 */
	public LessonsLearnedCollection() {
		lessonsList = new ArrayList<LessonsLearned>();
	}
	
	// add lesson into the List:
	public void addLesson(LessonsLearned lesson) {
		lessonsList.add(lesson);
	}
	
	// remove lesson from the List by Title:
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
	
	public List<LessonsLearned> getLessons() {
		return this.lessonsList;
	}
	
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
