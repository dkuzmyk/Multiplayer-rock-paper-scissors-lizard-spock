import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.*;

public class RPLS extends Application {
	// list fields
	ListView<String> p1Hand;
	ListView<String> serverMsg;


	// connections
	GameInfo serverConnection;

	// hash Map
	HashMap<String, Scene> sceneMap;

	// image handling
	Image background;
	ImageView bg;

	// using pane
	Pane serverLayer;

	Button startServer;
	Button quitServer;
	boolean checkWord;
	int port;
	int playerCount;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("RPLS Server");

		p1Hand = new ListView<String>();
		serverMsg = new ListView<String>();

		// using pane for this project
		serverLayer = new Pane();
		// setting scene 1, background
		background = new Image("file:src/main/resources/server1.jpg");
		bg = new ImageView();
		bg.setImage(background);
		serverLayer.getChildren().add(bg);

		// text field
		TextField portField = new TextField("5023");	// added default port for testing
		portField.setFocusTraversable(false);
		portField.setLayoutX(350);
		portField.setLayoutY(240);
		portField.setPrefSize(100, 40);
		serverLayer.getChildren().add(portField);

		// list view
		p1Hand.setPrefHeight(357);
		p1Hand.setPrefWidth(227);
		p1Hand.setLayoutX(502);
		p1Hand.setLayoutY(160);

		// server list
		serverMsg.setPrefHeight(357);
		serverMsg.setPrefWidth(227);
		serverMsg.setLayoutX(76);
		serverMsg.setLayoutY(160);


		// buttons
		// start the server
		startServer = new Button();
		startServer.setPadding(Insets.EMPTY);
		ImageView serverGraphic = new ImageView("file:src/main/resources/launchButton.png");
		serverGraphic.setFitWidth(120);
		serverGraphic.setFitHeight(40);
		startServer.setGraphic(serverGraphic);
		startServer.setLayoutX(340);
		startServer.setLayoutY(173);

		quitServer = new Button();
		quitServer.setPadding(Insets.EMPTY);
		ImageView serverGraphic2 = new ImageView("file:src/main/resources/exit.png");
		serverGraphic2.setFitWidth(120);
		serverGraphic2.setFitHeight(40);
		quitServer.setGraphic(serverGraphic2);
		quitServer.setLayoutX(340);
		quitServer.setLayoutY(173);


		Button connectButton = new Button();
		connectButton.setGraphic(new ImageView("file:src/main/resources/connectButton.png"));
		connectButton.setPadding(Insets.EMPTY);
		//connectButton.setPrefSize(100,50);
		connectButton.setLayoutY(290);
		connectButton.setLayoutX(310);

		connectButton.setOnAction(e->{

			port = Integer.parseInt(portField.getText());
			primaryStage.setScene(sceneMap.get("start"));

		});


		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("start",  startServerGui());
		//sceneMap.put("exit",  exitServerGui());


		serverLayer.getChildren().add(connectButton);
		Scene scene = new Scene(serverLayer,800,600);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	public Scene startServerGui(){
		Pane pane = new Pane();
		background = new Image("file:src/main/resources/server2.jpg");
		bg = new ImageView();
		bg.setImage(background);
		pane.getChildren().add(bg);
		pane.getChildren().add(p1Hand);
		pane.getChildren().add(serverMsg);
		pane.getChildren().add(startServer);

		startServer.setOnAction(start->{
			pane.getChildren().add(quitServer);
			quitServer.setOnAction(exit->{
				System.exit(0);
				Platform.exit();
			});

			serverConnection = new GameInfo(data->{
				Platform.runLater(()->{
					if(data.toString().contains("System")){serverMsg.getItems().add(data.toString());}
					else{p1Hand.getItems().add(data.toString());}
				});
			});
			serverConnection.changePort(port);						// assign the port from user input
			pane.getChildren().remove(startServer);
		});

		return new Scene(pane, 800,600);
	}

}
