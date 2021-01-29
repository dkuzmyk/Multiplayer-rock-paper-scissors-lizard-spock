import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GameInfo{
    int lastWinner;
    int p1Points=0;
    int p2Points=0;
    public String p1Hand = "";
    public String p2Hand = "";
    //GameLogic gameLogic;
    boolean twoPlayers = false;
    int winner;
    int port;

    int count = 1;
    int playerSize=0;

    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    serverThread server;
    private Consumer<Serializable> callback;

    GameInfo(Consumer<Serializable> call){          // constructor for the new server
        callback = call;
        server = new serverThread();
        server.start();
    }

    public void changePort(Integer x){
        port = x;
    }

    boolean areTwoPlayers(){
        int size = clients.size();
        if(size == 2)
            return true;

        return false;
    }

    public class serverThread extends Thread{

        public void run() {

            try(ServerSocket serverSock = new ServerSocket(port);){ // create a new socket for server
                System.out.println("System: Server is running on port: " + port);
                callback.accept("System: Server is running on port "+port);


                while(true) {                                       // infinite loop to listen
                    ClientThread c = new ClientThread(serverSock.accept(), count);
                    callback.accept("System: Client "+count+ " connected.");    // connect the client
                    clients.add(c);                                        // add client to list
                    c.start();                                             // start thread
                    System.out.println("System: Client "+count+" connected.");
                    count++;
                    playerSize++;
                }

            }//end of try
            catch(Exception e) {
                callback.accept("System: Server failed launch.");
                System.out.println("System: Server failed");
            }
        }//end of while
    }


    class ClientThread extends Thread{
        Socket connection;                      // new socket
        int count = 1;                          // count
        ObjectInputStream in;                   // input info
        ObjectOutputStream out;                 // output info

        ClientThread(Socket s, int count){      // constructor for client threads
            this.connection = s;
            this.count = count;
        }

        public void updateClients(String message) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(message);
                }
                catch(Exception e) {System.out.println("System: Failed updating clients.");}
            }
        }

        public void updateSingleClient(String message, Integer number){
            ClientThread t = clients.get(number);
            try{
                t.out.writeObject(message);
            }catch(Exception e){System.out.println("Can't update a single client.");}
        }

        public void updateServer(String message){
            // add messages to server log
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

        void declareWinner(Integer winner){
            if(winner == 2){
                callback.accept("Player 2 wins.");
                updateClients("Player 2 wins!");
                p2Points++;
                System.out.println("Player 2 has "+p2Points+" points.");
                callback.accept("Player 2 has "+p2Points+" points.");
                updateClients("p2AddPoints");
            }
            else if(winner == 1){
                callback.accept("Player 1 wins.");
                updateClients("Player 1 wins!");
                p1Points++;
                System.out.println("Player 1 has "+p1Points+" points.");
                callback.accept("Player 1 has "+p1Points+" points.");
                updateClients("p1AddPoints");
            }
            else{
                updateClients("It's a draw!!");
                updateClients("draw");
                callback.accept("Draw.");
            }
        }

        void checkGameWinner(Integer p1, Integer p2){
            if(p1Points == 3){
                lastWinner=1;
                updateClients("Player 1 wins the game!");
                updateClients("end");
                callback.accept("Player 1 wins the game.");
                System.out.println("Player 1 wins the game!");
                p1Points = 0;
                p2Points = 0;
            }

            else if(p2Points == 3){
                lastWinner=2;
                updateClients("Player 2 wins the game!");
                updateClients("end");
                callback.accept("Player 2 wins the game.");
                System.out.println("Player 2 wins the game!");
                p1Points = 0;
                p2Points = 0;
            }

        }


        public void run(){
            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            // need a better method for disconnected players
/*
            if(count == 1)
                updateSingleClient("Player1", count);
            else if(count == 2)
                updateSingleClient("Player2", count);
            else if(count%2 == 0)
                updateSingleClient("Player1", count);
            else
                updateSingleClient("Player2", count);
*/
            //System.out.println(clients.size());
            if(clients.size()==2){
                updateSingleClient("Player1", 0);
                updateSingleClient("Player2", 1);
            }


            //if(count >= 2){updateClients("twoPlayers");}
            if(clients.size() == 2){updateClients("twoPlayers");}
            //updateClients("Connected player: #"+count);
            updateClients("New player joins the game.");

            while(true) {
                try {   // core
                    String data = in.readObject().toString();
                    callback.accept("Player: " + count + " plays: " + data);
                    if(count == playerSize){updateClients("Player 2 chose his hand.");}
                    else  {updateClients("Player 1 chose his hand.");}

                    // sorting data
                    //if(count == 1){p1Hand = data;}              // first player is always p1
                    //else{p2Hand = data;}

                    if(count == playerSize){p2Hand = data;        // assign the newest person to p2
                    //System.out.println(count+" "+playerSize);
                    }
                    else {p1Hand = data;                          // oldest person always p1
                    //System.out.println(count+" "+playerSize);
                    }

                    updateSingleClient(p1Hand, 1);      // send the opponent hand to show image
                    updateSingleClient(p2Hand, 0);

                    if(data.equals("restartGame") && count == playerSize){updateSingleClient("restartGame", 1);
                    System.out.println("Count "+count+" == playersize  "+playerSize);}
                    else if(data.equals("restartGame") && count < playerSize){updateSingleClient("restartGame", 0);
                        System.out.println("Count "+count+" < playersize  "+playerSize);}

                    //if(data.equals("res3") && lastWinner==1){updateSingleClient("restartGame", 0);}
                    //if(data.equals("res3") && lastWinner==2){updateSingleClient("restartGame", 0);}
                    //if(!data.equals("res3")){updateSingleClient("restartGame", 1);}

                    System.out.println("p1: "+p1Hand+" "+"p2: "+p2Hand);
                    updateClients("Waiting on opponent player..");

                    try {
                        if(!p1Hand.equals("") && !p2Hand.equals("")) {
                            winner = whoWon(p1Hand, p2Hand);
                            p1Hand = "";
                            p2Hand = "";
                            declareWinner(winner);
                            checkGameWinner(p1Points, p2Points);
                        }
                    }catch(Exception a){System.out.println("Can't determine winner.");}


                }
                catch(Exception e) {
                    callback.accept("System: Error with player " + count + ". Disconnected");
                    updateClients("Player "+count+" Disconnected.");
                    updateClients("reset");
                    updateClients("PlayerDisconnected");

                    p1Points=0;
                    p2Points=0;
                    //System.out.println(clients.size());

                    clients.remove(this);
                    break;
                }
            }
        }//end of run
    }//end of client thread







}






