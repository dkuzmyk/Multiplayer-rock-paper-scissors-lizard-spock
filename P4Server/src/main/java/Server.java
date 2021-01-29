import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.util.Duration; 


/*
 * Authors: Tarun Maddineni, Aditya Vandanapu, Dmytro Kuzmyk, Reggie Sales
 * netIDs: tmaddi2, avanda7, dkuzmy3, rsales3
 * Description: This project contains the server code for the game Rock Paper Scicssors Lizard Spock. The server accepts connections from new
 *              clients on the local host. It receives input from both clients and decides on the winner. It will end the game after a client
 *              has reached 3 wins. The server uses threads to handle the clients separately and updates its own GUI separately.
 */

/*
 *  This class contains the server's logic for creating a new connection and passing info between the server and the clients.
 */
public class Server{
  TheServer server;
  Integer port;
  ArrayList<ClientThread> clientList = new ArrayList<ClientThread>();
  ListView console;
  ListView players;
  int count;

  // Constructor that takes two ListViews and an Integer as inputs. Creates a new TheServer object and starts it.
  Server(ListView l1, ListView l2, Integer p){
    server = new TheServer();
    port = p;
    console = l1;
    players = l2;
    count = 1;
    server.start();
  }

  // Inner class that actually contains the server logic to send and receive game info. Creates the separate client threads.
  public class TheServer extends Thread{
    
    public void run(){
      ServerSocket mysocket = null;
      // Try to launch server on the specified port
      try{
        mysocket = new ServerSocket(port);
        Platform.runLater(()->{console.getItems().add("System: Server is running on port "+port);});
      }
      catch(Exception e){
        Platform.runLater(()->{console.getItems().add("The server socket did not launch!");});
      }
      // Continue to accept clients and add them to the array list
      while(true){
        try{
          ClientThread c = new ClientThread(mysocket.accept(), count, 1);
          clientList.add(c);
          c.start();
          Platform.runLater(()->{console.getItems().add("Client: " + c.id + " joined the server!");});
          count++;
        }
        catch(Exception e){
          Platform.runLater(()->{console.getItems().add("Client could not join");});
        }
      }
    }
  }

  // Inner class that will keep track of the client connection on its own thread
  public class ClientThread extends Thread{
    Socket connection;
    Integer id;
    GameInfo game;
    ObjectInputStream in;
    ObjectOutputStream out;
    int status;
    ClientThread opponent;

    // Constructor that creates a new ClientThread given a socket
    ClientThread(Socket s, Integer n, Integer c){
      this.connection = s;
      this.id = n;
      this.status = c;
      this.opponent = null;
      game = new GameInfo();
    }

    // Send the new player list with statuses to other client threads
    synchronized void updatePlayerList(){
      GameInfo tempGame = new GameInfo();
      tempGame.messageID = 1;

      // Give the thread a current copy of the playerlist
      for(int i = 0; i < clientList.size(); i++){
        ClientThread t = clientList.get(i);
        if(t == null){
          tempGame.playerList.add(0);
        }
        else if(t.status == 1){
          tempGame.playerList.add(1);
        }
        else{
          tempGame.playerList.add(2);
        }
      }
 
      // Reset the sever player list
      Platform.runLater(()->{players.getItems().clear();});

      // Send the current player list to each client
      for(int i = 0; i < clientList.size(); i++){
        ClientThread t = clientList.get(i);
        if(t == null){
          continue;
        }
        try{
          Platform.runLater(()->{players.getItems().add("Player " + t.id);});
          t.out.writeObject(tempGame);
          t.out.reset();
        }
        catch(Exception e){}
      }
    }

    // Set the winner of the round
    void gameResult(GameInfo g){
      String p1 = g.p1Move;
      String p2 = g.p2Move;
      
      // If both are same then it's a tie
       if((p1.equals(p2))){
         g.whoWon = 0;
       }

       // Rock is beaten by Paper and Spock, but beats Lizard and Scissors
       else if(p1.equals("Rock")){
         if((p2.equals("Paper")) || (p2.equals("Spock"))){
           g.whoWon = g.p2ID;
         }
         else{
           g.whoWon = g.p1ID;
           System.out.println(g.p1ID);
         }
       }

       // Paper is beaten by Scissors and Lizard, but beats Rock and Spock
       else if(p1.equals("Paper")){
         if((p2.equals("Scissors")) || (p2.equals("Lizard"))){
           g.whoWon = g.p2ID;
         }
         else{
           g.whoWon = g.p1ID;
         }
       }

       // Scissors is beaten by Spock and Rock, but beats Lizard and Paper
       else if(p1.equals("Scissors")){
         if((p2.equals("Spock")) || (p2.equals("Rock"))){
           g.whoWon = g.p2ID;
         }
         else{
           g.whoWon = g.p1ID;
         }
       }

       // Spock is beaten by Lizard and Paper, but beats Rock and Scissors
       else if(p1.equals("Spock")){
         if((p2.equals("Paper")) || (p2.equals("Lizard"))){
           g.whoWon = g.p2ID;
         }
         else{
           g.whoWon = g.p1ID;
         }
       }

       // Lizard is beaten by Scissors and Rock, but beats Paper and Spock
       else{
         if((p2.equals("Scissors")) || (p2.equals("Rock"))){
           g.whoWon = g.p2ID;
         }
         else{
           g.whoWon = g.p1ID;
         }
       }
 
    }

    // Run the client thread
    public void run(){
      try{
        in = new ObjectInputStream(connection.getInputStream());
        out = new ObjectOutputStream(connection.getOutputStream());
        connection.setTcpNoDelay(true);
      } 
      catch(Exception e) {
        System.out.println("Streams not open");
      }

      // Give all clients the new player list
      updatePlayerList();
      
      // Read in game information and respond accordingly
      while(true){
        GameInfo tempGame;
        try{
          tempGame = (GameInfo)this.in.readObject();
          
          // Just update the player list
          if(tempGame.messageID == 1){
            try{
              this.out.writeObject(tempGame);
              this.out.reset();
            }
            catch(Exception e){
              Platform.runLater(()->{console.getItems().add("Client " + this.id + " could not update its clients");});
            }
          }
          // If the client has challenged someone, notify both players
          else if(tempGame.messageID == 2){
            ClientThread t = clientList.get(tempGame.p2ID - 1);
            this.opponent = t;
            t.opponent = this;
            this.status = 2;
            t.status = 2;
            updatePlayerList();
            Platform.runLater(()->{console.getItems().add("Player " + tempGame.p1ID + " challenged Player " + tempGame.p2ID);});
            try{
              this.out.writeObject(tempGame);
              this.out.reset();
            }
            catch(Exception e){
              Platform.runLater(()->{console.getItems().add("Could not send challenge to player " + tempGame.p1ID);});
            }
            try{
              t.out.writeObject(tempGame);
              t.out.reset();
            }
            catch(Exception e){
              Platform.runLater(()->{console.getItems().add("Could not send challenge to player " + tempGame.p2ID);});
            }
          }
          // If the client has made a move, messageID = 3
          else{
           
            ClientThread t = clientList.get(tempGame.p1ID - 1);   
            ClientThread t2 = clientList.get(tempGame.p2ID - 1);

            // If the client is player 1 
            if(this.id.equals(tempGame.p1ID)){
              Platform.runLater(()->{console.getItems().add("Player "+tempGame.p1ID+" chose"+tempGame.p1Move);});
              t.game.p1Move = tempGame.p1Move;  
              t.game.p1ID = this.id;          
            }
            // If the client is player 2
            else{
              Platform.runLater(()->{console.getItems().add("Player "+tempGame.p2ID+" chose"+tempGame.p2Move);});
              t.game.p2Move = tempGame.p2Move;
              t.game.p2ID = this.id;
            }
            
            // If both players have made their move
            if((t.game.p1Move != null) && (t.game.p2Move != null)){
              // Set winner and send results
              GameInfo newGame = t.game;
              t.game = new GameInfo();
              newGame.messageID = 3;
              gameResult(newGame);

              if(newGame.whoWon == 0){
                Platform.runLater(()->{console.getItems().add("Client " + newGame.p1ID + " tied their game" 
                                      + " with Client " + newGame.p2ID);});
              } 
              else{
                Platform.runLater(()->{console.getItems().add("Client " + newGame.whoWon + " won their game");});
              }
              
              try{
                t2.out.writeObject(newGame);
                t2.out.reset();
              }
              catch(Exception e){
                Platform.runLater(()->{console.getItems().add("Could not send winner");});
              }
              try{
                t.out.writeObject(newGame);
                t.out.reset();
              } 
              catch(Exception e){
                Platform.runLater(()->{console.getItems().add("Could not send winner");});
              }
             
              t.status = 1;
              t2.status = 1;

              PauseTransition p = new PauseTransition(Duration.millis(3000));
              p.setOnFinished(e->{updatePlayerList();});
              p.playFromStart();
            }
          }
        }
        catch(Exception e) {
          Platform.runLater(()->{console.getItems().add("Client" + this.id + " disconnected.");});
          if(this.status == 2){
            GameInfo tGame = new GameInfo();
            tGame.messageID = 4; 
            try{
              opponent.out.writeObject(tGame);
              opponent.status = 1;
            }
            catch(Exception e2){}
          }
          clientList.set(this.id - 1, null);
          PauseTransition p = new PauseTransition(Duration.millis(3000));
          p.setOnFinished(y->{updatePlayerList();});
          break;
        }
      }
    }
  }
}
