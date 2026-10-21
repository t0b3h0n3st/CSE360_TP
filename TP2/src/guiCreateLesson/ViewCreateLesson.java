package guiCreateLesson;

import java.util.ArrayList;			
import java.util.List;

import javafx.collections.FXCollections;
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
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiAdminHome.ControllerAdminHome;
import guiDeleteUser.ControllerDeleteUser;
import guiDeleteUser.ViewDeleteUser;
import guiLessonsHome.ControllerLessonsHome;
import guiLessonsHome.ViewLessonsHome;
import guiUserUpdate.ViewUserUpdate;
import javafx.scene.control.TextArea;
	
/**
 * <p> Title: ViewCreateLesson </p>
 *
 * <p> Description: The JavaFX view for the Create Lesson page. It presents a title field, a
 * text area for the lesson, a Confirm button, and a button to return to the Lessons Learned
 * Home page. The widgets are static and the layout is built once, the first time the page is
 * displayed.</p>
 *
 * @author who do I put here?
 * @version 2.0
 * @see ControllerCreateLesson
 * @see ModelCreateLesson
 */

public class ViewCreateLesson {
	
	// APPLICATION WINDOW DIMENSIONS:
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
		
	// IMPORTANT VARIABLES FROM OTHER CLASSES:
	protected static User theUser;
	protected static ViewCreateLesson theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
		
	// CORE javaFX stuff:
	protected static Stage theStage;
	protected static Scene theScene;
	protected static Pane theRootPane;
	
	// LABELS:
	protected static Label topLabel = new Label("Create a New Lesson");
	protected static Label titleLabel = new Label("Enter your title: ");
	protected static Label lessonsLabel = new Label("Lessons Learned: ");
	
	// BUTTONS:
	protected static Button confirmButton = new Button("Confirm");
	protected static Button returnButton = new Button("Return to Lessons Learned Home Page");
	
	// TEXT:
	protected static TextField lessonTitle = new TextField();
	protected static TextArea lessonText = new TextArea();
	
	// ALERTS:
	protected static Alert alertMsg = new Alert(AlertType.INFORMATION);
	protected static Alert resultMsg = new Alert(AlertType.INFORMATION);
	protected static Alert alertConfirmation = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
	
	// DISPLAYING THE STAGE WITH THE NEW SCENE:
		/**
		 * Displays the Create Lesson page on the given stage.
		 *
		 * @param ps the JavaFX stage on which to show the page
		 * @param user the currently logged-in user, who will be the author of the new lesson
		 */
	
	public static void displayCreateLesson(Stage ps, User user) {
		if (theView == null) theView = new ViewCreateLesson();        // Instantiate singleton if needed
		theStage = ps;
		theUser = user;

		theStage.setTitle("Lessons Learned Home");
	    theStage.setScene(theScene);
	    theStage.show();
	}
	
	// PRIVATE CONSTRUCTOR FOR SETTING UP UI COMPONENTS:
	private ViewCreateLesson() {
		
		// CONSTRUCTOR STUFF: 
    	theRootPane = new Pane();
    	theScene = new Scene(theRootPane, width, height);
    	
    	// LABEL UI:
    	setupLabelUI(topLabel, "Arial", 28, width, Pos.CENTER, 0, 15);
    	setupLabelUI(titleLabel, "Arial", 18, 150, Pos.CENTER_RIGHT, 100, 62);
    	setupLabelUI(lessonsLabel, "Arial", 18, width, Pos.CENTER, 0, 105);
    	
    	// TEXT FIELD UI:
    	setupTextUI(lessonTitle, "Arial", 16, 400, Pos.CENTER_LEFT, 280, 60, true);
    	
    	
    	// TEXT AREA UI:
    	lessonText.setPromptText("Write your lessons learned below:");
    	lessonText.setWrapText(true);
        lessonText.setPrefWidth(500);
        lessonText.setPrefHeight(340);
    	lessonText.setLayoutX(150);
    	lessonText.setLayoutY(140);
    	
    	// BUTTON UI:
    	setupButtonUI(confirmButton, "Dialog", 18, 100, Pos.CENTER, 450, 540);
    	setupButtonUI(returnButton, "Dialog", 18, 250, Pos.CENTER, 25, 540);
    	
		// ALERTS:
		alertMsg.setTitle("Error");
		resultMsg.setTitle("Success");
		
		// confirmation alert
		alertConfirmation.setTitle("Confirmation");
		alertConfirmation.setHeaderText("Are You Sure?");
    	
    	// BUTTON ACTIONS:
    	confirmButton.setOnAction((_) -> {ControllerCreateLesson.confirmCreation(theUser);}); // CREATE BUTTON:
    	returnButton.setOnAction((_) -> {ControllerCreateLesson.performReturn(); }); // RETURN TO HOME:
    	
    	// ADD ALL THE UI COMPONENTS TO THE SCENE:
    	theRootPane.getChildren().addAll(topLabel, titleLabel, lessonsLabel, confirmButton, returnButton, lessonTitle, lessonText);
	}
	
	
    /* ------------------------------------
     * COPY PASTED UI SETUP HELPER METHODS:
       ------------------------------------ */
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
		
}
