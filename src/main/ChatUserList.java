package main;

import java.net.*;
import java.util.*;

public class ChatUserList {
	
	private static ChatUserList instance;

	private ChatUserList() {
		userList = new HashMap<>();
	}

	public static ChatUserList getInstance() {
		if (instance == null)
			instance = new ChatUserList();
		return instance;
	}
	
	// HashMap for storing user's information
	private HashMap<InetAddress, ChatUserInfo> userList;
	
	public void eraseUserList(){
		userList.clear();
	}
	
	public ChatUserInfo getUser(InetAddress userID){
		return userList.get(userID);
	}
	
	// look for the element
	public boolean isInside(InetAddress userID){
		return userList.containsKey(userID);
	}
	
	// add an instance
	public synchronized void addInstance(String username, InetAddress address){
		if (!userList.containsKey(address))
			userList.put(address, new ChatUserInfo(username, address));
	}
	
	// remove an instance
	public void removeInstance(InetAddress userID){
		userList.remove(userID);
	}
	
	public InetAddress getAddress(InetAddress userID){
		if (userList.containsKey(userID))
			return userList.get(userID).getAddress();
		else
			return null;
	}

	public String getUserID(InetAddress address){
		if (userList.containsKey(address))
			return userList.get(address).getUserID();
		else
			return null;
	}
	
	// return a list of the users
	public Vector<ChatUserInfo> getUserList(){
		Vector<ChatUserInfo> result = new Vector<>();
		for (ChatUserInfo user : userList.values() ){
			result.addElement(user);
		}
		return result;
	}
	
	// return a list of users, containing the input string
	public Vector<ChatUserInfo> searchUserList(String input){
		if (input == "") // return the default list if nothing inside the search box
			return getUserList();
		else{
			Vector<ChatUserInfo> result = new Vector<>();
			for (ChatUserInfo user : userList.values() ){
				if (user.getUsername().contains(input))
					result.addElement(user);
			}
			return result;
		}
	}
}
