package main;

import com.sun.javafx.css.CssError;
import com.sun.javafx.css.StyleManager;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import utility.Logger;

/**
 * <h1>Application entry point</h1>
 * Also holds root references to objects used within the applications
 */
public class ApplicationLoader extends Application {

	/**
	 * <h2>Development bool, CSS is reloaded on focus if true.</h2>
	 */
	public static boolean reloadOnFocus;

	/**
	 * <h2>The root GUI pane</h2>
	 */
	private ModuleSelectionToolRootPane view;

	/**
	 * <h2>The stage holding {@link ApplicationLoader#view}</h2>
	 */
	public static Stage mainStage = null;

	/**
	 * <h2>The main MVC controller</h2>
	 */
	public static ModuleSelectionToolController mainController = null;

	public Stage stage() {return mainStage;}

	@Override
	public void init() {
		//create view and model and pass their references to the controller
		view = new ModuleSelectionToolRootPane();
		StudentProfile model = new StudentProfile();
		mainController = new ModuleSelectionToolController(view, model);
	}

	@Override
	public void start(Stage stage) {
		// start

		//sets min width and height for the stage window
		mainStage = stage;
		stage.setMinWidth(800);
		stage.setMinHeight(700);


		stage.setTitle("Final Year Module Selection Tool");
		stage.setScene(new Scene(view));

		// Just an experiment. Yes, it could be transparent.
//		stage.initStyle(StageStyle.TRANSPARENT);
//		stage.getScene().setFill(Color.TRANSPARENT);

		stage.show();

		// Try to locate and apply CSS.
		StyleManager.errorsProperty().addListener(
			(ListChangeListener<? super CssError>) c -> {
				while(c.next()) {
					for(CssError error : c.getAddedSubList()) {
						Logger.AsyncAlert(Alert.AlertType.ERROR, "CSS error", "There was an error in styling the interface, most likely an issue with the stylesheet file : \n\n " + error.getMessage() +
								"\n\n Program will still run, but will look ugly :(");
						break;
					}
				}
		});
		setStylesheet(stage);


		// TODO Haven't tested on windows, but the "you've entered fullscreen" message when entering exclusive fullscreen is horribly broken. IDK why, yet another quiky bug of JavaFX.

		stage.setResizable(true);

		if (System.getProperty("os.name").equals("Mac OS X"))
			Logger.Alert(Alert.AlertType.INFORMATION, "Resizing, Fullscreen, and Mac", "You're on Mac OS. There's a known mac specific JavaFX bug which prevents resizing after fullscreen, so the window won't be auto-fullscreened " +
					"\n\n The programme was built to be used in fullscreen, and the option is available in the 'view' menu.");
		else
			stage.setFullScreen(true);

		Logger.CheckAsyncAlert();
		stage.focusedProperty().addListener((observable, oldValue, newValue) -> ApplicationLoader.onFocus());
	}

	/**
	 * <h2>Application has recieved focus, reloads css</h2>
	 */
	public static void onFocus(){
		if (reloadOnFocus)
			setStylesheet();
	}

	/**
	 * <h2>Enables / disables fullscreen</h2>
	 */
	public static void toggleFS(){
		mainStage.setFullScreen(!mainStage.isFullScreen());
	}


	/**
	 * <h2>Enables / disables CSS</h2>
	 */
	public static void toggleCSS() {
		if (mainStage.getScene().getStylesheets().size() != 0)
			clearStylesheet();
		else
			setStylesheet();
	}

	/**
	 * <h2>Removes stylesheet</h2>
	 */
	private static void clearStylesheet() {
		Logger.New("Clearing stylesheet");
		mainStage.getScene().getStylesheets().clear();
		mainStage.getScene().getStylesheets().add("blank.css"); // There was some funky refusal to reload / clear css classes. This fixed that, idk why.
		mainStage.getScene().getStylesheets().clear();
	}


	/**
	 * <h2>Clears and reloads stylesheet</h2>
	 */
	public static void setStylesheet(){
		clearStylesheet();
		setStylesheet(mainStage);
	}


	/**
	 * <h2>Sets the default stylesheet</h2>
	 */
	public static void setStylesheet(Stage stage){
		Logger.New("Parsing stylesheet");
		stage.getScene().getStylesheets().clear();
		stage.getScene().getStylesheets().add("stylesheet.css");
		Logger.CheckAsyncAlert();
	}

	/**
	 * Main entry point.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}