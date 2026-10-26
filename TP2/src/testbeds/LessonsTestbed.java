package testbeds;

import java.sql.SQLException;
import java.util.List;

import database.Database;
import entityClasses.Category;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedCollection;

/**
 *  * <p> Title: LessonsTestbed </p>
 *
 * <p> Description: A console-based test program designed to test all the required CRUD database operations
 *     of LessonsLearned class. </p>
 *
 * @author Harrison
 * @version 2.0
 */

public class LessonsTestbed {
	
	/**
	 * Constructor is not used
	 */
	private LessonsTestbed() {}
	
	/**
	 * A database variable for this whole class. This is created temporarily in memory
	 */
	private static Database database;
	
	/**
	 * <p> The main method runs all of the test methods that I have created.
	 * @param args command line arguments
	 * @throws SQLException if a database connection or operation fails
	 */
	public static void main (String args[]) throws SQLException {
		// no idea how it works, but it's used in our TP1 database testbed. i guess it separates it from our main database, so it serves as a temp
		System.setProperty("foundation.db.url", "jdbc:h2:mem:tp1_test;DB_CLOSE_DELAY=-1");
		database = new Database();
		database.connectToDatabase(); // could cause the exception:
		
		/* ---------------------
		 * C) ADDING LESSONS:
		   --------------------- */
		line(2);
		System.out.println("Adding Lessons into the Database");
		line(2);
		
		LessonsLearned lesson1 = createLesson("Kevin", "Issue on Line 5", "I experienced a severe bug on Line 5 of our program.", Category.Technical, "This looks good to me. Let's note this down.", 1, false, false, false);
		LessonsLearned lesson2 = createLesson("Kevin", "Communication Issues.", "We need to change our team's communication problems. We should setup a Slack.", Category.Communication, "Lets bring this up with the rest of the team.", 2, false, false, true);
		LessonsLearned lesson3 = createLesson("Bob", "Poor Task Organization", "We did not delegate tasks clearly, so we were too inefficent.", Category.Project_Management, "Bob brings a fair point. Let's work on this.", 3, true, true, true);
		LessonsLearned lesson4 = createLesson("Jane", "Eclipse Issues", "I had an issue with Eclipse where JavaFX programs did not run. You have to set the run configurations to fix this.", Category.Technical, "Bring this up in the next meeting.", 4, false, false, false);
		
		testCreateLesson(lesson1);
		testCreateLesson(lesson2);
		testCreateLesson(lesson3);
		testCreateLesson(lesson4);
		
		/* ---------------------
		 * R) READING LESSONS:
		   --------------------- */
		
		// POSITIVE TESTS 
		testGetAllLessons();
		testGetLessonByAuthor("Kevin");
		testGetLessonByAuthor("Bob");
		testGetLessonByID(3);
		
		// FAIL TESTS:
		testGetLessonByAuthor("Klaus");
		testGetLessonByID(9999);
		
		/* ---------------------
		 * U) UPDATING LESSONS:
		   --------------------- */
		
		LessonsLearned editedContent = new LessonsLearned("Rob", "I edited this title.", "We need to change our team's communication problems. We should setup a Discord channel for questions.", Category.Communication, "We are not going to use a Discord. Bad idea.");
		testUpdateLesson(2, editedContent);
		
		// FAIL TESTS:
		testUpdateLesson(999, editedContent);
		
		/* ---------------------
		 * D) DELETING LESSONS:
		   --------------------- */
		
		testDeleteLesson(1);
		testGetLessonByID(1); // should fail now;
		
		// FAIL TEST:
		testDeleteLesson(999);
		
		
		
		/* ---------------------
		 * FINAL OUTPUT:
		   --------------------- */
		
		System.out.println("\nFINAL OUTPUT:\n");
		
		testGetAllLessons();
		
	}
	
	/* --------------------------
	 * DATABASE TESTER METHODS
	   -------------------------- */
	
	/**
	 * <p> testCreateLesson is the testing method to see if a lesson can be successfully created.</p>
	 * @param lesson the given lesson to be created in the database
	 * @return {@code true} if the lesson is able to be created to the database, {@code false} if there is an SQL/database error.
	 */
	
	// C) LESSON CREATION:
	public static boolean testCreateLesson(LessonsLearned lesson) {
		if (database.addLesson(lesson)) {
			System.out.println("Lesson successfully added.");
			return true;
		}
		
		System.out.println("Lesson did not get added.");
		return false;
	}
	
	/* -------------
	 * READ METHODS:
	   ------------- */
	
	/**
	 * <p> testGetAllLessons returns a LessonsLearnedCollection of all the lessons in the database at the time it's called.</p>
	 * @return a LessonsLearnedCollection of all lessons, or null if there is no lessons created.
	 */
	
	// R) LESSON RETRIEVAL (ALL LESSONS)
	public static LessonsLearnedCollection testGetAllLessons() {
		LessonsLearnedCollection list = new LessonsLearnedCollection();
		
		line(2);
		System.out.println("Retrieving ALL Lessons from the Database");
		line(2);
		
		list = database.getAllLessons();
		List<LessonsLearned> lessonsList = list.getLessons();
		
		if (lessonsList.isEmpty()) {
			System.out.println("No lessons have been created yet.");
			return null;
		}
		
		for (int i = 0; i < lessonsList.size(); i++) {
			System.out.printf("(Lesson #%d) ", i+1);
			LessonsLearned temp = lessonsList.get(i);
			dispLesson(temp);
		}
		
		return list;

	}
	
	/**
	 * <p> testGetLessonByAuthor returns a LessonsLearnedCollection of all the lessons in the database created by a specific user.</p>
	 * @param name the author to filter lessons created by
	 * @return a LessonsLearnedCollection of all lessons made by that author, or null if there is no lessons created by that author.
	 */
	
	// R) LESSON RETRIEVAL (BY AUTHOR)
	public static LessonsLearnedCollection testGetLessonByAuthor(String name) {
		LessonsLearnedCollection list = new LessonsLearnedCollection();
		
		line(2);
		System.out.printf("Retrieving ONLY Lessons Created by %s\n", name);
		line(2);
		
		list = database.getLessonByAuthor(name);
		
		List<LessonsLearned> lessonsList = list.getLessons();
		
		if (lessonsList.isEmpty()) {
			System.out.printf("Unable to find any lessons created by user = %s\n\n", name);
			return null;
		}
		
		for (int i = 0; i < lessonsList.size(); i++) {
			LessonsLearned temp = lessonsList.get(i);
			System.out.printf("(Lesson #%d) ", i+1);
			dispLesson(temp);
		}
		
		return list;

	}
	
	/**
	 * <p> testGetLessonByID returns a given LessonsLearned object by its given ID </p>
	 * @param ID the unique specifier for that lesson
	 * @return the LessonsLearned object if the lesson exists, null if it does not exist with that given ID in the database.
	 */
	
	// R) LESSON RETRIEVAL (BY ID)
	public static LessonsLearned testGetLessonByID(int ID) {
		line(2);
		System.out.printf("Retrieving Lesson with ID = %d\n", ID);
		line(2);
		LessonsLearned lesson = database.getLessonByID(ID);
		if (lesson == null) {
			System.out.printf("Unable to find a lesson with ID = %d\n\n", ID);
			return null;
		} 
		dispLesson(lesson);
		return lesson;
	}
	
	/**
	 * <p> testUpdateLesson updates the lesson specified by an ID, with another lesson's details.
	 * @param ID the unique specifier for each lesson
	 * @param lesson the newly created lesson object to update the original one.
	 * @return the updated lesson (in case the user wants to use it for something)
	 */
	// U) UPDATE LESSON (ONLY THE CONTENT)
	public static LessonsLearned testUpdateLesson(int ID, LessonsLearned lesson) {
		line(2);
		System.out.println("Testing Update Lesson");
		line(2);
		
		LessonsLearned original = database.getLessonByID(ID);
		
		if (database.updateLesson(ID, lesson)) {
			System.out.println("Successfully Updated the Lesson\n");
			
			line(1);
			System.out.println("Old Lesson Details");
			line(1);
			dispLesson(original);
			
			line(1);
			System.out.println("Newly Edited Lesson Details");
			line(1);
			dispLesson(database.getLessonByID(ID));
			
			return database.getLessonByID(ID);
		}
		else {
			System.out.printf("Unable to update the lesson\n\n");
			return null;
		}
	}
	
	/**
	 * <p> testDeleteLesson permanently deletes a lesson from the database. 
	 * @param ID the unique specifier for the desired lesson
	 * @return {@code true} if the lesson exists and was deleted, {@code false} if it could not be deleted.
	 */
	// D) DELETE LESSON:
	public static boolean testDeleteLesson(int ID) {
		line(2);
		System.out.println("Testing Delete Lesson");
		line(2);
		
		LessonsLearned original = database.getLessonByID(ID);
		if (database.deleteLesson(ID)) {
			System.out.println("Lesson to be Deleted:");
			dispLesson(original);
			System.out.println("Lesson has successfully been deleted.\n");
			return true;
		}
		else {
			System.out.printf("Lesson with ID = %d could not be found in the database. Deletion failed.\n\n", ID);
			return false;
		}

	}
	
	/* ----------------
	 * HELPER METHODS
	   ---------------- */
	
	/**
	 * <p> createLesson is a simple helper method for initializing a LessonsLearned object
	 * @param author the lesson author
	 * @param title the lesson title
	 * @param text the lesson contributor text
	 * @param category the lesson's category
	 * @param curInfo the lesson's curator additional information
	 * @param id the lesson's unique identifier
	 * @param titleLocked if the lesson's title is locked
	 * @param descriptionLocked if the lesson's description is locked
	 * @param lessonLocked if the lesson itself is locked
	 * @return the LessonsLearned object 
	 */
	
	public static LessonsLearned createLesson(String author, String title, String text, Category category, String curInfo, int id, boolean titleLocked, boolean descriptionLocked, boolean lessonLocked) {
		return new LessonsLearned(author, title, text, category, curInfo, id, titleLocked, descriptionLocked, lessonLocked);
	}
	
	/**
	 * dispLesson is a helper method used to display an input lesson's attributes
	 * @param temp the lesson whose attributes will be displayed
	 */
	public static void dispLesson(LessonsLearned temp) {
		System.out.printf("Lesson Title: %s / Category: %s / by %s (ID: %d)\n", temp.getLessonTitle(), temp.getCategory().name(), temp.getAuthor(), temp.getLessonID());
		System.out.printf("Lesson Text: %s\n", temp.getLessonText());
		System.out.printf("Curator Notes: %s\n", temp.getCuratorAdditionalInformation());
		System.out.printf("Lesson Locked: %b | Title Locked: %b | Text Locked: %b\n\n", temp.getIsLessonLocked(), temp.getIsTitleLocked(), temp.getIsDescriptionLocked());
	}
	
	/**
	 * line is a simple method used to print out lines to the console. this is just used to split up the sections of text.
	 * @param count how many lines to print out
	 */
	public static void line(int count) {
		for (int i = 0; i < count; i++) {
			System.out.print("--------------------");
		}
		System.out.println();
	}
	
	
}
