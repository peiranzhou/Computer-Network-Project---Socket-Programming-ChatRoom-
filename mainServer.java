import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.*;


public class mainServer implements Runnable{
	
        //------------------------------------------------------------------------------------------------------
		// Initialization
        public static long BLOCK_OUT = 60;

		Socket clientSocket;
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;
		InetAddress enteringUserIP;
//		public long BLOCK_OUT3;
//		public int TIME_OUT3;
		static LinkedHashMap<String, String> theOnlineUsers = new LinkedHashMap<String, String>();
        static HashMap<String, InetAddress> blockingUsers = new HashMap<String, InetAddress>();
        static HashMap<String, Long> blockingUserTime = new HashMap<String, Long>();
        static HashMap<String, Long> UsersHaveBeenLoggedOut = new HashMap<String, Long>();
		static ArrayList<OfflineMessage> offlineMessage = new ArrayList<OfflineMessage>();
		static ArrayList<ObjectOutputStream> objectOutputStream = new ArrayList<ObjectOutputStream>(); // Match with the online users, whenever a user log in, there will be an ouput added into objectOutputtream
//		long TimeTryToLogIn;
//		long PassedTimeFromLastTimeLogIn;
//		long theRestOfTime;
        String enteringUserName = null;
        String enteringPassword = null;
        String sha1 = null;
    	public static int TIME_OUT = 30 * 60;  // By chaing the value, you can determine when the user will log out with inactivity.	
		      
    	
	    //------------------------------------------------------------------------------------------------------
    	// Constructor Function
		public mainServer(Socket sockZPR) throws Exception{
		this.clientSocket = sockZPR;
		    	
		enteringUserIP = clientSocket.getInetAddress();
		inputStream = new ObjectInputStream(clientSocket.getInputStream());
		outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
		objectOutputStream.add(outputStream);
		}
	
		
	    //------------------------------------------------------------------------------------------------------
	    public void run(){
	    	
        //  outputStream.writeObject("You have successfully connected with the server");
		    try {
				LogIn();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		    terminalCommandLine();
	     }
	    
	    
	    //------------------------------------------------------------------------------------------------------
	    // User Login Process
	    public void LogIn() throws NoSuchAlgorithmException{
//	    	String BLOCK_OUT2 = System.getenv("BLOCK_OUT");
//	    	BLOCK_OUT3 = Long.valueOf(BLOCK_OUT2).longValue();	    	

	    	String FailedUserName = "peiranzhou";
			String SHAString = null;
			int LogInTimesHaveTried = 0;
	    		    
		while(true){
			
			boolean WhetherExistingInYongHu = false;
			boolean UserSuccessfullyLoggedIn = false;
			boolean WhetherTheUserIsOnline = false;
			boolean WhetherIsBlocking = false;

			try {
				outputStream.writeObject("Please enter your username:");
				enteringUserName = (String)inputStream.readObject();
			
			    // Check whether the enteringClientName is existing in YongHu
				if(Server.YongHu.containsKey(enteringUserName)){
					WhetherExistingInYongHu = true;
				}
				else{
					WhetherExistingInYongHu = false;
				}
				
				// Check whether the user is blocking
				if(WhetherExistingInYongHu == true){
					if(blockingUsers.containsKey(enteringUserName) && blockingUsers.get(enteringUserName).equals(enteringUserIP)){  
						if((System.currentTimeMillis() - blockingUserTime.get(enteringUserName)) < BLOCK_OUT*1000){
							WhetherIsBlocking = true;
						}
						else{
							WhetherIsBlocking = false;
						}
					} // blocking: WhetherIsBlocking = true
					else{
						WhetherIsBlocking = false;
					}				
				
				if(WhetherIsBlocking == false){
					if(theOnlineUsers.containsKey(enteringUserName)){
						WhetherTheUserIsOnline = true;
						outputStream.writeObject("This Username is Online Now, Please Try Another Username");
					}
					else{
						WhetherTheUserIsOnline = false;
					}
				
				if(WhetherTheUserIsOnline == false){
					
					outputStream.writeObject("Please enter password: ");
					enteringPassword = (String) inputStream.readObject();
				    SHAString = convertStringToSHA1(enteringPassword);
				    
					if(Server.YongHu.get(enteringUserName).equals(SHAString) && Server.YongHu.containsKey(enteringUserName)){
						outputStream.writeObject("Welcome to Simple Chat Server");
						UserSuccessfullyLoggedIn = true;
					}
					else{
						if(FailedUserName.equals(enteringUserName)){
							LogInTimesHaveTried = LogInTimesHaveTried + 1;
						   UserSuccessfullyLoggedIn = false;
						   if(LogInTimesHaveTried == 3){
						      outputStream.writeObject("Sorry, You have been blocked!");
							  blockingUsers.put(enteringUserName, enteringUserIP);
							  blockingUserTime.put(enteringUserName, System.currentTimeMillis());
							}
						   else{
							   outputStream.writeObject("Your password is wrong, please try another one:");
						   }
						}
						if(FailedUserName.equals(enteringUserName) == false){
							   FailedUserName = enteringUserName;
							   LogInTimesHaveTried = 1;
							   UserSuccessfullyLoggedIn = false;
							   outputStream.writeObject("Your password is wrong, please try another one:");
						}
					}
				}
				
				if(UserSuccessfullyLoggedIn == true){
					UsersHaveBeenLoggedOut.remove(enteringUserName);
					sendOnlineNotice(enteringUserName);
					receiveOfflineMessage(enteringUserName);
					theOnlineUsers.put(enteringUserName, enteringPassword);
					break;
				}
				
				} // End of: Whether the user is blocking
				else{
					outputStream.writeObject("The user is being blocked.");
				}
				
				
				} // End of: Whether username is existing in YongHu
				else{
					outputStream.writeObject("The username is not existing, please type in another username. Thank you.");
				}								
			}catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
	  }
}    
	    
	    
	    //------------------------------------------------------------------------------------------------------
	    // User enter command line
	    public void terminalCommandLine(){
//	    	String TIME_OUT2 = System.getenv("TIME_OUT");
//	    	TIME_OUT3 = Integer.parseInt(TIME_OUT2);
	    	String enteringCommand = null;
	    	boolean WhetherGoalNameIsExisting = false;
	    	boolean NobodyIsOnline = true;
	    	
	    	// Using regular expression to realzie the command
	    	while(true){
	    		
	    		try {
	    			enteringCommand = null;
					outputStream.writeObject("Please enter command:");
					
					// Automatic logout of the client after 30 minutes of inactivity
					
										
					TimeCalculation eggTimer = new TimeCalculation(TIME_OUT);
					eggTimer.start();
					
					
					enteringCommand = (String)inputStream.readObject();
					
					// Command 1 - who
					if(enteringCommand.equals("who")){
						eggTimer.timer.cancel();
						commandWho(enteringUserName);
					}
					// Command 2 - last
					else if(enteringCommand.startsWith("last") && enteringCommand.length() > 5){
						eggTimer.timer.cancel();
						commandLast(enteringCommand);
					}
					// Command 3 - broadcast
					else if(enteringCommand.startsWith("broadcast")){
						eggTimer.timer.cancel();
                        commandBroadCast(enteringUserName, enteringCommand);                 
					}
					// Command 4 - send private
					else if(enteringCommand.startsWith("send") && enteringCommand.substring(5, 6).equals("(") == false){
						eggTimer.timer.cancel();
						commandSendPrivate(enteringCommand);
					}
					// Command 5 - send list of users
					else if(enteringCommand.length() > 5 && enteringCommand.substring(5, 6).equals("(")){
						eggTimer.timer.cancel();
						commandSendListUsers(enteringCommand, enteringUserName);
					}
					// Command 6 logout
					else if(enteringCommand.startsWith("logout")){
						eggTimer.timer.cancel();
						commandLogOut();
					}
					// Command 7 - wrong command
					else {
						eggTimer.timer.cancel();
						outputStream.writeObject("The command is wrong");
					}
					
				}catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    	}
	    	
	    }
	    
	    //------------------------------------------------------------------------------------------------------
	    // When a user has logged in, the system will notice other online users
	    public void sendOnlineNotice(String enteringUserName) throws IOException{
	    	int count3 = 0;
	    	for( String keyUserName : theOnlineUsers.keySet() ){
	    		if(keyUserName != enteringUserName){
			   	objectOutputStream.get(count3).writeObject("The User: " + enteringUserName + " is Online Now");
			   	objectOutputStream.get(count3).writeObject("Please enter command:");
	    		}
	    		count3 = count3 + 1;
			   }
	    }
	    // When a user has logged in, if there is any other user has sent him message, he will be noticed
	    public void receiveOfflineMessage(String enteringUserName) throws IOException{
	    	for(int m = 0; m < mainServer.offlineMessage.size(); m++){
		    	if(enteringUserName.equals(offlineMessage.get(m).getUsernameWhoWillReceiveMessage())){
		    		outputStream.writeObject("User " + mainServer.offlineMessage.get(m).getUsernameWhoSentMessage() + " Sent You the Message:" + mainServer.offlineMessage.get(m).getMessageContent());
		    		offlineMessage.remove(m);
		    	}
		    }
	    }
	    
	    //------------------------------------------------------------------------------------------------------
	    // A series of methods of Command:
	    // A series of methods of Command;
	    public void commandWho(String enteringUserName) throws IOException{
	    	for ( String keyUserName : theOnlineUsers.keySet() ) {
				if(keyUserName != enteringUserName){
					outputStream.writeObject(keyUserName);
				}
			}
			if(theOnlineUsers.size() == 1){
				outputStream.writeObject("Nobody else is online!");
			}
	    }
	    
	    public void commandBroadCast(String enteringUserName, String enteringCommand) throws IOException{
	    	String str1 = enteringCommand.substring(enteringCommand.indexOf(' '));
            String broadcastContent = str1.substring(1);
	    	int count0 = 0;
	    	for ( String keyUserName : theOnlineUsers.keySet() ) {
	    	    if(keyUserName != enteringUserName){
	    	    	objectOutputStream.get(count0).writeObject(enteringUserName + " broadcast: " + broadcastContent);
	    			objectOutputStream.get(count0).writeObject("Please enter command:");
	    	    }
	    	    count0 = count0 + 1;
	    	}
	    }
        public void commandLast(String enteringCommand) throws IOException{
        	String str0 = enteringCommand.substring(enteringCommand.indexOf(' '));
			String str00 = str0.substring(1);
		    int numberLastCommand = Integer.parseInt(str00);
		    
			for ( String keyUserName : theOnlineUsers.keySet() ) {
				outputStream.writeObject(keyUserName);
			}
			
			for ( String keyUserName : UsersHaveBeenLoggedOut.keySet() ) {
				long lastingTime = System.currentTimeMillis() - UsersHaveBeenLoggedOut.get(keyUserName);
				if(lastingTime < numberLastCommand*60*1000){
					outputStream.writeObject(keyUserName);
				}
			}
        }
	    public void commandLogOut() throws IOException{
	    	int count2 = 0;
			UsersHaveBeenLoggedOut.put(enteringUserName, System.currentTimeMillis());
			outputStream.writeObject("You have been logged out!");
			theOnlineUsers.remove(enteringUserName);
			for( String keyUserName : theOnlineUsers.keySet() ){
				if(enteringUserName.equals(keyUserName)){
					objectOutputStream.remove(count2);
				}
				count2 = count2 + 1;
			}
			clientSocket.close();
	    }
	    public void commandSendPrivate(String enteringCommand) throws IOException{
	    	String str2_userandmessage = enteringCommand.substring(5);
			String str2_user = str2_userandmessage.substring(0, str2_userandmessage.indexOf(' ')); // Goal name
			String str2_spaceandmessage = str2_userandmessage.substring(str2_userandmessage.indexOf(' '));
			String str2_message = str2_spaceandmessage.substring(1);
			
			if(enteringUserName.equals(str2_user) == false){
				if(Server.YongHu.containsKey(str2_user)){
					int count = 0;
					for ( String keyUserName : theOnlineUsers.keySet() ) {
						if(str2_user.equals(keyUserName)){
							objectOutputStream.get(count).writeObject(enteringUserName + " says: " + str2_message);
							objectOutputStream.get(count).writeObject("Please enter command:");
						}
						count = count + 1;
					}
					
					if(theOnlineUsers.containsKey(str2_user) == false){
						OfflineMessage leaveOfflineMessage = new OfflineMessage();
						leaveOfflineMessage.setUsernameWhoSentMessage(enteringUserName);
						leaveOfflineMessage.setUsernameWhoWillReceiveMessage(str2_user);
						leaveOfflineMessage.setMessageContent(str2_message);
						offlineMessage.add(leaveOfflineMessage);
					}
				}
				else{
					outputStream.writeObject("The user is not existing. Please try others.");
				}
			}
			else{
				outputStream.writeObject("You can not send message to yourself");

			}
	    }
	    public void commandSendListUsers(String enteringCommand, String enteringUserName) throws IOException{
	    	String str_4 = enteringCommand.substring(enteringCommand.indexOf('('), enteringCommand.indexOf(')'));
			str_4 = str_4.substring(1); // This line and last line are going to take out users information
			String str_message0 = enteringCommand.substring(enteringCommand.indexOf(")"));
			String str_message = str_message0.substring(2);// take out the message.
			
			while(true){
				if(str_4.indexOf(' ') != -1){
				String str_zpr = str_4.substring(0, str_4.indexOf(' '));
				int count = 0;
				for ( String keyUserName : theOnlineUsers.keySet() ) {
					if(str_zpr.equals(keyUserName)){
						objectOutputStream.get(count).writeObject(enteringUserName + " says: " + str_message);
						objectOutputStream.get(count).writeObject("Please enter command:");
					}
					count = count +1;
				}
				String str_44 = str_4.substring(str_4.indexOf(' '));
				str_4 = str_44.substring(1);
				}
				
				if(str_4.indexOf(' ') == -1){
					int count1 = 0;
					for ( String keyUserName : theOnlineUsers.keySet() ) {
						if(str_4.equals(keyUserName)){
							objectOutputStream.get(count1).writeObject(enteringUserName + " says: " + str_message);
							objectOutputStream.get(count1).writeObject("Please enter command:");
						}
						count1 = count1 +1;
					}

					break;
				}
			
		    }
	    }
	    
	    //------------------------------------------------------------------------------------------------------
	    // Set a time calculation, when time runs out, the user will log out automatically
public class TimeCalculation {
	    	
	    	private final Timer timer = new Timer();
	    	private final int HowLong;
	    	
	    	public TimeCalculation(int minutes) {
	    		this.HowLong = minutes;
	    	}
	    	
	    	public void start() {
	    		
	    		timer.schedule(new TimerTask() {
	    			
	    			public void run() {
	    				
    					if(theOnlineUsers.containsKey(enteringUserName)){
					    UsersHaveBeenLoggedOut.put(enteringUserName, System.currentTimeMillis());
						try {
							outputStream.writeObject("You have been logged out!");
						} catch (IOException e) {
							// TODO Auto-generated catch block
//							e.printStackTrace();
						}
						int count = 0;
						for( String keyUserName : theOnlineUsers.keySet() ){
							if(enteringUserName.equals(keyUserName)){
								objectOutputStream.remove(count);
							}
							count = count + 1;
						}
						theOnlineUsers.remove(enteringUserName);
						
						try {
							clientSocket.close();
						}catch (IOException e) {
						 // TODO Auto-generated catch block
                         //e.printStackTrace();
						}
						
					}
	    				
	    				
	    				timer.cancel();
	    			}

	    	}, HowLong * 1000 ); // Setting the time, when logout
	    		}

	    }
	    
	    
	    //------------------------------------------------------------------------------------------------------
	    // Convert enteringPassword to SHA1
	    private static String convertStringToSHA1(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{

		    MessageDigest convertion = MessageDigest.getInstance("SHA-1");
		    convertion.reset();
		    convertion.update(password.getBytes("UTF-8"));

		    return new BigInteger(1, convertion.digest()).toString(16);
		}
	    
	}

     
