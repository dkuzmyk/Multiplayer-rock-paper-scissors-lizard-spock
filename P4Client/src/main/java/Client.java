import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javax.xml.soap.Text;
import java.util.ArrayList;

/*
 * Authors: Tarun Maddineni, Aditya Vandanapu, Regie Sales, 
 * netIDs: tmaddi2
 * Description: This project contains the server code for the game Rock Paper Scicssors Lizard Spock. The server accepts connections from new
 *              clients on the local host. It receives input from both clients and decides on the winner. It will end the game after a client
 *              has reached 3 wins. The server uses threads to handle the clients separately and updates its own GUI separately.
 */

/*
 *  This class contains the client's logic for creating a new connection and passing info between the server and the client.
 */
public class Client extends Thread{
   Socket clientSocket;
   ObjectOutputStream out;
   ObjectInputStream in;
   GameInfo game = new GameInfo();
   Integer port;
   String ip;
   Button b1;
   Button b2;
   Button b3;
   Button b4;
   Button b5;
   Button b6;
   ListView playerList;
   ListView console;
   ListView console2;
   ImageView otherP;
   Scene scene2;
   Scene scene3;
   Stage stage;
   Integer id;
   ImageView myHand;
   ImageView tr;

   // points
   int myPoints=0;
   int myLoss=0;

  TextField winScore;
  TextField lossScore;
  TextField winScore2;
  TextField lossScore2;

   // Sends the challenge to the server
   public void sendChallenge(Integer i){
     GameInfo tempGame = new GameInfo();
     tempGame.messageID = 2;
     tempGame.p1ID = this.id;
     tempGame.p2ID = i;
     try{
       this.out.writeObject(tempGame);
       this.out.reset();
     }
     catch(Exception e){
       Platform.runLater(()->{console.getItems().add("Could not send challenge");});
       Platform.runLater(()->{console2.getItems().add("Could not send challenge");});
     }
   }


   // Sends the move to the server
   synchronized public void sendMove(String s){
     GameInfo tempGame = new GameInfo();
     tempGame.messageID = 3;
     tempGame.p1ID = this.game.p1ID;
     tempGame.p2ID = this.game.p2ID;
     if(this.id.equals(tempGame.p1ID)){
       tempGame.p1Move = s;
     }
     else{
       tempGame.p2Move = s;
     }
     try{
       this.out.writeObject(tempGame);
       this.out.reset();
     }
     catch(Exception e){
       Platform.runLater(()->{console.getItems().add("Could not send move");});
       Platform.runLater(()->{console2.getItems().add("Could not send move");});
     }
     this.game.p1ID = null;
     this.game.p2ID = null;
   }


   // Constructor that takes ip, port, and GUI elements to create the client
   Client(Integer p, String a, Button z, Button x, Button c, Button v, Button b, Button n, ListView l, ListView o,
          ImageView i, Scene s2, Scene s3, Stage s, ListView c2, ImageView mh, ImageView tri, TextField ws, TextField ls, TextField ws2, TextField ls2){
     port = p;
     ip = a;
     b1 = z;
     b2 = x;
     b3 = c; 
     b4 = v;
     b5 = b;
     b6 = n;
     playerList = l;
     console = o;
     otherP = i;
     scene2 = s2;
     scene3 = s3;
     stage = s;
     console2 = c2;
     myHand = mh;
     tr = tri;
     winScore = ws;
     lossScore = ls;
     winScore2 = ws2;
     lossScore2 = ls2;
   }

   // Run the client by attempting to connect to the server and reads/sends input from/to server
   public void run(){
     try{
       clientSocket = new Socket(ip, port);
       out = new ObjectOutputStream(clientSocket.getOutputStream());
       in = new ObjectInputStream(clientSocket.getInputStream());
       clientSocket.setTcpNoDelay(true);
     }
     catch(Exception e){
       Platform.runLater(()->{console.getItems().add("Streams not open");});
       Platform.runLater(()->{console2.getItems().add("Streams not open");});
     }

     // Accept game info from server and respond accordingly
     while(true){
       GameInfo tempGame = null;
       // Check to see if someone has been challenged
       try{
         tempGame = (GameInfo) this.in.readObject();
       }
       catch(Exception e){
         Platform.runLater(()->{console.getItems().add("Client could not receive information from server");});
         Platform.runLater(()->{console2.getItems().add("Client could not receive information from server");});
       }

       // If the player list needs to be udpated
       if(tempGame.messageID == 1){
         // When the player first joins update their id
         if(id == null){
           id = tempGame.playerList.size();
           Platform.runLater(()->{console.getItems().add("I'm player " + id);});
           Platform.runLater(()->{console2.getItems().add("I'm player " + id);});
         }
 
         // Empty player list
         Platform.runLater(()->{playerList.getItems().clear();});      
 
         this.game.playerList = new ArrayList<Integer>(); 

         // Use updated list
         for(int i = 0; i < tempGame.playerList.size(); i++){
           final int count = i + 1;
           this.game.playerList.add(tempGame.playerList.get(i));
           if(tempGame.playerList.get(i) != 0){
             Platform.runLater(()->{playerList.getItems().add("Player " + count);});
           }
         }
       }
       // If client has received the challenge display the game screen
       else if(tempGame.messageID == 2){
         this.game.p1ID = tempGame.p1ID;
         this.game.p2ID = tempGame.p2ID;
         Platform.runLater(()->{stage.setScene(scene3);});
       }
       // If a winner has been decided then tell both players and go back to lobby
       else{
         // Other client left
         if(tempGame.messageID == 4){
           Platform.runLater(()->{console.getItems().add("Your opponent left, you won!");});
           Platform.runLater(()->{
           tr.setImage(new Image("youWinText.png"));
           myPoints++;
           winScore.setText(""+myPoints);
           winScore2.setText(""+myPoints);});
           
     
           // Re-enable challenge option
           Platform.runLater(()->{b6.setDisable(false);});

           // Pause for a couple seconds then go back to the lobby
           PauseTransition p = new PauseTransition(Duration.millis(3000));
           p.setOnFinished(e->{
           // remove opponent hand
           otherP.setImage(new Image("empty.png"));
           // remove own hand here ()
           myHand.setImage(new Image("empty.png"));
           tr.setImage(new Image("empty.png"));
           // remove the win/loss pop up

           stage.setScene(scene2);
         });
         p.playFromStart();

         Platform.runLater(()->b1.setDisable(false));
         Platform.runLater(()->b2.setDisable(false));
         Platform.runLater(()->b3.setDisable(false));
         Platform.runLater(()->b4.setDisable(false));
         Platform.runLater(()->b5.setDisable(false));
    
         continue;
       }  

         Image other;
         // Display opponent's move
         if(id.equals(tempGame.p1ID)){
           if(tempGame.p2Move.equals("Rock")){
             other = new Image("rock.png");
           }
           else if(tempGame.p2Move.equals("Paper")){
             other = new Image("paper.png");
           }
           else if(tempGame.p2Move.equals("Scissors")){
             other = new Image("scissor.png");
           }
           else if(tempGame.p2Move.equals("Lizard")){
             other = new Image("lizard.png");
           }
           else{
             other = new Image("spock.png");
           }
         }
         else{
           if(tempGame.p1Move.equals("Rock")){
             other = new Image("rock.png");
           }
           else if(tempGame.p1Move.equals("Paper")){
             other = new Image("paper.png");
           }
           else if(tempGame.p1Move.equals("Scissors")){
             other = new Image("scissor.png");
           }
           else if(tempGame.p1Move.equals("Lizard")){
             other = new Image("lizard.png");
           }
           else{
             other = new Image("spock.png");
           }
         }
         Platform.runLater(()->{otherP.setImage(other);});
 
 
         // Display who played what
         final int p1 = tempGame.p1ID;
         final int p2 = tempGame.p2ID;
         final String p1Move = tempGame.p1Move;
         final String p2Move = tempGame.p2Move;
         final int winner = tempGame.whoWon;

         Platform.runLater(()->{console.getItems().add("Player " + p1 + " played " + p1Move);});
         Platform.runLater(()->{console.getItems().add("Player " + p2 + " played " + p2Move);});
         Platform.runLater(()->{console2.getItems().add("Player " + p1 + " played " + p1Move);});
         Platform.runLater(()->{console2.getItems().add("Player " + p2 + " played " + p2Move);});

         // Display who won
         if(tempGame.whoWon == 0){
           Platform.runLater(()->{console.getItems().add("You tied the round");});
           // make the DRAW pop up ==================================================================
           Platform.runLater(()->{tr.setImage(new Image("draw.png"));});
           Platform.runLater(()->{console2.getItems().add("You tied the round");});
         }

         else{
           Platform.runLater(()->{console.getItems().add("Player " + winner + " won the round!");});
           // make the YOU WIN pop up ================================================================
           if(winner == this.id){Platform.runLater(()->{
              tr.setImage(new Image("youWinText.png"));
              myPoints++;
              winScore.setText(""+myPoints);
              winScore2.setText(""+myPoints);
           });}
           else{Platform.runLater(()->{
             tr.setImage(new Image("youLoseText.png"));
             myLoss++;
             lossScore.setText(""+myLoss);
             lossScore2.setText(""+myLoss);
           });}
           // add points
           Platform.runLater(()->{console2.getItems().add("Player " + winner + " won the round!");});
         }
 
         // Re-enable challenge option
         Platform.runLater(()->{b6.setDisable(false);});

         // Pause for a couple seconds then go back to the lobby
         PauseTransition p = new PauseTransition(Duration.millis(3000));
         p.setOnFinished(e->{
           // remove opponent hand
           otherP.setImage(new Image("empty.png"));
           // remove own hand here ()
           myHand.setImage(new Image("empty.png"));
           tr.setImage(new Image("empty.png"));
           // remove the win/loss pop up

           stage.setScene(scene2);
         });
         p.playFromStart();         

         Platform.runLater(()->b1.setDisable(false));
         Platform.runLater(()->b2.setDisable(false));
         Platform.runLater(()->b3.setDisable(false));
         Platform.runLater(()->b4.setDisable(false));
         Platform.runLater(()->b5.setDisable(false));
       }

     }

   }
}
