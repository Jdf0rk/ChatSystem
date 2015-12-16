package main;


import java.net.InetAddress;
import java.util.ArrayList;
import java.io.File; 

import services.*;
import gui.*;
import network.*;

public class ChatMediator {

	private static ChatMediator instance;

	private ChatMediator(){
	}

	public static ChatMediator getInstance(){
		if (instance == null){
			instance = new ChatMediator();
		}
		return instance;
	}

	// initialize all the fields
	private static ChatGUI gui;
	private static ChatController controller;
	private static ChatNI network;
	public void initAll(ChatController controller, ChatNI network, ChatGUI gui){
		this.controller = controller;
		this.network = network;
		this.gui = gui;
	}
	
	// methods to communicate between each class
	public void displayMessage(String msg, String usr){
		gui.showMessage(msg, usr);
	}
	
	public void log(){
		gui.openLogin();
	}
	
	public void logged(String name){
		controller.logged(name);
	}
	
	public void logOut(){
		controller.logOut();
	}
	
	public void clearAll(){
		gui.clearAll();
		network.clearAll();
	}
	
	public void exit(){
		controller.exit();
	}
	
	public void sendBroadCast(Message msg){
		network.sendBroadCast(msg);
	}

	public String getUserName() {
		return controller.getUserName();
	}
	
	public void openChatbox(ChatUserInfo info){
		gui.openChatbox(info);
	}
	
	public void openUserList(){
		gui.openUserList();
	}
	
	public synchronized void userListUpdated(){
		gui.userListUpdated();
	}
	
	public void createMessage(ChatUserInfo ID, MessageStruct message){
		controller.createMessage(ID, message);
	}
	
	public void messageReceived(Message msg, InetAddress addr){
		controller.receiveMessage(msg, addr);
	}
	
	public void clearMessages(String opponentID){
		gui.clearMessages(opponentID);
	}
	
	public void updateMessage(Message msg, ChatUserInfo id){
		gui.updateMessage(msg, id);
	}
	
	public void sendMessage(Message msg, InetAddress addr){
		network.sendMessage(msg, addr);
	}
	
	public ArrayList<InetAddress> getLocalAddresses(){
		return network.getLocalAddresses();
	}


}
