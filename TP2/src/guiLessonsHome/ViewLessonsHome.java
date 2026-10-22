package guiLessonsHome;

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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.LessonsLearned;
import entityClasses.LessonsLearnedCollection;
import entityClasses.User;
import javafx.scene.control.TextArea;

/**
 * <p> Title: ViewLessonsHome </p>
 *
 * <p> Description: The JavaFX view for the Lessons Learned Home page. It lists the lessons
 * written by the current user and provides buttons to create, view/edit, and delete a lesson
 * or to return to the previous page. The class is a singleton-style view: its widgets are
 * static and the layout is built once, the first time the page is displayed.</p>
 *
 * @author who do I put here?
 * @version 2.0
 * @see ControllerLessonsHome
 */

public class ViewLessonsHome {
	
	// APPLICATION WINDOW DIMENSIONS:
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	// IMPORTANT VARIABLES FROM OTHER CLASSES:
	protected static User theUser;
	protected static ViewLessonsHome theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	// CORE javaFX stuff:
	protected static Stage theStage;
	protected static Scene theScene;
	protected static Pane theRootPane;
	
	// LABELS:
	protected static Label topLabel = new Label("Lessons Learned Home Page");
	protected static Label userLabel = new Label();
	protected static Label message = new Label();
	
	// BUTTONS:
	protected static Button createButton = new Button("Create Lesson");
	protected static Button viewButton = new Button("View Lesson");
	protected static Button deleteButton = new Button("Delete Lesson");
	protected static Button returnButton = new Button("Return");
	
	// ALERTS:
	protected static Alert deleteAlert = new Alert(AlertType.INFORMATION);
	protected static Alert deleteConfirm = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
	protected static Alert viewAlert = new Alert(AlertType.INFORMATION);
	
//	protected static Button logoutButton = new Button("Logout");
//	protected static Button quitButton = new Button("Quit");
	
	// LIST VIEW (TEMPORARY FOR NOW):
	protected static ListView<String> list = new ListView<String>(); 
	
	/**
	 * Displays the Lessons Learned Home page. The list is refreshed with the current user's
	 * lessons each time the page is shown, and a prompt is displayed when the user has not
	 * created any lessons yet.
	 *
	 * @param ps the JavaFX stage on which to show the page
	 * @param user the currently logged-in user
	 */
	public static void displayLessonsHome(Stage ps, User user) {
		if (theView == null) theView = new ViewLessonsHome();        // Instantiate singleton if needed
		theStage = ps;
		theUser = user;
		
		userLabel.setText("User: " + theUser.getUserName());
		
		// RETRIEVE LESSON COLLECTION BASED ON THE AUTHOR: (since contributors can only see their own work, the contributor name):
		LessonsLearnedCollection lessonsCollection;

		if (applicationMain.FoundationsMain.activeHomePage == 3) {
		    lessonsCollection = theDatabase.getAllLessons();
		}
		else {
		    lessonsCollection = theDatabase.getLessonByAuthor(theUser.getUserName());
		}
		
		fillList(list, lessonsCollection.getLessons());
		
		// if the list view is empty (user has not created anything), prompt a message:
		if (list.getItems().isEmpty()) {
			message.setText("Please create a lesson to see it in the list view.");
		}
		else {
			message.setText("");
		}
		
	   	theStage.setTitle("Lessons Learned Home");
    	theStage.setScene(theScene);
    	theStage.show();
	}
	
	private ViewLessonsHome() {
		// CONSTRUCTOR STUFF: 
    	theRootPane = new Pane();
    	theScene = new Scene(theRootPane, width, height);
    	
    	// LABEL UI:
    	setupLabelUI(topLabel, "Arial", 28, width, Pos.CENTER, 0, 15);
    	setupLabelUI(userLabel, "Arial", 16, width, Pos.CENTER, 0, 50);
    	setupLabelUI(message, "Arial", 16, width, Pos.CENTER, 0, 80);
    	
    	// BUTTON UI:
    	
    	setupButtonUI(createButton, "Dialog", 18, 150, Pos.CENTER, 25, 120);
    	setupButtonUI(viewButton, "Dialog", 18, 150, Pos.CENTER, 200, 120);
    	setupButtonUI(deleteButton, "Dialog", 18, 150, Pos.CENTER, 375, 120);
    	
    	// bottom buttons: 
    	setupButtonUI(returnButton, "Dialog", 18, 200, Pos.CENTER, 20, 540);
	
    	// SETUP THE LIST:
    	list.setPrefWidth(400);
    	list.setPrefHeight(320);
    	list.setLayoutX(150);
    	list.setLayoutY(180);
    	list.setFixedCellSize(28); 
    	
    	// ALERTS:
    	deleteAlert.setTitle("Lesson Deletion");
    	deleteAlert.setHeaderText("Deletion Result");
    	viewAlert.setTitle("View Lesson");
    	viewAlert.setTitle("Error: ");
    	
		// confirmation alert
		deleteConfirm.setTitle("Confirmation");
		deleteConfirm.setHeaderText("Are You Sure?");
    	
    	
    	// BUTTON ACTIONS:
    	createButton.setOnAction((_) -> {ControllerLessonsHome.goToCreation(theStage, theUser);}); // CREATE BUTTON:
    	viewButton.setOnAction((_) -> {ControllerLessonsHome.goToView(theStage, theUser	);}); // VIEW BUTTON:
    	deleteButton.setOnAction((_) -> {ControllerLessonsHome.performDeletion();}); // DELETE BUTTON:
    	returnButton.setOnAction((_) -> {ControllerLessonsHome.performReturn(); }); // RETURN TO HOME:

    	// ADD ALL THE UI COMPONENTS TO THE SCENE:
    	theRootPane.getChildren().addAll(topLabel, createButton, viewButton, deleteButton, returnButton, userLabel, message, list);
	}
	
	/**
	 * Replaces the contents of a list view with one row per lesson. Each row has the form
	 * {@code "[title] (ID: [id])"} so that the controller can recover the lesson ID from
	 * the selected row.
	 *
	 * @param list the list view to fill
	 * @param lessonsList the lessons to display
	 */
	protected static void fillList(ListView<String> list, List<LessonsLearned> lessonsList) {
		list.getItems().clear(); // clear everytime this method is refreshed (so it doesnt duplicate)
		for (int i = 0; i < lessonsList.size(); i++) {
			String title = lessonsList.get(i).getLessonTitle();
			String category = lessonsList.get(i).getCategory().name();
			String id = String.valueOf(lessonsList.get(i).getLessonID());
			
			// String row = String.format("%s / %s (ID: %s)", title, category, id);
			String row = String.format("%s / %s", title, category);
			
			// UI update: include author names if it's a curator. no need for contributors, since they can only see their own.
			if (theDatabase.isCurator(theUser.getUserName())) {
				row += String.format(" / by %s", lessonsList.get(i).getAuthor());
			}
			row += String.format(" (ID: %s)", id);
			list.getItems().add(row);
		}
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
