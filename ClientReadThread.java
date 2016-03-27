import java.io.ObjectInputStream;
import java.net.Socket;


public class ClientReadThread implements Runnable {
	
    Socket clientReadSocket;
    
    //Constructor Function
    public ClientReadThread(Socket clientSocket) {
        this.clientReadSocket = clientSocket;
    }

    public void run() {
        try{
            ObjectInputStream readInputStream0 = new ObjectInputStream(clientReadSocket.getInputStream());
            while(true){
                String clientReadInputStream = (String)readInputStream0.readObject();
                System.out.println(clientReadInputStream);
            }
        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
}