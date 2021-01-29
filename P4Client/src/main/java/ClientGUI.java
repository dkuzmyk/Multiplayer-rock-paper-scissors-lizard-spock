import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.sun.media.jfxmedia.events.PlayerEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.*;

/*
 * Class that handles the client GUI and updates it based on the state of the game.
 */
public class ClientGUI extends Application {
        Pane clientLayer;
        Pane lobbyLayer;
        Pane gameLayer;

        Scene scene1;
        Scene scene2;
        Scene scene3;

        static Client c;
        static final int picHeight = 126;
        static final int picWidth = 126;

        // textfields
        TextField winScore;
        TextField lossScore;

        TextField winScore2;
        TextField lossScore2;

        // points
        int wins;
        int losses;

        // images for ui
        Image background;
        ImageView bg;

        Image plain;
        ImageView pln;

        Image waiting;
        ImageView wtg;

        Image myHandChoice;
        ImageView myHand;

        Image textResult;
        ImageView tr;

        ListView clientConsole2;


        // Disables the player's buttons
        public static void disableButtons(Button a, Button b, Button c, Button d, Button e){
          a.setDisable(true);
          b.setDisable(true);
          c.setDisable(true);
          d.setDisable(true);
          e.setDisable(true);
        }

        // Sees if the proposed challenge is valid
        synchronized public static void challengePlayer(Button b, Integer i, ListView console){
          // Can't challenge yourself or a unable player
          if((c.id.equals(i)) || (c.game.playerList.get(i - 1) != 1)){
            console.getItems().add("Can't challenge this player");
            return;
          }   
 
          c.sendChallenge(i);
          b.setDisable(true);

          return;
        }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("RPSLS Client");

        clientLayer = new Pane();
        background = new Image("file:src/main/resources/loginScreen.png");
        bg = new ImageView();
        bg.setImage(background);
        clientLayer.getChildren().add(bg);

        lobbyLayer = new Pane();
        // image plain
        plain = new Image("file:src/main/resources/plain.png");
        pln = new ImageView();
        pln.setImage(plain);
        lobbyLayer.getChildren().add(pln);

        // win/loss images
        textResult = new Image("empty.png");
        tr = new ImageView();
        tr.setLayoutX(280);
        tr.setLayoutY(400);
        tr.setImage(textResult);

        // textfield for scores
        winScore = new TextField();
        winScore.setPrefSize(80,50);
        winScore.setText(""+wins);
        winScore.setDisable(true);

        lossScore = new TextField();
        lossScore.setPrefSize(80,50);
        lossScore.setText(""+losses);
        lossScore.setDisable(true);

        winScore.setLayoutX(330);
        winScore.setLayoutY(150);
        lossScore.setLayoutX(470);
        lossScore.setLayoutY(150);

        // textfield for scores for game interface
        winScore2 = new TextField();
        winScore2.setPrefSize(150,50);
        winScore2.setText(""+wins);
        winScore2.setDisable(true);

        lossScore2 = new TextField();
        lossScore2.setPrefSize(150,50);
        lossScore2.setText(""+losses);
        lossScore2.setDisable(true);

        winScore2.setLayoutX(150);
        winScore2.setLayoutY(335);
        lossScore2.setLayoutX(150);
        lossScore2.setLayoutY(480);


        //Scene 3 GUI + functionality
        Image i1 = new Image("rock.png");
        Image i2 = new Image("paper.png");
        Image i3 = new Image("scissor.png");
        Image i4 = new Image("lizard.png");
        Image i5 = new Image("spock.png");

        ImageView v = new ImageView(i1);
        v.setFitHeight(picHeight);
        v.setFitWidth(picWidth);
        v.setPreserveRatio(true);

        ImageView v2 = new ImageView(i2);
        v2.setFitHeight(picHeight);
        v2.setFitWidth(picWidth);
        v2.setPreserveRatio(true);

        ImageView v3 = new ImageView(i3);
        v3.setFitHeight(picHeight);
        v3.setFitWidth(picWidth);
        v3.setPreserveRatio(true);

        ImageView v4 = new ImageView(i4);
        v4.setFitHeight(picHeight);
        v4.setFitWidth(picWidth);
        v4.setPreserveRatio(true);

        ImageView v5 = new ImageView(i5);
        v5.setFitHeight(picHeight);
        v5.setFitWidth(picWidth);
        v5.setPreserveRatio(true);

        myHandChoice  = new Image("empty.png");
        myHand = new ImageView();
        myHand.setFitWidth(182);
        myHand.setFitHeight(182);
        myHand.setLayoutX(361);
        myHand.setLayoutY(448);
        myHand.setImage(myHandChoice);

        Button b1 = new Button();
        Button b2 = new Button();
        Button b3 = new Button();
        Button b4 = new Button();
        Button b5 = new Button();

        Button exitButton = new Button();
        exitButton.setGraphic(new ImageView("file:src/main/resources/exitButton2.png"));
        exitButton.setPadding(Insets.EMPTY);
        exitButton.setLayoutX(310);
        exitButton.setLayoutY(500);
        exitButton.setOnAction(leaveGame->{
                System.exit(0);
                Platform.exit();
        });

        Button exitButton2 = new Button();
        exitButton2.setGraphic(new ImageView("file:src/main/resources/exitButton2.png"));
        exitButton2.setPadding(Insets.EMPTY);
        exitButton2.setLayoutX(610);
        exitButton2.setLayoutY(160);
        exitButton2.setOnAction(leaveGame->{
             System.exit(0);
             Platform.exit();
        });

        b1.setGraphic(v);
        b2.setGraphic(v2);
        b3.setGraphic(v3);
        b4.setGraphic(v4);
        b5.setGraphic(v5);

        b1.setOnAction(e->{
                c.sendMove("Rock"); disableButtons(b1, b2, b3, b4, b5);
                myHandChoice  = new Image("rock.png");
                myHand.setImage(myHandChoice);
        });
            b1.setPadding(Insets.EMPTY);
            b1.setLayoutX(110);
            b1.setLayoutY(660);

        b2.setOnAction(e->{
                c.sendMove("Paper"); disableButtons(b1, b2, b3, b4, b5);
                myHandChoice  = new Image("paper.png");
                myHand.setImage(myHandChoice);
        });
            b2.setPadding(Insets.EMPTY);
            b2.setLayoutX(250);
            b2.setLayoutY(660);

        b3.setOnAction(e->{
                c.sendMove("Scissors"); disableButtons(b1, b2, b3, b4, b5);
                myHandChoice  = new Image("scissor.png");
                myHand.setImage(myHandChoice);
        });
            b3.setPadding(Insets.EMPTY);
            b3.setLayoutX(390);
            b3.setLayoutY(660);

        b4.setOnAction(e->{
                c.sendMove("Lizard"); disableButtons(b1, b2, b3, b4, b5);
                myHandChoice  = new Image("lizard.png");
                myHand.setImage(myHandChoice);
        });
            b4.setPadding(Insets.EMPTY);
            b4.setLayoutX(528);
            b4.setLayoutY(660);

        b5.setOnAction(e->{
                c.sendMove("Spock"); disableButtons(b1, b2, b3, b4, b5);
                myHandChoice  = new Image("spock.png");
                myHand.setImage(myHandChoice);
        });
            b5.setPadding(Insets.EMPTY);
            b5.setLayoutX(665);
            b5.setLayoutY(660);

        // list view for server messages / players
        ListView clientConsole = new ListView();
        clientConsole.setPrefHeight(230);
        clientConsole.setPrefWidth(235);
        clientConsole.setLayoutX(323);
        clientConsole.setLayoutY(237);

        clientConsole2 = new ListView();
        clientConsole2.setPrefHeight(275);
        clientConsole2.setPrefWidth(190);
        clientConsole2.setLayoutX(576);
        clientConsole2.setLayoutY(340);

        waiting = new Image("file:src/main/resources/selectOpponent.png");
        wtg = new ImageView();
        wtg.setImage(waiting);
        wtg.setLayoutX(130);
        wtg.setLayoutY(25);

        // game gui
        gameLayer = new Pane();
        background = new Image("file:src/main/resources/GameBG.png");
        bg = new ImageView();
        bg.setImage(background);
        gameLayer.getChildren().add(bg);
        //gameLayer.getChildren().add(pHand);

        gameLayer.getChildren().addAll(b1,b2,b3,b4,b5);
        gameLayer.getChildren().addAll(winScore2, lossScore2, exitButton2);
        gameLayer.getChildren().add(clientConsole2);
        gameLayer.getChildren().add(myHand);
        gameLayer.getChildren().add(tr);

        ImageView v6 = new ImageView();
        v6.setFitHeight(182);
        v6.setFitWidth(182);
        v6.setLayoutX(360);
        v6.setLayoutY(223);
        gameLayer.getChildren().add(v6);

        // rename to scene3
        scene3 = new Scene(gameLayer, 900, 900);

        // Scene 2 GUI + functionality // lobby
        ListView playerList = new ListView();
        playerList.setPrefHeight(340);
        playerList.setPrefWidth(235);
        playerList.setLayoutX(50);
        playerList.setLayoutY(100);

        TextField chooseID = new TextField("Choose a number to challenge");
        chooseID.setPrefWidth(235);
        chooseID.setLayoutX(50);
        chooseID.setLayoutY(445);

        Button challenge = new Button("");
        challenge.setGraphic(new ImageView("file:src/main/resources/challenge.png"));
        challenge.setPadding(Insets.EMPTY);
        challenge.setLayoutX(100);
        challenge.setLayoutY(500);

        challenge.setOnAction(e->{challengePlayer(challenge, Integer.parseInt(chooseID.getText()), clientConsole);});

        lobbyLayer.getChildren().addAll(chooseID, challenge, playerList, clientConsole, exitButton, wtg);
        lobbyLayer.getChildren().addAll(winScore, lossScore);
        scene2 = new Scene(lobbyLayer, 600, 600);


        // Starting scene GUI + functionality // login
        TextField portField = new TextField("5023");
        portField.setFocusTraversable(false);
        portField.setLayoutX(400);
        portField.setLayoutY(300);
        portField.setPrefSize(150, 40);

        TextField ipField = new TextField("127.0.0.1");
        ipField.setFocusTraversable(false);
        ipField.setLayoutX(400);
        ipField.setLayoutY(380);
        ipField.setPrefSize(150, 40);

        Button startClient = new Button("");
        startClient.setGraphic(new ImageView("file:src/main/resources/connectButton.png"));
        startClient.setPadding(Insets.EMPTY);
        startClient.setLayoutY(450);
        startClient.setLayoutX(360);

        clientLayer.getChildren().addAll(ipField, portField, startClient);
        scene1 = new Scene(clientLayer,900,900);        // scene ///////////////////////////////

        startClient.setOnAction(e->{c = new Client(Integer.parseInt(portField.getText()), ipField.getText(), b1, b2,
                                    b3, b4, b5, challenge, playerList, clientConsole, v6, scene2, scene3, primaryStage,
                                        clientConsole2, myHand, tr, winScore, lossScore, winScore2, lossScore2);
                                    c.start(); primaryStage.setScene(scene2);});



        primaryStage.setScene(scene1);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
	}

        private void repositionPoints() {

        }

} 
