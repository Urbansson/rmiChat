package se.chat.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

import se.chat.interf.Chat;
import se.chat.interf.Receivable;


public class ChatServer extends UnicastRemoteObject implements Chat{

	private static final long serialVersionUID = 1L;
	private ArrayList<Receivable> clientList = null;
	//  private ArrayList<String> usernNames = null;



	protected ChatServer() throws RemoteException {
		super();
		clientList = new ArrayList<Receivable>();
	}

	@Override
	public void sendMessage(String message, String user) throws RemoteException {
		// TODO Auto-generated method stub

		Iterator<Receivable> iter = clientList.iterator();
		while (iter.hasNext()) {
			try{
				iter.next().ReciveNewMessage(user+": "+message);
			}catch(RemoteException re){
				iter.remove();
			}
		}

	}

	@Override
	public String getClients() throws RemoteException {
		// TODO Auto-generated method stub
		StringBuilder users = new StringBuilder();
		users.append("Active users:\n");
		Iterator<Receivable> iter = clientList.iterator();
		while (iter.hasNext()) {
			try{
				users.append(iter.next().getName()+"\n");
			}catch(RemoteException re){
				iter.remove();
			}
		}
		users.deleteCharAt(users.length()-1);
		return users.toString();
	}

	@Override
	public String getHelp() throws RemoteException {
		// TODO Auto-generated method stub
		return "Walid commands are\n/quit \n/who \n/nick <new name>";
	}

	@Override
	public boolean setNick(String name) throws RemoteException {
		// TODO Auto-generated method stub

		Iterator<Receivable> iter = clientList.iterator();
		while (iter.hasNext()) {
			try{
				if(iter.next().getName().equals(name)){
					return false;
				}
			}catch(RemoteException re){
				iter.remove();
			}
		}	
		return true;
	}



	@Override
	public boolean registerForChat(Receivable n) throws RemoteException {

		if(this.setNick(n.getName())){
			clientList.add(n);
			return true;
		}
		return false;
	}

	@Override
	public void deRegisterChat(Receivable n) throws RemoteException {
		clientList.remove(n);     			
	}

	public static void main(String [] args) {

		try {

			ChatServer auc = new ChatServer();
			Registry registry = LocateRegistry.createRegistry(2222);

			registry.rebind("chatserver", auc);
			System.out.println("ChatServer is running...");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


}
