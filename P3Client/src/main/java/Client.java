import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

    protected int port;
    protected String ip;

    int p1Points=0;
    int p2Points=0;

    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    protected Consumer<Serializable> callback;

    Client(Consumer<Serializable> call){
        callback = call;
    }

    public void changeIp(String x){
        ip = x;
    }

    public void changePort(Integer x){
        port = x;
    }

    public void run() {

        try {
            socketClient= new Socket(ip,port);
            System.out.println("Setting ip: "+ip+" Port: "+port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {

            try {
                String message = in.readObject().toString();
                callback.accept(message);
            }
            catch(Exception e) {System.out.println("Can't update message");}
        }

    }

    public int getP1Points(){
        return p1Points;
    }

    public int getP2Points(){
        return p2Points;
    }

    public void send(String data) {
        try {
            out.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTest(String data) {
        System.out.println("Sent: "+data);
    }


}
