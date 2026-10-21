/** 
 * The Foundations Application module.
 */
module FoundationsF26 {
	requires javafx.controls;
	requires java.sql;
	requires javafx.graphics;
	requires java.desktop;
	
	opens applicationMain to javafx.graphics, javafx.fxml;
}
