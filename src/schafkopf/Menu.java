package schafkopf;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class Menu extends HBox {
	
	private View view;
	private Button serverMenu;
	private Button connectMenu;

	public Menu(View view) {
		init(view);
	}

	public Menu(View view, double spacing) {
		super(spacing);
		init(view);
	}

	public Menu(View view, Node... children) {
		super(children);
		init(view);
	}

	public Menu(View view, double spacing, Node... children) {
		super(spacing, children);
		init(view);
	}
	
	private void init(View view) {
		this.view = view;
		
		this.setMaxSize(200, 80);
		this.setBackground(null);
		
		connectMenu = new Button("C");
		connectMenu.setPrefSize(40, 40);
		this.getChildren().add(connectMenu);
		HBox.setHgrow(connectMenu, Priority.SOMETIMES);
		HBox.setMargin(connectMenu, new Insets(5, 5, 5, 5));
	
		serverMenu = new Button("S");
		serverMenu.setPrefSize(40, 40);
		this.getChildren().add(serverMenu);
		HBox.setHgrow(serverMenu, Priority.SOMETIMES);
		HBox.setMargin(serverMenu, new Insets(5, 5, 5, 5));
	}

}
