import java.io.*;
import java.net.*;

public class Client {

	BufferedReader reader;
	PrintWriter writer;
	// Send IPAddress and Port into 
	public static void main(String[] args){
		Client go = new Client();
		go.startThread(args[0], args[1]);
	}
    
    public void startThread(String str1, String str2){
    	try{

			// Client connect to server's corresponding IPAddress and Port
            InetAddress serverSideIP = InetAddress.getByName(str1);
            int serverListeningPort = Integer.parseInt(str2);
            Socket IPandPort_ClientSideConnectTo = new Socket(serverSideIP,serverListeningPort);
            
            // Create the read thread
            Runnable readMission = new ClientReadThread(IPandPort_ClientSideConnectTo);
            Thread readMissionWorker = new Thread(readMission);
            readMissionWorker.start();
            
            // Create the write thread
            Runnable writeMission= new ClientWriteThread(IPandPort_ClientSideConnectTo);
            Thread writeMissionWorker = new Thread(writeMission);
            writeMissionWorker.start();
 
            
        }catch(Exception ex){
            //ex.printStackTrace();
        }
    }
	  
	  
	 
    
    
    
    
}

