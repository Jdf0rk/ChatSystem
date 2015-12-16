package main;

import java.net.*;

import services.*;
import gui.*;

public class ChatController {

	// singleton pattern for ChatController
	private static ChatController instance;
	private ChatController(){
	}

	public static ChatController getInstance() {
		if (instance == null)
			instance = new ChatController();
		return instance;
	}
	
	private ChatMediator mediator;
	private String userName;
	private ChatUserList userList;

	// Instantiate all the different classes
	public void initAll(ChatMediator mediator){
		userList = ChatUserList.getInstance();
		
		this.mediator = mediator;
	}
	
	public void start(){
		mediator.log();
	}
	
	public void setUserName(String name){
		userName = name;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void receiveMessage(Message message, InetAddress address){
		
		if ((userName != null)){
			//Adapter adapt = new Adapter(message);

			String userID = address.toString();
		
			switch (message.getHeader()){
				case bye:
					userID = userList.getUserID(address);
					// efface les messages de l'utilisateur
					mediator.clearMessages(userID);
					userList.removeInstance(address);
					mediator.userListUpdated();
					break;
					
				case hello:
					addNewUser(message, address);
					
					if (!mediator.getLocalAddresses().contains(address) && (userName != null))
						mediator.sendMessage(Message.createHelloAck(userName), address);
					break;
				case helloAck:
					addNewUser(message, address);
					System.out.println("On m'a répondu à mon bonjour !");
					break;
				case message:
					ChatUserInfo info = userList.getUser(address);
					if (info == null)
					System.out.println("WTF");
					// give the message to the GUIModel
					if (message.getData().length() > 0)
						mediator.updateMessage(message, info);
					break;
				default:
					break;
			}
		}
	}
	
	// add user to the list if it is not already inside 
	private void addNewUser(Message message, InetAddress address){
	//Adapter adapt = new Adapter(message);
		// if this application is not the source, add the user
		if (!mediator.getLocalAddresses().contains(address)){

			userList.addInstance(message.getData(), address);
			mediator.userListUpdated();
		}
	}
	
	// will create a message to send
	public void createMessage(ChatUserInfo ID, MessageStruct message){
		Message msg = Message.createMessage( message.getMessage());
		// give it to the NetworkNI
		//Adapter adapt = new Adapter(destinationID);
		mediator.sendMessage(msg, ID.getAddress());
	}
	
	public void logged(String name){

		String nick = null;
		try {
			nick = name + "@" + new String(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		setUserName(name);
		// send Hello from NI
		mediator.sendBroadCast(Message.createHello(nick));
		mediator.openUserList();
	}
	
	public void logOut(){
		exit();

		// reopen log box	
		mediator.log();
	}
	
	// send bye message and reinitialize all the lists
	public void exit(){
		mediator.sendBroadCast(Message.createBye());
		userName = null;
		clearAll();
	}
	
	public void clearAll(){
		userList.eraseUserList();
		mediator.clearAll();
	}


}
