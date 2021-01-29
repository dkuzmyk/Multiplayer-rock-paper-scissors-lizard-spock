import java.io.Serializable;
import java.util.ArrayList;

/*
 * Class that will hold information of the state of the game and will be passed between the server and clients.
 */
public class GameInfo implements Serializable{

  ArrayList<Integer> playerList;
  Integer p1ID;
  Integer p2ID;
  String p1Move;
  String p2Move;
  Integer messageID;
  Integer whoWon;

  // New game sets all values to 0/null
  GameInfo(){
    playerList = new ArrayList<Integer>();
    p1ID = 0;
    p2ID = 0;
    p1Move = null;
    p2Move = null;
    messageID = 1;
    whoWon = 0;
  }
}
