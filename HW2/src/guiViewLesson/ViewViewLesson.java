package guiViewLesson;

import database.Database;
import entityClasses.User;
import entityClasses.LessonsLearned;
import guiLessonsHome.ControllerLessonsHome;
import guiLessonsHome.ViewLessonsHome;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class ViewViewLesson {
	// APPLICATION WINDOW DIMENSIONS:
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// IMPORTANT VARIABLES FROM OTHER CLASSES:
	protected static User theUser;
	protected static ViewViewLesson theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	protected static LessonsLearned theLesson;
	
	// CORE javaFX stuff:
	protected static Stage theStage;
	protected static Scene theScene;
	protected static Pane theRootPane;
	
	// LABELS:
	protected static Label topLabel = new Label("View / Edit Lesson");
	protected static Label titleLabel = new Label("Title: ");
	protected static Label lessonsLabel = new Label("Lessons Learned: ");
	
	// BUTTONS:
	protected static Button saveButton = new Button("Save Edits");
	protected static Button returnButton = new Button("Return");
	
	// TEXT FIELDS:
	protected static TextField titleText = new TextField();
	protected static TextArea lessonText = new TextArea();
	
	// ALERTS:
	protected static Alert saveAlert = new Alert(AlertType.INFORMATION);
	protected static Alert saveConfirm = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
	
	public static void displayViewLesson(Stage ps, User user, LessonsLearned lesson) {
		if (theView == null) theView = new ViewViewLesson();        // Instantiate singleton if needed
		theStage = ps;
		theUser = user;
		theLesson = lesson; // theLesson variable will be used when passing into other args into controller.
		
		// automatically fill in the fields:
		titleText.setText(lesson.getLessonTitle());
		lessonText.setText(lesson.getLessonText());
		
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
    	setupLabelUI(titleLabel, "Arial", 18, 150, Pos.CENTER_RIGHT, 100, 62);
    	setupLabelUI(lessonsLabel, "Arial", 18, width, Pos.CENTER, 0, 105);

    	// BUTTON UI:
    	setupButtonUI(saveButton, "Dialog", 18, 150, Pos.CENTER, 240, 540);
    	
    	// bottom buttons: 
    	setupButtonUI(returnButton, "Dialog", 18, 200, Pos.CENTER, 20, 540);
    	
       	// TEXT FIELD UI:
    	setupTextUI(titleText, "Arial", 16, 400, Pos.CENTER_LEFT, 280, 60, true);
    	
    	// TEXT AREA UI:
    	lessonText.setWrapText(true);
        lessonText.setPrefWidth(500);
        lessonText.setPrefHeight(340);
    	lessonText.setLayoutX(150);
    	lessonText.setLayoutY(140);
    	
    	// ALERTS:
    	saveAlert.setTitle("Save Lesson Edits");
    	saveAlert.setHeaderText("Save Result");
    	
		// confirmation alert
		saveConfirm.setTitle("Confirmation");
		saveConfirm.setHeaderText("Are You Sure?");
    	
    	// BUTTON ACTIONS:		
    	saveButton.setOnAction((_) -> {ControllerViewLesson.performSave(theLesson, theUser);}); // CREATE BUTTON:
    	returnButton.setOnAction((_) -> {ControllerViewLesson.performReturn(); }); // RETURN TO HOME:

    	// ADD ALL THE UI COMPONENTS TO THE SCENE:
    	theRootPane.getChildren().addAll(topLabel, titleLabel, lessonsLabel, titleText, lessonText, saveButton, returnButton);
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
}
