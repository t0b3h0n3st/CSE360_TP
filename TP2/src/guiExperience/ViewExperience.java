package guiExperience;

import entityClasses.LessonExperience;
import entityClasses.LessonsLearned;
import entityClasses.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewExperience </p>
 * <p> Description: Contributor page for appending a real attempt and reading its history.
 * Separate What/How/Elapsed/Member effort fields come from the Contributor experience story.
 * A fresh page per visit prevents stale input from crossing accounts or lessons.</p>
 * @author Huichang
 */
public final class ViewExperience {
	/** Model contains the fixed identity and lesson context, never an editable owner. */
	protected static ModelExperience theModel;
	/** History and details display saved attempts separately from the new-attempt editor. */
	protected static ListView<LessonExperience> history;
	/** Read-only full content of the selected saved attempt. */
	protected static TextArea details;
	/** What/how text and decimal minutes satisfy the four experience-information requirements. */
	protected static TextArea whatDone, howDone, effort;
	/** Decimal elapsed minutes from the experience story, separate from person-minutes. */
	protected static TextField elapsed;
	/** Context-specific feedback stays on screen, retaining invalid input for correction. */
	protected static Label status;
	/** Pages are constructed in displayExperience rather than reused across identities. */
	private ViewExperience() { }

	/**
	 * Opens a fresh experience page using the existing application database.
	 * @param stage application stage
	 * @param user signed-in user
	 * @param lesson existing selected lesson
	 */
	public static void displayExperience(Stage stage, User user, LessonsLearned lesson) {
		theModel = new ModelExperience(applicationMain.FoundationsMain.database,
				user.getUserName(), lesson.getLessonID());
		history = new ListView<>();
		history.setPlaceholder(new Label("No experience recorded yet."));
		history.setPrefHeight(130);
		history.getSelectionModel().selectedItemProperty().addListener(
				(_, _, selected) -> ControllerExperience.show(selected));
		details = area();
		details.setEditable(false);
		details.setPrefRowCount(9);
		Button refresh = new Button("Refresh History");
		refresh.setOnAction((_) -> ControllerExperience.refresh());
		VBox saved = new VBox(8, new Label("Saved attempts"), history, refresh, details);
		saved.setPrefWidth(300);
		VBox.setVgrow(details, Priority.ALWAYS);

		whatDone = area();
		howDone = area();
		elapsed = new TextField();
		elapsed.setPromptText("Elapsed minutes, e.g. 30 or 30.50");
		effort = area();
		effort.setPromptText("Alex: 20\nSam: 10");
		Button add = new Button("Add Experience");
		add.setOnAction((_) -> ControllerExperience.add());
		Button clear = new Button("Clear New Attempt");
		clear.setOnAction((_) -> clear());
		VBox editor = new VBox(6, new Label("New attempt — all fields required"),
				new Label("What was done (max 10,000)"), whatDone,
				new Label("How it was done (max 10,000)"), howDone,
				new Label("Elapsed time in minutes (greater than zero)"), elapsed,
				new Label("Member effort: Name: minutes, one member per line (max 10,000)"), effort,
				new Label("Minutes allow two decimals; member effort may be zero."), new HBox(8, add, clear));
		HBox content = new HBox(12, saved, editor);
		HBox.setHgrow(editor, Priority.ALWAYS);
		ScrollPane scroll = new ScrollPane(content);
		scroll.setFitToWidth(true);
		status = new Label();
		status.setWrapText(true);
		Button back = new Button("Return to Lesson");
		back.setOnAction((_) -> guiViewLesson.ViewViewLesson.displayViewLesson(stage, user, lesson));
		BorderPane root = new BorderPane(scroll);
		root.setPadding(new Insets(12));
		root.setTop(new Label("Experience Information | Lesson " + lesson.getLessonID()
				+ ": " + lesson.getLessonTitle()));
		root.setBottom(new VBox(8, status, back));
		stage.setTitle("Lesson Experience Information");
		stage.setScene(new Scene(root, applicationMain.FoundationsMain.WINDOW_WIDTH,
				applicationMain.FoundationsMain.WINDOW_HEIGHT));
		ControllerExperience.refresh();
		stage.show();
	}

	/** Creates a wrapping input area consistent across the experience fields. */
	private static TextArea area() {
		TextArea area = new TextArea();
		area.setWrapText(true);
		area.setPrefRowCount(3);
		return area;
	}

	/** Clears only the new-attempt inputs; saved attempts remain intact. */
	protected static void clear() {
		whatDone.clear();
		howDone.clear();
		elapsed.clear();
		effort.clear();
		status.setText("Enter the next attempt's experience information.");
	}
}
