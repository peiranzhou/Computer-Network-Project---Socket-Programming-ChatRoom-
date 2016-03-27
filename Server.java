import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
	
	public static int PORTofTCP;
//	public static Socket sockZPR;
	public static HashMap<String, String> YongHu = new HashMap<String, String>();
	
	public static void main(String[] args) throws Exception{
		
//        PORTofTCP = Integer.parseInt(args[0]);
        
	    //------------------------------------------------------------------------------------------------------
        // Read text file to HashMap
        String username1;
		String password1;
		try{
			// read user_pass.txt, save each record (username, password) into YongHu
			
			File file = new File("user_pass.txt");
			Scanner UserPassFile = new Scanner (file);
			while(UserPassFile.hasNextLine()){
				Scanner UserPassFile2 = new Scanner(UserPassFile.nextLine());
				while(UserPassFile2.hasNext()){
				username1 = UserPassFile2.next();
				password1 = UserPassFile2.next();
				YongHu.put(username1, password1);
				}
		    }
			
		}catch(Exception ex){ }
        
		
	    //------------------------------------------------------------------------------------------------------
		// Start and wait for connection.
        ServerSocket serverSockZPR = new ServerSocket(Integer.parseInt(args[0])); //Server listening to the port, waiting for the request
        System.out.println("Connecting ... ");
        while(true){                                    
        
        // ServerSocket
        Socket sockZPR = new Socket();
        sockZPR = serverSockZPR.accept(); // Server accept connection from client       
        System.out.println("Successfully Connected!");

        //MultiThread
        Runnable runner = new mainServer(sockZPR); //Create runnable instance
        Thread myThread = new Thread(runner); //Create thread, and give it mission
        myThread.start(); //Start thread
            
        }
        
    }
	
}


