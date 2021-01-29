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

public class RPLS extends Application {
	Pane clientLayer;
	String ip;
	int port;

	boolean arePlayers = false;
	boolean restartGame = false;
	boolean restartGame2 = false;
	boolean player1 = false;
	boolean player2 = false;
	boolean moved = false;

	boolean p1Ready=false;
	boolean p2Ready=false;

	boolean p1stop=false;
	boolean p2stop=false;

	// images for ui
	Image background;
	ImageView bg;
	Image waiting;
	ImageView wtg;
	Image plain;
	ImageView pln;

	// images for hands
	Image rockPic;
	ImageView rockp;
	Image paperPic;
	ImageView paperp;
	Image scissorsPic;
	ImageView scissorsp;
	Image lizardPic;
	ImageView lizardp;
	Image spockPic;
	ImageView spockp;

	// images for opponent hand
	Image OrockPic;
	ImageView Orockp;
	Image OpaperPic;
	ImageView Opaperp;
	Image OscissorsPic;
	ImageView Oscissorsp;
	Image OlizardPic;
	ImageView Olizardp;
	Image OspockPic;
	ImageView Ospockp;

	Button rock;
	Button paper;
	Button scissor;
	Button lizard;
	Button spock;

	Button quitGameButton;
	Button quitGameButton2;
	Button yesButton;
	Button noButton;
	Image youWin;
	ImageView yw;
	Image youLose;
	ImageView yl;
	Image exitScene;
	ImageView exs;

	Button yesButton2;
	Button noButton2;
	Image youWin2;
	ImageView yw2;
	Image youLose2;
	ImageView yl2;
	Image exitScene2;
	ImageView exs2;

	ListView<String> pHand;

	// using pane
	Pane pane;
	Pane disconnectedPane;
	Pane exitPaneWin;
	Pane exitPaneLose;

	TextField pScore;
	TextField oScore;

	HashMap<String, Scene> sceneMap;

	Client client;
	int myPoints;
	int opponentPoints;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("RPLS Client");
		// add background
		clientLayer = new Pane();
		background = new Image("file:src/main/resources/loginScreen.png");
		bg = new ImageView();
		bg.setImage(background);
		clientLayer.getChildren().add(bg);

		pHand = new ListView<String>();			// new view list log sheet

		// textfield for scores
		pScore = new TextField();
		pScore.setPrefSize(150,50);
		pScore.setLayoutX(150);
		pScore.setLayoutY(335);
		pScore.setText(""+myPoints);
		pScore.setDisable(true);

		oScore = new TextField();
		oScore.setPrefSize(150,50);
		oScore.setLayoutX(150);
		oScore.setLayoutY(480);
		oScore.setText(""+opponentPoints);
		oScore.setDisable(true);

		rock = new Button("");
		paper = new Button("");
		scissor = new Button("");
		lizard = new Button("");
		spock = new Button("");

		// textfield for port
		TextField portField = new TextField("5023");
		portField.setFocusTraversable(false);
		portField.setLayoutX(400);
		portField.setLayoutY(300);
		portField.setPrefSize(150, 40);
		clientLayer.getChildren().add(portField);

		// textfield for ip
		TextField ipField = new TextField("127.0.0.1");
		ipField.setFocusTraversable(false);
		ipField.setLayoutX(400);
		ipField.setLayoutY(380);
		ipField.setPrefSize(150, 40);
		clientLayer.getChildren().add(ipField);

		// image waiting
		waiting = new Image("file:src/main/resources/waitingOpponent.png");
		wtg = new ImageView();
		wtg.setImage(waiting);
		wtg.setLayoutX(200);
		wtg.setLayoutY(400);

		// image plain
		plain = new Image("file:src/main/resources/plain.png");
		pln = new ImageView();
		pln.setImage(plain);

		// image of hands =================
		rockPic  = new Image("file:src/main/resources/rock.png");
		rockp = new ImageView();
		rockp.setFitWidth(182);
		rockp.setFitHeight(182);
		rockp.setLayoutX(360);
		rockp.setLayoutY(447);
		rockp.setImage(rockPic);

		paperPic  = new Image("file:src/main/resources/paper.png");
		paperp = new ImageView();
		paperp.setFitWidth(182);
		paperp.setFitHeight(182);
		paperp.setLayoutX(360);
		paperp.setLayoutY(447);
		paperp.setImage(paperPic);

		scissorsPic  = new Image("file:src/main/resources/scissor.png");
		scissorsp = new ImageView();
		scissorsp.setFitWidth(182);
		scissorsp.setFitHeight(182);
		scissorsp.setLayoutX(360);
		scissorsp.setLayoutY(447);
		scissorsp.setImage(scissorsPic);

		lizardPic  = new Image("file:src/main/resources/lizard.png");
		lizardp = new ImageView();
		lizardp.setFitWidth(182);
		lizardp.setFitHeight(182);
		lizardp.setLayoutX(360);
		lizardp.setLayoutY(447);
		lizardp.setImage(lizardPic);

		spockPic  = new Image("file:src/main/resources/spock.png");
		spockp = new ImageView();
		spockp.setFitWidth(182);
		spockp.setFitHeight(182);
		spockp.setLayoutX(361);
		spockp.setLayoutY(448);
		spockp.setImage(spockPic);

		// opponent hand  ===============
		OrockPic  = new Image("file:src/main/resources/rock.png");
		Orockp = new ImageView();
		Orockp.setFitWidth(182);
		Orockp.setFitHeight(182);
		Orockp.setLayoutX(360);
		Orockp.setLayoutY(223);
		Orockp.setImage(OrockPic);

		OpaperPic  = new Image("file:src/main/resources/paper.png");
		Opaperp = new ImageView();
		Opaperp.setFitWidth(182);
		Opaperp.setFitHeight(182);
		Opaperp.setLayoutX(360);
		Opaperp.setLayoutY(223);
		Opaperp.setImage(OpaperPic);

		OscissorsPic  = new Image("file:src/main/resources/scissor.png");
		Oscissorsp = new ImageView();
		Oscissorsp.setFitWidth(182);
		Oscissorsp.setFitHeight(182);
		Oscissorsp.setLayoutX(360);
		Oscissorsp.setLayoutY(223);
		Oscissorsp.setImage(OscissorsPic);

		OlizardPic  = new Image("file:src/main/resources/lizard.png");
		Olizardp = new ImageView();
		Olizardp.setFitWidth(182);
		Olizardp.setFitHeight(182);
		Olizardp.setLayoutX(360);
		Olizardp.setLayoutY(223);
		Olizardp.setImage(OlizardPic);

		OspockPic  = new Image("file:src/main/resources/spock.png");
		Ospockp = new ImageView();
		Ospockp.setFitWidth(182);
		Ospockp.setFitHeight(182);
		Ospockp.setLayoutX(361);
		Ospockp.setLayoutY(224);
		Ospockp.setImage(OspockPic);

		// listview
		pHand.setPrefHeight(275);
		pHand.setPrefWidth(190);
		pHand.setLayoutX(576);
		pHand.setLayoutY(340);

		// exit scene
		yesButton = new Button();
		yesButton.setGraphic(new ImageView("file:src/main/resources/yesButton.png"));
		yesButton.setPadding(Insets.EMPTY);
		yesButton.setLayoutX(270);
		yesButton.setLayoutY(400);

		noButton = new Button();
		noButton.setGraphic(new ImageView("file:src/main/resources/noButton.png"));
		noButton.setPadding(Insets.EMPTY);
		noButton.setLayoutX(470);
		noButton.setLayoutY(400);

		youWin = new Image("file:src/main/resources/youWinText.png");
		yw = new ImageView();
		yw.setLayoutX(280);
		yw.setLayoutY(150);
		yw.setImage(youWin);

		youLose = new Image("file:src/main/resources/youLoseText.png");
		yl = new ImageView();
		yl.setLayoutX(280);
		yl.setLayoutY(150);
		yl.setImage(youLose);

		exitScene = new Image("file:src/main/resources/EndGameBG.png");
		exs = new ImageView();
		exs.setImage(exitScene);

		// exit scene 2
		yesButton2 = new Button();
		yesButton2.setGraphic(new ImageView("file:src/main/resources/yesButton.png"));
		yesButton2.setPadding(Insets.EMPTY);
		yesButton2.setLayoutX(270);
		yesButton2.setLayoutY(400);

		noButton2 = new Button();
		noButton2.setGraphic(new ImageView("file:src/main/resources/noButton.png"));
		noButton2.setPadding(Insets.EMPTY);
		noButton2.setLayoutX(470);
		noButton2.setLayoutY(400);

		youWin2 = new Image("file:src/main/resources/youWinText.png");
		yw2 = new ImageView();
		yw2.setLayoutX(280);
		yw2.setLayoutY(150);
		yw2.setImage(youWin2);

		youLose2 = new Image("file:src/main/resources/youLoseText.png");
		yl2 = new ImageView();
		yl2.setLayoutX(280);
		yl2.setLayoutY(150);
		yl2.setImage(youLose2);

		exitScene2 = new Image("file:src/main/resources/EndGameBG.png");
		exs2 = new ImageView();
		exs2.setImage(exitScene2);

		/// quit game button
		quitGameButton = new Button();
		quitGameButton.setGraphic(new ImageView("file:src/main/resources/exitButton.png"));
		quitGameButton.setPadding(Insets.EMPTY);
		quitGameButton.setLayoutX(610);
		quitGameButton.setLayoutY(165);

		quitGameButton2 = new Button();
		quitGameButton2.setGraphic(new ImageView("file:src/main/resources/exitButton.png"));
		quitGameButton2.setPadding(Insets.EMPTY);
		quitGameButton2.setLayoutX(350);
		quitGameButton2.setLayoutY(500);
		quitGameButton2.setOnAction(leaveGame->{
			System.exit(0);
			Platform.exit();
		});

		// connect button
		Button connectButton = new Button();
		connectButton.setGraphic(new ImageView("file:src/main/resources/connectButton.png"));
		connectButton.setPadding(Insets.EMPTY);
		//connectButton.setPrefSize(100,50);
		connectButton.setLayoutY(450);
		connectButton.setLayoutX(360);
		clientLayer.getChildren().add(connectButton);

		connectButton.setOnAction(e->{
			ip = ipField.getText();
			port = Integer.parseInt(portField.getText());
			System.out.println("Received ip: "+ip);
			System.out.println("Received port: "+port);
			//clientLayer.getChildren().remove(connectButton);

			client = new Client(data->{
				Platform.runLater(()->{
					//System.out.println(data);		// command check
					//String messageReceived=data.toString();

					if(data.toString().equals("Player1")){player1=true;player2=false;p1Ready=true;}
					else if(data.toString().equals("Player2")){player2=true;player1=false;p2Ready=true;}

					if(player1&&!p1stop){p1stop=true;client.callback.accept("You are Player 1");}
					else if(player2&&!p2stop){p2stop=true;client.callback.accept("You are Player 2");}

					if(data.toString().equals("restartGame")){
						myPoints=0;
						opponentPoints=0;
						if(arePlayers){
							primaryStage.setScene(sceneMap.get("startGame"));restartGame=false;
						}
						else{
							primaryStage.setScene(sceneMap.get("disconnected"));restartGame=false;
						}
					}

					if(p1Ready){p1Ready=false;client.callback.accept("Player 1 ready.");}
					if(p2Ready){p2Ready=false;client.callback.accept("Player 2 ready.");}

					if(data.toString().equals("p1AddPoints")){
						enableButtons();
						if(player1)
							myPoints++;
						else
							opponentPoints++;
					}
					else if(data.toString().equals("p2AddPoints")){
						enableButtons();
						if(player2)
							myPoints++;
						else
							opponentPoints++;
					}
					else if(data.toString().equals("draw")){enableButtons();}

					else if(data.toString().equals("twoPlayers")){
						arePlayers=true;
						if(!player2){p2Ready=true;}
						if(!player1){p1Ready=true;}
						System.out.println("Found a match!");
						primaryStage.setScene(sceneMap.get("startGame"));
					}

					else if(data.toString().equals("PlayerDisconnected")||!arePlayers){
						arePlayers=false;
						if(player2){p1Ready=false;}
						if(player1){p2Ready=false;}
						primaryStage.setScene(sceneMap.get("disconnected"));
					}

					else if(data.toString().equals("end")){
						//System.out.println("I am player 1 "+player1+" and player 2 "+player2);
						System.out.println("opponent points: "+opponentPoints+" my points: "+myPoints);
						System.out.println("Are players "+arePlayers);
						if(myPoints>opponentPoints)
						primaryStage.setScene(sceneMap.get("exitWin"));
						else if(myPoints<opponentPoints)
						primaryStage.setScene(sceneMap.get("exitLose"));
					}

					else if(data.toString().equals("reset")){
						myPoints=0;
						opponentPoints=0;
						removeAllHands();
						System.out.println("removing all hands");
					}

					else if(data.toString().equals("rock")&&moved){opponentRock();moved=false;}
					else if(data.toString().equals("paper")&&moved){opponentPaper();moved=false;}
					else if(data.toString().equals("scissors")&&moved){opponentScissors();moved=false;}
					else if(data.toString().equals("spock")&&moved){opponentSpock();moved=false;}
					else if(data.toString().equals("lizard")&&moved){opponentLizard();moved=false;}

					// fillers not to print opponent's hand lol
					else if(data.toString().equals("rock")){}
					else if(data.toString().equals("paper")){}
					else if(data.toString().equals("scissors")){}
					else if(data.toString().equals("spock")){}
					else if(data.toString().equals("lizard")){}



/*					// old method
					else if(oppRock&&moved){opponentRock();oppRock=false;moved=false;}
					else if(oppPaper&&moved){opponentPaper();oppPaper=false;moved=false;}
					else if(oppScissors&&moved){opponentScissors();oppScissors=false;moved=false;}
					else if(oppLizard&&moved){opponentLizard();oppLizard=false;moved=false;}
					else if(oppSpock&&moved){opponentSpock();oppSpock=false;moved=false;}
*/

					else{pHand.getItems().add(data.toString());}
					// ------------------------------------------------

					oScore.setText(""+myPoints);
					pScore.setText(""+opponentPoints);

					/*
					if(!arePlayers && loadingscreent){
						clientLayer.getChildren().addAll(pln, wtg);
						loadingscreent = false;
					}
					else if(arePlayers && !loadingscreent){
						clientLayer.getChildren().removeAll(wtg, pln);
						primaryStage.setScene(sceneMap.get("startGame"));
					}

					*/

				});
			});

			client.changeIp(ip);
			client.changePort(port);
			client.start();

			System.out.println("Waiting on opponent..");

			// faster debug, remove for program use
			//primaryStage.setScene(sceneMap.get("startGame")); // use to fast access to game interface

		});


		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("startGame",  startClientGui());
		sceneMap.put("disconnected",  disconnectedClientGui());
		sceneMap.put("exitWin",  exitWinClientGui());
		sceneMap.put("exitLose",  exitLoseClientGui());

		Scene scene = new Scene(clientLayer,900,900);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	public Scene exitWinClientGui(){
		exitPaneWin = new Pane();
		yesButton.setOnAction(event1->{
			myPoints=0;
			opponentPoints=0;
			client.callback.accept("Waiting on opponent..");
			client.callback.accept("twoPlayers");
		});
		noButton.setOnAction(exit->{
			System.exit(0);
			Platform.exit();
		});
		exitPaneWin.getChildren().addAll(exs,yesButton,noButton);
		exitPaneWin.getChildren().add(yw);
		return new Scene(exitPaneWin, 900, 900);
	}

	public Scene exitLoseClientGui(){
		exitPaneLose = new Pane();
		yesButton2.setOnAction(event1->{
			myPoints=0;
			opponentPoints=0;
			client.callback.accept("twoPlayers");
		});
		noButton2.setOnAction(exit->{
			System.exit(0);
			Platform.exit();
		});
		exitPaneLose.getChildren().addAll(exs2,yesButton2,noButton2);
		exitPaneLose.getChildren().add(yl);
		return new Scene(exitPaneLose, 900, 900);
	}

	public Scene disconnectedClientGui(){
		disconnectedPane = new Pane();
		disconnectedPane.getChildren().addAll(pln, wtg, quitGameButton2);
		return new Scene(disconnectedPane, 900,900);
	}

	public Scene startClientGui(){
		pane = new Pane();
		background = new Image("file:src/main/resources/GameBG.png");
		bg = new ImageView();
		bg.setImage(background);
		pane.getChildren().add(bg);
		pane.getChildren().add(pHand);

		pane.getChildren().add(pScore);
		pane.getChildren().add(oScore);

		pane.getChildren().add(Orockp);
		pane.getChildren().add(Opaperp);
		pane.getChildren().add(Oscissorsp);
		pane.getChildren().add(Olizardp);
		pane.getChildren().add(Ospockp);

		Orockp.setVisible(false);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(false);
		Olizardp.setVisible(false);

		rock.setGraphic(new ImageView("file:src/main/resources/rock.png"));
		rock.setPadding(Insets.EMPTY);
		rock.setLayoutX(110);
		rock.setLayoutY(660);
		rock.setOnAction(e->{
			client.send("rock");
			try{pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}catch(Exception s){System.out.println("Can't remove previous image.");}
			pane.getChildren().add(rockp);
			moved=true;
			disableButtons();
		});
		pane.getChildren().add(rock);

		paper.setGraphic(new ImageView("file:src/main/resources/paper.png"));
		paper.setPadding(Insets.EMPTY);
		paper.setLayoutX(250);
		paper.setLayoutY(660);
		paper.setOnAction(a->{
			client.send("paper");
			try{pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}catch(Exception s){System.out.println("Can't remove previous image.");}
			pane.getChildren().add(paperp);
			moved=true;
			disableButtons();
		});
		pane.getChildren().add(paper);

		scissor.setGraphic(new ImageView("file:src/main/resources/scissor.png"));
		scissor.setPadding(Insets.EMPTY);
		scissor.setLayoutX(390);
		scissor.setLayoutY(660);
		scissor.setOnAction(a->{
			client.send("scissors");
			try{pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}catch(Exception s){System.out.println("Can't remove previous image.");}
			pane.getChildren().add(scissorsp);
			moved=true;
			disableButtons();
		});
		pane.getChildren().add(scissor);

		lizard.setGraphic(new ImageView("file:src/main/resources/lizard.png"));
		lizard.setPadding(Insets.EMPTY);
		lizard.setLayoutX(528);
		lizard.setLayoutY(660);
		lizard.setOnAction(a->{
			client.send("lizard");
			try{pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}catch(Exception s){System.out.println("Can't remove previous image.");}
			pane.getChildren().add(lizardp);
			moved=true;
			disableButtons();
		});
		pane.getChildren().add(lizard);

		spock.setGraphic(new ImageView("file:src/main/resources/spock.png"));
		spock.setPadding(Insets.EMPTY);
		spock.setLayoutX(665);
		spock.setLayoutY(660);
		spock.setOnAction(a->{
			client.send("spock");
			try{pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}catch(Exception s){System.out.println("Can't remove previous image.");}
			pane.getChildren().add(spockp);
			moved=true;
			disableButtons();
		});
		pane.getChildren().add(spock);
		quitGameButton.setOnAction(leave->{
			System.exit(0);
			Platform.exit();
		});
		pane.getChildren().add(quitGameButton);
		return new Scene(pane, 900,900);
	}

	public void updateScores(){
		opponentPoints = client.getP1Points();
		myPoints = client.getP2Points();
	}

	public void disableButtons(){
		rock.setDisable(true);
		paper.setDisable(true);
		scissor.setDisable(true);
		spock.setDisable(true);
		lizard.setDisable(true);
	}

	public void enableButtons(){
		rock.setDisable(false);
		paper.setDisable(false);
		scissor.setDisable(false);
		spock.setDisable(false);
		lizard.setDisable(false);
	}

	public void opponentRock(){
		Orockp.setVisible(true);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(false);
		Olizardp.setVisible(false);
	}
	public void opponentPaper(){
		Orockp.setVisible(false);
		Opaperp.setVisible(true);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(false);
		Olizardp.setVisible(false);
	}

	public void opponentScissors(){
		Orockp.setVisible(false);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(true);
		Ospockp.setVisible(false);
		Olizardp.setVisible(false);
	}

	public void opponentLizard(){
		Orockp.setVisible(false);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(false);
		Olizardp.setVisible(true);
	}

	public void opponentSpock(){
		Orockp.setVisible(false);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(true);
		Olizardp.setVisible(false);
	}

	public void removeAllHands(){
		Orockp.setVisible(false);
		Opaperp.setVisible(false);
		Oscissorsp.setVisible(false);
		Ospockp.setVisible(false);
		Olizardp.setVisible(false);
		try{
		pane.getChildren().removeAll(rockp,paperp,scissorsp,spockp,lizardp);}
		catch(Exception s){System.out.println("Can't remove previous image.");}
	}
}
