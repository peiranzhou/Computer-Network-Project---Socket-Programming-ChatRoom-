# Computer-Network-Project---Socket-Programming-ChatRoom-

Brief Description of Code
My program contains all the functions that the programming assignment required, including: 
1. Authentication log in, which supports multiple clients log in. 
After the client is logged in, we have the functions:
2. Command: who; (Display name of other connected users)
3. Command: last number; (Display name of those users connected within the last number minutes, e.g., last 15)
4. Command: broadcast message; (Broadcasts message to all connected users)
5. Command: send (user1 user2 user3) message; (Sends message to the list of users, separated by space. Ignore users who are not logged in)
6. Command: send user message; (Private message to a user. If the user is not online, the message will send when the user logs in)
7. Command: logout; (Log out this user)
Also,
8. Automatic logout of the client after 30 minutes of inactivity.
9. Graceful exit of client and server programs using control +c.
10. Online Reminder.
11. Offline Message.

There are eleven classes in the program, they are: 
Client.java: Client Side.
ClientReadThread.java: Client read data.
ClientWriteThread.java: Client write data.
Server.java: Server side.
mainServer.java: Server’s mainly code part.
OfflineMessage.java: Storing offline message.
An inner class: TimeCalculation.class: To calculate the time, if the time runs out, the user will automatically log out.



Details on Development Environment
Software: Eclipse, Terminal;
Java Version: 1.8.0_60;



Instructions on How to Run Code
1. Go to the directory of the file: 
2. Run the “Makefile” by using following command:
1> $Make clean
2> $Make
3. Run the Server: $Java Server Port
4. Run the Client: $Java Client 127.0.0.1 Port



Sample Commands to Invoke Code

Pre-Build:
$cd ~/Documents/workspace/Java/pz2210_java
$Make clean
$Make

Connection:
$Java Server 4119
$Java Client 127.0.0.1 4119

LogIn:
columbia
116bway

Command: 
1. Command “who”: user can check out who else is online now.
Example: who

2. Command “last number”: by typing into last number, user can check out the users who connected within the last number minutes.
Example: last 10

3. Command “broadcast message”: user can broadcast message to all the online users.
Example: broadcast Everybody I’m seas

4. Command “send (user1 user2) message”: User can send messages to a list of online users.
Example: send (columbia seas) HelloHello

5. Command “send user message”: an online user can send a private message to another user, no matter it online or not.
Example: send columbia I’m foobar

6. Command “logout”: Then the user type into the command “logout”, the user will log out from the online-chat system.
Example: logout



Description of Additional Functionalities
1. Online Reminder
Whenever a user successfully logged in, the system will remind other online users who just logged in.

2. Offline Message
If an online client A sends a private message to logged out client B. The message will be stored in the server. When the client B logs in, it will receive the message sent from client A. 

3. Inactivity Logout
If the user didn’t type any command, he will log out automatically



