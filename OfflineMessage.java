
public class OfflineMessage {

	// We need the "MessageContent" sent from "UsernameWhoSentMessage" and a receiver "UsernameWhoWillReceiveMessage"
	
	private String MessageContent;	
	private String UsernameWhoWillReceiveMessage;
	private String UsernameWhoSentMessage;

	public void setMessageContent(String MessageContent){
		this.MessageContent = MessageContent;
	}
	public String getMessageContent(){
		return MessageContent;
	}

	public void setUsernameWhoWillReceiveMessage(String UsernameWhoWillReceiveMessage){
		this.UsernameWhoWillReceiveMessage = UsernameWhoWillReceiveMessage;
	}
	public String getUsernameWhoWillReceiveMessage(){
		return UsernameWhoWillReceiveMessage;
	}
	
	public void setUsernameWhoSentMessage(String UsernameWhoSentMessage){
		this.UsernameWhoSentMessage = UsernameWhoSentMessage;
	}
	public String getUsernameWhoSentMessage(){
		return UsernameWhoSentMessage;
	}
	
	
	
}
