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
		
		if ((userName != null) && (message.getSender() != null)){
			
			String userID = message.getSender() + "@" + address.toString();
		
			switch (message.getHeader()){
				case bye:
					// efface les messages de l'utilisateur
					mediator.clearMessages(userID);
					
					userList.removeInstance(userID);
					mediator.userListUpdated();
					break;
					
				case hello:
					addNewUser(message, address);
					
					if (!mediator.getLocalAddresses().contains(address) && (userName != null))
						mediator.sendMessage(Message.createHelloAck(userName), userList.getAddress(userID));
					break;
				case helloAck:
					addNewUser(message, address);
					break;
				case message:
					addNewUser(message, address);
					
					// give the message to the GUIModel
					if (message.getData().length() > 0)
						mediator.updateMessage(message, userID);
					break;
				default:
					break;
			}
		}
	}
	
	// add user to the list if it is not already inside 
	private void addNewUser(Message message, InetAddress address){
		// if this application is not the source, add the user
		if (!mediator.getLocalAddresses().contains(address) && message.getSender() != ""){
			userList.addInstance(message.getSender(), address);
			mediator.userListUpdated();
		}
	}
	
	// will create a message to send
	public void createMessage(String destinationID, MessageStruct message){
		Message msg = Message.createMessage( message.getMessage());
		// give it to the NetworkNI
		mediator.sendMessage(msg, userList.getAddress(destinationID));
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
