package se.chat.interf;


import java.rmi.*;

public interface Receivable extends Remote {
	
    public void ReciveNewMessage(String message) throws RemoteException;    
    public String getName() throws RemoteException;    

}