package gui;

import main.*;
//import services.Adapter;

import java.util.*;

// generate an output presentation based on the model

public class GUIView {

	private static GUIView instance;
	private ChatGUI controller;
	private HashMap<ChatUserInfo, ViewChatBox> chatBox;
	
	private GUIView(ChatGUI chatGUI) {
		controller = chatGUI;
		chatBox = new HashMap<ChatUserInfo, ViewChatBox>();
	}

	public static GUIView getInstance() {
		if (instance == null)
			instance = new GUIView(ChatGUI.getInstance());
		return instance;
	}
	
	public static GUIView getInstance(ChatGUI chatGUI) {
		if (instance == null)
			instance = new GUIView(chatGUI);
		return instance;
	}
	
	private ViewLogin login;
	public void openLoginWindow(){
		if (login == null)
			login = new ViewLogin();
	}
	
	public void closeLoginWindow(){
		login.dispose();
		login = null;
	}
	
	public void openChatbox(ChatUserInfo info){
		//multiton pattern
		if (!isChatOpen(info)){
			chatBox.put(info, new ViewChatBox(info));
		}
	}
	
	public ViewChatBox getChatBox(ChatUserInfo id){
		return chatBox.get(id);
	}
	
	public boolean isChatOpen(ChatUserInfo id){
		return chatBox.containsKey(id);
	}
	
	public void closeChatBox(ChatUserInfo id){
		chatBox.remove(id);
	}
	
	private ViewUserList userList;
	public void openUserList(){
		userList = new ViewUserList();
	}
	
	public void closeUserList(){
		if (userList != null)
			userList.dispose();
		userList = null;
	}
	
	public void updateUserList(){
		// the userList window is opened
		if (userList != null){
			userList.updateList();
		}
	}
	
	public void messageReceivedNotification(ChatUserInfo id){
		// test if the chatbox window is opened
		//Adapter adapt = new Adapter(id);

		if (userList != null){
			if (!isChatOpen(id)){
				ChatUserList.getInstance().getUser(id.getAddress()).incrementUnreadCount();
				userList.updateList();
			}
		}
			
		// if not, add count to unread message and update UserList
	}
	

	
	public void clearAll(){
		// close all the opened chatboxes
		for (ViewChatBox box : chatBox.values()){
			box.closeBox();
		}
		chatBox.clear();

		// close the UserList
		userList.dispose();
	}
}
