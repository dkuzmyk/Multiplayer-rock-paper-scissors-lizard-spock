import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
/*
 * Class that handles the server GUI and updates it based on the state of the game.
 */
public class ServerGUI extends Application {

    // listView
    ListView console;
    ListView playerList;

    // scene map
    HashMap<String, Scene> sceneMap;

    // image handling
    Image background;
    ImageView bg;

    // using pane
    Pane serverLayer;

    Button startServer;
    Button quitServer;

    Server s;

    TextField portField;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("RPSLS Server");
		
        // Scene 2 GUI + functionality
        console = new ListView();
        playerList = new ListView();

        serverLayer = new Pane();
        // setting scene 1, background
        background = new Image("file:src/main/resources/server1.jpg");
        bg = new ImageView();
        bg.setImage(background);
        serverLayer.getChildren().add(bg);

        // text field
        portField = new TextField("5023");	// added default port for testing
        portField.setFocusTraversable(false);
        portField.setLayoutX(350);
        portField.setLayoutY(240);
        portField.setPrefSize(100, 40);
        serverLayer.getChildren().add(portField);

        // list view
        playerList.setPrefHeight(357);
        playerList.setPrefWidth(227);
        playerList.setLayoutX(502);
        playerList.setLayoutY(160);

        // server list
        console.setPrefHeight(357);
        console.setPrefWidth(227);
        console.setLayoutX(76);
        console.setLayoutY(160);

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
        connectButton.setLayoutY(290);
        connectButton.setLayoutX(310);

        // button to switch scene
        connectButton.setOnAction(e->{
            primaryStage.setScene(sceneMap.get("start"));
        });

        sceneMap = new HashMap<String, Scene>();
        sceneMap.put("start",  startServerGui());

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
        pane.getChildren().add(playerList);
        pane.getChildren().add(console);
        pane.getChildren().add(startServer);

        startServer.setOnAction(start->{
            pane.getChildren().add(quitServer);
            quitServer.setOnAction(exit->{
                System.exit(0);
                Platform.exit();
            });
            s = new Server(console, playerList, Integer.parseInt(portField.getText()));
            pane.getChildren().remove(startServer);
        });

        return new Scene(pane, 800,600);
    }

}
