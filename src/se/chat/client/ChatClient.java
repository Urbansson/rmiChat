package se.chat.client;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import se.chat.interf.Chat;
import se.chat.interf.Receivable;



public class ChatClient extends UnicastRemoteObject implements Receivable{

	private static final long serialVersionUID = 1L;
	private Chat chatServer;
	private String userName; 


	protected ChatClient(Chat chatServer, String username) throws RemoteException {
		super();
		this.chatServer = chatServer;
		this.userName = username;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 2) {
			System.out.println("usage: java AuctionClient <server_host> <username>");
			System.exit(0);
		}


		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 2222);	
			Chat chat = (Chat) registry.lookup("chatserver");
			ChatClient client = new ChatClient(chat,args[1]);


			if(!chat.registerForChat(client))
				System.out.println("Username was taken, try with something else!");
			else
				client.runClient();

		}
		catch (NotBoundException nbe) {
			System.out.println(nbe.toString());
			System.out.println(args[0] + " is not available");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.exit(0);

	}

	private void runClient() throws RemoteException {
		Scanner scan = new Scanner(System.in);
		String command;
		while(true){
			command = scan.nextLine();
			if(command.equals("/help"))
				System.out.println(chatServer.getHelp());
			else if(command.equals("/who"))
				System.out.println(chatServer.getClients());
			else if(command.equals("/quit"))
				break;
			else if(command.startsWith("/nick")){
				try{
					String newUsername = command.split("\\s+")[1];
					if(chatServer.setNick(newUsername)){
						chatServer.sendMessage("Changed name to "+ newUsername, this.userName);
						this.userName =newUsername;

					}else{
						System.out.println("Name is taken try with something else!");
					}
				} catch ( ArrayIndexOutOfBoundsException a) {
					System.out.println("You forgot to type name");
				}
			}else{
				chatServer.sendMessage(command, this.userName);
			}

		}



		scan.close();
		System.out.println("Exiting...");
		chatServer.deRegisterChat(this);
		System.exit(0);
	}


	@Override
	public void ReciveNewMessage(String message) throws RemoteException {
		System.out.println(message);		
	}


	@Override
	public String getName() throws RemoteException {
		return userName;
	}


}
