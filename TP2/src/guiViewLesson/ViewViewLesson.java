package guiViewLesson;

import database.Database;
import entityClasses.User;
import entityClasses.Category;
import entityClasses.LessonsLearned;
import guiLessonsHome.ControllerLessonsHome;
import guiLessonsHome.ViewLessonsHome;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

/**
 * <p> Title: ViewViewLesson </p>
 *
 * <p> Description: The JavaFX view for the View / Edit Lesson page. It shows the title and
 * text of one lesson in editable fields, with buttons to save edits and to return. Curators
 * additionally see buttons to lock or unlock the title and description. A locked field is
 * read-only for the lesson's original author.</p>
 *
 * @author Harrison
 * @version 2.0
 * @see ControllerViewLesson
 * @see ModelViewLesson
 */

/* 
 * NOTE TO SELF: EDIT UPDATING LESSOn, CATEGORY CHANGE COUNTS AS DIFFERENT
 */
public class ViewViewLesson {
	// APPLICATION WINDOW DIMENSIONS:
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// IMPORTANT VARIABLES FROM OTHER CLASSES:
	protected static User theUser;
	protected static ViewViewLesson theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static LessonsLearned theLesson;
	
	// IMPORTANT VARIABLES FORM OTHER CLASSES
	private static String author;
	
	// CORE javaFX stuff:
	protected static Stage theStage;
	protected static Scene theScene;
	protected static Pane theRootPane;
	
	// LABELS:
	protected static Label topLabel = new Label("View / Edit Lesson");
	protected static Label titleLabel = new Label("Title: ");
	protected static Label lessonsLabel = new Label("Lessons Learned: ");
	
	protected static Label curatorInfoLabel =
	        new Label("Curator Additional Information: ");
	
	// BUTTONS:
	protected static Button saveButton = new Button("Save Edits");
	protected static Button saveCuratorInfoButton = new Button("Save Curator Info");
	protected static Button returnButton = new Button("Return");
	
	/** Opens attempt history without changing the lesson title or description. */
	protected static Button experienceButton = new Button("Experience Information");
	public static Button lockTitleButton = new Button("Lock Title");
	public static Button lockDescriptionButton = new Button("Lock Description");
	
	// TEXT FIELDS:
	protected static TextField titleText = new TextField();
	protected static TextArea lessonText = new TextArea();
	protected static TextArea curatorInfoText = new TextArea();
	
	// ALERTS:
	protected static Alert saveAlert = new Alert(AlertType.INFORMATION);
	protected static Alert saveConfirm = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
	
	protected static Alert lockAlert = new Alert(AlertType.INFORMATION);
	
	/* ---------------------------------------------
	 * adding in combo box for category selection:
	   --------------------------------------------- */
	// COMBO BOX (Category):
	protected static Label categoryLabel = new Label("Select a category: ");
	protected static ComboBox <Category> category;
	
	/**
	 * Displays the View / Edit Lesson page for a lesson. The fields are filled in from the
	 * lesson, the lock buttons are shown only to curators, and a field is made read-only when
	 * it is locked and the current user is the lesson's author.
	 *
	 * @param ps the JavaFX stage on which to show the page
	 * @param user the currently logged-in user
	 * @param lesson the lesson to display and edit
	 */
	
	public static void displayViewLesson(Stage ps, User user, LessonsLearned lesson) {
		if (theView == null) theView = new ViewViewLesson();        // Instantiate singleton if needed
		theStage = ps;
		theUser = user;
		theLesson = lesson; // theLesson variable will be used when passing into other args into controller.

		category.setValue(theLesson.getCategory());
		
		// automatically fill in the fields:
		titleText.setText(lesson.getLessonTitle());
		lessonText.setText(lesson.getLessonText());
		
		String currentUser = theUser.getUserName();

		//TP2: This checks to see if the role is curator or not
				if (theDatabase.isCurator(currentUser)) {
			        lockTitleButton.setVisible(true);
			        lockDescriptionButton.setVisible(true);
			        
			        // every time the curator enters the page: it'll be editable for them (fixes bug where you log in as contributor first)
			        titleText.setEditable(true);
			        lessonText.setEditable(true);
			        
			        // CURATOR INFORMATION BOX: 
	        	    curatorInfoLabel.setVisible(true);
	        	    curatorInfoText.setVisible(true);
	        	    saveCuratorInfoButton.setVisible(true);
	        	    curatorInfoText.setText(
	        	            lesson.getCuratorAdditionalInformation() == null
	        	            ? ""
	        	            : lesson.getCuratorAdditionalInformation());
	        	    
	        	    // CURATORS - set experience information button false
	        	    experienceButton.setVisible(false);
	        	    
			    } else {
			    	lockTitleButton.setVisible(false);
			        lockDescriptionButton.setVisible(false);
	        	    curatorInfoLabel.setVisible(false);
	        	    curatorInfoText.setVisible(false);
	        	    saveCuratorInfoButton.setVisible(false);
	        	    
	        	    // [HUICHANG CODE], can change this - but should experienceButton only be visible for contributors?
	        	    experienceButton.setVisible(true);
			    }
		
		//The part below could be changed depending on how we implement Curator. This part so far works as if 
		//The curator and contributor see the same page but if they have a different View then the current user check could be removed		
		//Check to see if It is the original author and if the title or description has to be locked		

				/* --------------------
				 * [harrison] : removed the condition for author. author check is already handled by the database read operations.
				 * bug fixed pt 2: made it such that curators can still edit (bug caused by my last change lol)
				   --------------------*/
				
				// if title locked: change the button text. make it uneditable only if they are not a curator.
				if (lesson.getIsTitleLocked()) {
					 System.out.println("Title is locked");
					 ViewViewLesson.lockTitleButton.setText("Unlock Title");
					 if (!theDatabase.isCurator(theUser.getUserName())) 
						 titleText.setEditable(false);
				} else {
					ViewViewLesson.lockTitleButton.setText("Lock Title");
					System.out.println("Title is not locked");
					titleText.setEditable(true);
				}
				
				// if lesson text locked: change the button text. make it uneditable only if they are not a curator.
				if (lesson.getIsDescriptionLocked()) {
					System.out.println("Description is locked");
					lockDescriptionButton.setText("Unlock Description");
					if (!theDatabase.isCurator(theUser.getUserName())) 
						lessonText.setEditable(false);
				} else {
					System.out.println("Description is not locked");
					lockDescriptionButton.setText("Lock Description");
					lessonText.setEditable(true);
				}
		
		// show the Stage: 
	   	theStage.setTitle("View / Edit Lesson");
    	theStage.setScene(theScene);
    	theStage.show();
	}
	
	private ViewViewLesson() {
		// CONSTRUCTOR STUFF: 
    	theRootPane = new Pane();
    	theScene = new Scene(theRootPane, width, height);
    	
    	// LABEL UI:
    	setupLabelUI(topLabel, "Arial", 28, width, Pos.CENTER, 0, 15);
    	setupLabelUI(curatorInfoLabel, "Arial", 16, 300, Pos.CENTER_LEFT, 150, 400);
    	setupLabelUI(titleLabel, "Arial", 18, 150, Pos.CENTER_RIGHT, 100, 62);
    	setupLabelUI(categoryLabel, "Arial", 18, 150, Pos.CENTER_RIGHT, 100, 102);
    	setupLabelUI(lessonsLabel, "Arial", 18, width, Pos.CENTER, 0, 150);

    	// BUTTON UI:
    	setupButtonUI(saveButton, "Dialog", 18, 150, Pos.CENTER, 240, 540);
    	setupButtonUI(lockTitleButton, "Dialog", 18, 150, Pos.CENTER, 440, 540);
    	setupButtonUI(lockDescriptionButton, "Dialog", 18, 150, Pos.CENTER, 640, 540);
    	setupButtonUI(saveCuratorInfoButton, "Dialog", 16, 180, Pos.CENTER, 670, 450);
    	
    	// bottom buttons: 
    	setupButtonUI(returnButton, "Dialog", 18, 200, Pos.CENTER, 20, 540);
		setupButtonUI(experienceButton, "Dialog", 18, 250, Pos.CENTER, 20, 495);
    	
       	// TEXT FIELD UI:
    	setupTextUI(titleText, "Arial", 16, 400, Pos.CENTER_LEFT, 280, 60, true);
    	
		// COMBO BOX:
	    category = new ComboBox<>();
	    category.getItems().addAll(Category.values());
		setupComboBoxUI(category, "Dialog", 16, 250, 280, 100);
    	
    	// TEXT AREA UI:
    	lessonText.setWrapText(true);
        lessonText.setPrefWidth(500);
        lessonText.setPrefHeight(250);
    	lessonText.setLayoutX(150);
    	lessonText.setLayoutY(140);
    	curatorInfoText.setWrapText(true);
    	curatorInfoText.setPrefWidth(500);
    	curatorInfoText.setPrefHeight(80);
    	curatorInfoText.setLayoutX(150);
    	curatorInfoText.setLayoutY(430);
    	
    	// ALERTS:
    	saveAlert.setTitle("Save Lesson Edits");
    	saveAlert.setHeaderText("Save Result");
    	lockAlert.setTitle("Locking");
    	lockAlert.setHeaderText("The title/dscription has been locked");
    	
		// confirmation alert
		saveConfirm.setTitle("Confirmation");
		saveConfirm.setHeaderText("Are You Sure?");
		
    	
    	// BUTTON ACTIONS:		
    	saveButton.setOnAction((_) -> {ControllerViewLesson.performSave(theLesson, theUser);}); // CREATE BUTTON:
		returnButton.setOnAction((_) -> {ControllerViewLesson.performReturn(); }); // RETURN TO HOME:
		experienceButton.setOnAction((_) -> guiExperience.ViewExperience.displayExperience(theStage, theUser, theLesson));
    	lockTitleButton.setOnAction((_) -> {ControllerViewLesson.performTitleLock(theLesson); }); // TP2
    	lockDescriptionButton.setOnAction((_) -> {ControllerViewLesson.performDescriptionLock(theLesson); }); // TP2
    	saveCuratorInfoButton.setOnAction((_) -> {ControllerViewLesson.performSaveCuratorInfo(theLesson);});
    	
    	// ADD ALL THE UI COMPONENTS TO THE SCENE:

    	theRootPane.getChildren().addAll(topLabel, titleLabel, lessonsLabel, titleText, lessonText, category, categoryLabel, curatorInfoLabel, 
    			curatorInfoText, saveCuratorInfoButton, saveButton, returnButton, lockTitleButton, lockDescriptionButton, experienceButton);
	}
	
    // copy pasted again
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	
	
	protected static void setupComboBoxUI(ComboBox <Category> c, String ff, double f, double w,
			double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}
