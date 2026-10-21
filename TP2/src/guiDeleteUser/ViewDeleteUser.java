package guiDeleteUser;

import javafx.scene.control.Label;	
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

import database.Database;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;
import guiAdminHome.ViewAdminHome;
import guiUserUpdate.ViewUserUpdate;

/**
 * Description: Controls the UI components for the DeleteUser page.
 */
public class ViewDeleteUser {
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	protected static User theUser;
	protected static ViewDeleteUser theView;

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	// CORE javaFX stuff:
	protected static Stage theStage;
	protected static Scene theScene;
	protected static Pane theRootPane;

	// LABELS:
	protected static Label topLabel = new Label("Delete a User Page");
	protected static Label descriptionLabel = new Label("Select or Search for a User to Delete");
	protected static Label filterLabel = new Label("Filter: ");
	
	// TEXT AREA:
	protected static TextField searchFilter = new TextField();
	
	// BUTTONS:
	protected static Button deleteButton = new Button("Delete User");
	protected static Button returnButton = new Button("Return");
	protected static Button viewButton = new Button("View User Details");
	
	// ALERTS:
	protected static Alert alertMsg = new Alert(AlertType.INFORMATION);
	protected static Alert resultMsg = new Alert(AlertType.INFORMATION);
	protected static Alert alertConfirmation = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
	
	// for the listUsers()
	protected static Alert alertNotImplemented = new Alert(AlertType.INFORMATION);
	
	// NEW LIST VIEW:
	// this makes it such there's only 1 string per row, but you can click on it.
	protected static ListView<String> list = new ListView<String>(); 
	
	/**
	 * Method: displayDeleteUser(Stage ps, User user)
	 * <p> Description: when called, it calls the constructor if it hasn't already.
	 * 					Then, it will display all the UI widgets.</p>
	 * @param ps the stage being passed in
	 * @param user the current User object
	 */
	public static void displayDeleteUser(Stage ps, User user) {
		if (theView == null) theView = new ViewDeleteUser();        // Instantiate singleton if needed
		theStage = ps;
		theUser = user;
		
		fillList(list, theDatabase.getUserList());
		list.getSelectionModel().clearSelection();
		
	   	theStage.setTitle("User Deletion");
    	theStage.setScene(theScene);
    	theStage.show();
	}
	
    
    // Constructor: 
    private ViewDeleteUser() {
    	// CONSTRUCTOR STUFF: 
    	theRootPane = new Pane();
    	theScene = new Scene(theRootPane, width, height);
    	
    	// ADJUSTING UI DESIGN:
    	// LABELS:
    	setupLabelUI(topLabel, "Arial", 28, width, Pos.CENTER, 0, 15);
    	setupLabelUI(descriptionLabel, "Arial", 20, width, Pos.CENTER, 0, 70);
    	setupLabelUI(filterLabel, "Arial", 16, width, Pos.CENTER_LEFT, 140, 130);
//    	
//    	// BUTTONS:
    	setupButtonUI(deleteButton, "Dialog", 16, 200, Pos.CENTER, 550, 125);
    	setupButtonUI(returnButton, "Dialog", 16, 200, Pos.BOTTOM_LEFT, 150, 540);
    	setupButtonUI(viewButton, "Dialog", 16, 200, Pos.BOTTOM_LEFT, 400, 540);
    	
		// ALERTS:
		alertMsg.setTitle("Deletion Status");
		alertMsg.setHeaderText("Deletion");
		
		// confirmation alert
		alertConfirmation.setTitle("Confirmation");
		alertConfirmation.setHeaderText("Are You Sure?");
    	
//    	// TEXT FIELD
    	setupTextUI(searchFilter, "Arial", 16, 300, Pos.CENTER_LEFT, 200, 125, true);
    	
    	// ADD LIST
    	list.setPrefWidth(400);
    	list.setPrefHeight(320);
    	list.setLayoutX(150);
    	list.setLayoutY(180);
    	list.setFixedCellSize(28); 
    	
    	// BUTTON ACTIONS:
    	deleteButton.setOnAction((_) -> {ControllerDeleteUser.performDeletion(theUser);});
    	returnButton.setOnAction((_) -> {ControllerDeleteUser.performReturn();});
    	viewButton.setOnAction((_) -> {ControllerDeleteUser.listUsers();});
    	
    	// SEE CHANGE IN TEXT FIELD:
    	searchFilter.setOnAction((_) -> {ControllerDeleteUser.filterList(list);});
    	
    	theRootPane.getChildren().addAll(topLabel, descriptionLabel, filterLabel, searchFilter, deleteButton, list, returnButton, viewButton);
    	
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
	
	protected static void fillList(ListView<String> list, List<String> data) {
		// Loop through userNames in Database
		for (String userName : data) {
			// If the list does not already contain that username & it's not the first one, add it: 
			if (!list.getItems().contains(userName) && !userName.equals("<Select a User>")) {
					list.getItems().add(userName);
				}
			}
		}
	
	/* ------
	 * TIANA CODE: TP1 for displayUsers
	   ------*/
	protected static void displayUserAccountsList(String listing, int numberOfAccounts) {
        Alert alertUserList = new Alert(AlertType.INFORMATION);
        alertUserList.setTitle("List All Users");
        alertUserList.setHeaderText("User Accounts (" + numberOfAccounts + ")");

        TextArea textArea = new TextArea(listing);
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(400);

        alertUserList.getDialogPane().setContent(textArea);
        alertUserList.setResizable(true);
        alertUserList.showAndWait();
    }
	
} // end of class