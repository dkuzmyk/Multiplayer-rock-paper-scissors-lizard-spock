import java.io.Serializable;
import java.util.function.Consumer;

public class GameLogic {

    private Consumer<Serializable> callback;
    GameInfo game = new GameInfo(callback);;

    public void updateClients(String message) {
        for(int i = 0; i < game.clients.size(); i++) {
            GameInfo.ClientThread t = game.clients.get(i);
            try {
                t.out.writeObject(message);
            }
            catch(Exception e) {System.out.println("System: Failed updating clients.");}
        }
    }

    void declareWinner(Integer winner){
        if(winner == 2){
            System.out.println("Player 2 wins!");
            //updateClients("Player 2 wins!");
            game.p2Points++;
            System.out.println("Player 2 has "+game.p2Points+" points.");
            //updateClients("p2AddPoints");
        }
        else if(winner == 1){
            System.out.println("Player 1 wins.");
            //updateClients("Player 1 wins!");
            game.p1Points++;
            System.out.println("Player 1 has "+game.p1Points+" points.");
            //updateClients("p1AddPoints");
        }
        else{
            //updateClients("It's a draw!!");
            //updateClients("draw");
            System.out.println("Draw.");
        }
    }

    void checkGameWinner(Integer p1, Integer p2){
        if(p1 == 3){
            //game.lastWinner=1;
            //updateClients("Player 1 wins the game!");
            //updateClients("end");
            System.out.println("Player 1 wins the game!");
            game.p1Points = 0;
            game.p2Points = 0;
        }

        else if(p2 == 3){
            //game.lastWinner=2;
            //updateClients("Player 2 wins the game!");
            //updateClients("end");
            System.out.println("Player 2 wins the game!");
            //game.p1Points = 0;
            //game.p2Points = 0;
        }

    }

    public int whoWon(String player1, String player2){
        int winner = 0;         // 0 = draw, 1 = player1 wins, 2 = player2 wins

        //System.out.println("p1: "+player1);
        //System.out.println("p2: "+player2);

        // rock
        if(player1.equals("rock") && player2.equals("paper"))
            winner = 2;
        else if(player1.equals("rock") && player2.equals("scissors"))
            winner = 1;
        else if(player1.equals("rock") && player2.equals("rock"))
            winner = 0;
        else if(player1.equals("rock") && player2.equals("lizard"))
            winner = 1;
        else if(player1.equals("rock") && player2.equals("spock"))
            winner = 2;
            // paper
        else if(player1.equals("paper") && player2.equals("paper"))
            winner = 0;
        else if(player1.equals("paper") && player2.equals("scissors"))
            winner = 2;
        else if(player1.equals("paper") && player2.equals("rock"))
            winner = 1;
        else if(player1.equals("paper") && player2.equals("lizard"))
            winner = 2;
        else if(player1.equals("paper") && player2.equals("spock"))
            winner = 1;
            // scissors
        else if(player1.equals("scissors") && player2.equals("paper"))
            winner = 1;
        else if(player1.equals("scissors") && player2.equals("scissors"))
            winner = 0;
        else if(player1.equals("scissors") && player2.equals("rock"))
            winner = 2;
        else if(player1.equals("scissors") && player2.equals("lizard"))
            winner = 1;
        else if(player1.equals("scissors") && player2.equals("spock"))
            winner = 2;
            // spock
        else if(player1.equals("spock") && player2.equals("paper"))
            winner = 2;
        else if(player1.equals("spock") && player2.equals("scissors"))
            winner = 1;
        else if(player1.equals("spock") && player2.equals("rock"))
            winner = 1;
        else if(player1.equals("spock") && player2.equals("lizard"))
            winner = 2;
        else if(player1.equals("spock") && player2.equals("spock"))
            winner = 0;
            // lizard
        else if(player1.equals("lizard") && player2.equals("paper"))
            winner = 1;
        else if(player1.equals("lizard") && player2.equals("scissors"))
            winner = 2;
        else if(player1.equals("lizard") && player2.equals("rock"))
            winner = 2;
        else if(player1.equals("lizard") && player2.equals("lizard"))
            winner = 0;
        else if(player1.equals("lizard") && player2.equals("spock"))
            winner = 1;

        else {
            System.out.println("Error in calculating who won.");
            return 999;
        }

        if(winner != 0)
            System.out.println("Winner player "+winner);
        else
            System.out.println("It's a draw!");

        return winner;
    }

}
