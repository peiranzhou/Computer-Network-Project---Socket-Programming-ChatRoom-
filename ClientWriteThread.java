import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientWriteThread implements Runnable {
	
    Socket clientWriteSocket;
    
    public ClientWriteThread(Socket clientSocket) {
        this.clientWriteSocket = clientSocket;
    }
    
    public void run() {
        try{
            ObjectOutputStream clientWriteMission = new ObjectOutputStream(clientWriteSocket.getOutputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            BufferedReader bufferReadOutputStream = new BufferedReader(inputStreamReader);
            while(true){
                String clientWriteOutputStream = bufferReadOutputStream.readLine();
                clientWriteMission.writeObject(clientWriteOutputStream);
            }
            
        }catch(Exception ex){
            //..ex.printStackTrace();
        }
        
    }
    
}