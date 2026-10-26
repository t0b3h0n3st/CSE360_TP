package testbeds;

import java.sql.SQLException;
import java.util.List;

import database.Database;
import entityClasses.Category;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedCollection;
import entityClasses.User;
import guiLessonsHome.ViewLessonsHome;

/**
 * <p> ContributorTestbed is the testbed class for more contributor-specific interactions with LessonsLearned
 * @author Harrison
 * @version 2.0
 */
public class ContributorTestbed {
	
	/** 
	 * Private constructor is not used.
	 */
	private ContributorTestbed() {}
	
	private static Database database;
	
	
	/**
	 * The main method is where all of the test cases are computed. It utilizes some outer methods too.
	 * @param args command line arguments
	 * @throws SQLException if database connection or operation fails
	 */
	public static void main (String args[]) throws SQLException {
		// no idea how it works, but it's used in our TP1 database testbed. i guess it separates it from our main database, so it serves as a temp
		System.setProperty("foundation.db.url", "jdbc:h2:mem:tp1_test;DB_CLOSE_DELAY=-1");
		database = new Database();
		database.connectToDatabase(); // could cause the exception:
		
		/* --------------------------------
		 * DATABASE SETUP:
		   -------------------------------- */
		line(2);
		System.out.println("Adding Lessons into the Database");
		line(2);
		
		LessonsLearned lesson1 = LessonsTestbed.createLesson("Kevin", "Issue on Line 5", "I experienced a severe bug on Line 5 of our program.", Category.Technical, "This looks good to me. Let's note this down.", 1, false, false, false);
		LessonsLearned lesson2 = LessonsTestbed.createLesson("Kevin", "Communication Issues.", "We need to change our team's communication problems. We should setup a Slack.", Category.Communication, "Lets bring this up with the rest of the team.", 2, false, false, false);
		LessonsLearned lesson3 = LessonsTestbed.createLesson("Bob", "Poor Task Organization", "We did not delegate tasks clearly, so we were too inefficent.", Category.Project_Management, "Bob brings a fair point. Let's work on this.", 3, false, false, false);
		LessonsLearned lesson4 = LessonsTestbed.createLesson("Jane", "Eclipse Issues", "I had an issue with Eclipse where JavaFX programs did not run. You have to set the run configurations to fix this.", Category.Technical, "Bring this up in the next meeting.", 4, false, false, false);
		
		database.addLesson(lesson1);
		database.addLesson(lesson2);
		database.addLesson(lesson3);
		database.addLesson(lesson4);
		
		line(2);
		System.out.println("Reading All Lessons within the Database");
		line(2);
		
		LessonsLearnedCollection allLessons = database.getAllLessons();
		dispLessonList(allLessons);
		
		
		/* -----------------------------------------------
		 * TESTING NON-GUI RELIANT CONTRIBUTOR FEATURES:
		   ----------------------------------------------- */
		
		/* ------------------
		 * LOCK FEATURE
		   ------------------ */
		
		line(2);
		System.out.println("Testing Lesson Lock Feature:");
		line(2);
		
		// database methods:
		
		// lesson1: 
		database.lockTitle(lesson1.getAuthor(), lesson1.getLessonID());
		database.lockDescription(lesson1.getAuthor(), lesson1.getLessonID());
		database.lockLesson(lesson1.getAuthor(), lesson1.getLessonID());
		
		// lesson2:
		database.lockTitle(lesson2.getAuthor(), lesson2.getLessonID());
		
		// lesson3: 
		database.lockDescription(lesson3.getAuthor(), lesson3.getLessonID());
		
		// update the reference, print out its attributes
		lesson1 = database.getLessonByID(lesson1.getLessonID());
		lesson2 = database.getLessonByID(lesson2.getLessonID());
		lesson3 = database.getLessonByID(lesson3.getLessonID());
		LessonsTestbed.dispLesson(lesson1); 
		LessonsTestbed.dispLesson(lesson2); 
		LessonsTestbed.dispLesson(lesson3); 
		
		/* ------------------
		 * UNLOCK FEATURE
		   ------------------ */
		
		line(2);
		System.out.println("Testing Lesson Unlock Feature:");
		line(2);
		
		// database methods:
		database.unlockTitle(lesson1.getAuthor(), lesson1.getLessonID());
		database.unlockDescription(lesson1.getAuthor(), lesson1.getLessonID());
		database.unlockLesson(lesson1.getAuthor(), lesson1.getLessonID());
		
		// update the reference, print out its attributes
		lesson1 = database.getLessonByID(lesson1.getLessonID());
		LessonsTestbed.dispLesson(lesson1);
		
		
		/* ------------------
		 * DELETION
		   ------------------ */
		
		line(2);
		System.out.println("Testing Deleting Locked/Unlocked Lessons:");
		line(2);
		
		// one or more fields locked -> deletion fails.
		testDeleteLockedLesson(lesson2);
		testDeleteLockedLesson(lesson3);
		
		// all fields unlocked -> deletion successful.
		testDeleteLockedLesson(lesson1);
		testDeleteLockedLesson(lesson4);
		
		/* ------------------
		 * DELETION OUTPUT
		   ------------------ */
		line(2);
		System.out.println("Showing Result of Deletion:");
		line(2);
		
		allLessons = database.getAllLessons();
		dispLessonList(allLessons);
		
		
	} 
	
	/**
	 * testDeleteLockedLesson is a test method that tests whether a lesson can be deleted or not based on whether it contains any locked fields.
	 * @param lesson the lesson to be deleted from the database
	 * @return {@code true} if the lesson is deleted, {@code false} if the lesson could not be deleted due to a field being locked / the lesson not existing in the database.
	 */
	public static boolean testDeleteLockedLesson(LessonsLearned lesson) {
		
		if (lesson.getIsLessonLocked() || lesson.getIsTitleLocked() || lesson.getIsDescriptionLocked()) {
			System.out.printf("(ID = %d) Part of the lesson is locked, so you are unable to delete it.\n", lesson.getLessonID());
			return false;
		}
		
		database.deleteLesson(lesson.getLessonID());
		System.out.printf("Successfully deleting the lesson with ID = %d\n", lesson.getLessonID());
		return true;
	}
	
	// HELPER METHODS:
	
	/**
	 * dispLessonList is a helper method that takes a LessonsLearnedCollection and prints out all of its lessons and their attributes
	 * @param list input LessonsLearnedCollection to iterate through its lessons
	 */
	public static void dispLessonList(LessonsLearnedCollection list) {
		List<LessonsLearned> temp = list.getLessons();
		for (int i = 0; i < temp.size(); i++) {
			LessonsTestbed.dispLesson(temp.get(i));
		}
	}
	
	/**
	 * line is the same helper method borrowed from LessonsTestbed.
	 * @param count the number of times to repeat the line
	 */
	public static void line(int count) {
		for (int i = 0; i < count; i++) {
			System.out.print("--------------------");
		}
		System.out.println();
	}
	
}
