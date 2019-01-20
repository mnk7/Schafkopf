package schafkopf;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

// Stackpane -> stapelt Elemente aufeinander
public class Main extends Application {
	
	/*
	 * Aufbau der Graphik:
	 * 
	 *  --------------------
	 * | B_C B_S |      L_S | -> Kontrollknöpfe auf eigenem Pane; werden mit
	 * |---------           |    Stack-Layout aufgesetzt (.setAlignment(Pane, Pos.TOP_LEFT))
	 * |                    |
	 * |                    |
	 * |                    | -> L_S und Nachrichten-knöpfe werden per Stackpane auf 
	 * |                    |    die Spielerhand gesetzt.
	 * |                    |    Setze Ränder: .setMargin(element, new Insets(1, 1, 1, 1))
	 * |                    |
	 * |                    |
	 * |                    |
	 * |                    |
	 *  --------------------
	 */
	
	@Override
	public void start(Stage primaryStage) {
		try {
			StackPane root = new StackPane();
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			setUserAgentStylesheet(STYLESHEET_MODENA);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Schafkopf");
			
			SpielUI mainui = new SpielUI(6);
			root.getChildren().add(mainui);
			
			Menu menu = new Menu(mainui);
			root.getChildren().add(menu);
			StackPane.setAlignment(menu, Pos.TOP_LEFT);
			
			primaryStage.setMinWidth(400);
			primaryStage.setMinHeight(450);
			primaryStage.getIcons().add(new Image(
					Main.class.getResourceAsStream("/assets/Logo.gif")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
