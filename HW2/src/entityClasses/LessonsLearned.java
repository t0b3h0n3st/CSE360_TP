package entityClasses;

/* ------------
 * 	HW2 CODE: 
   ------------*/

/**
 * <p> LessonsLearned class
 */

/* 
 * DATABASE, store a LESSONS table:
 * 
 * [ author ] , [lessonTitle], [lessonTitle
 */
public class LessonsLearned {
	// instance fields per each OBJECT: 
	
	private String author;
	private String lessonTitle;
	private String lessonText; 
	private int lessonID; 
	
	/** 
	 * <p> Default constructor not used.
	 */
	
	private LessonsLearned() {
		
	}
	
	/**
	 * Constructor for creating a lesson (because the ID is created by SQL)
	 * @param author
	 * @param lessonTitle
	 * @param lessonText
	 */
	public LessonsLearned(String author, String lessonTitle, String lessonText) {
		this.author = author;
		this.lessonTitle = lessonTitle;
		this.lessonText = lessonText;
	}
	
	/**
	 * Constructor for reading a lesson, because we need all information
	 * @param author
	 * @param lessonTitle
	 * @param lessonText
	 * @param lessonID
	 */
	public LessonsLearned(String author, String lessonTitle, String lessonText, int lessonID) {
		this.author = author;
		this.lessonTitle = lessonTitle;
		this.lessonText = lessonText;
		this.lessonID = lessonID;
	}
	
	// GETTERS:
	
	public String getAuthor() { return this.author; }
	public String getLessonTitle() { return this.lessonTitle; }
	public String getLessonText() { return this.lessonText; }
	public int getLessonID() { return this.lessonID; }
	
	// SETTERS:
	// public void setAuthor(String author) { this.author = author; }
	public void setLessonTitle(String lessonTitle) { this.lessonTitle = lessonTitle; }
	public void setLessonText(String lessonText) { this.lessonText = lessonText; }
	public void setLessonID(int lessonID) { this.lessonID = lessonID; }
	
	// VALIDATION:
	
	// validateTitle
	
	// validateText
}
