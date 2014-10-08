package se.chat.interf;

/**
 * Auction.java
 *
 * This interface defines auction services, for clients to call 
 * through remote references.
 *
 * NB: Clients should call registerForNotification
 * to register on the server.
 */

import java.rmi.*;
import java.util.ArrayList;

public interface Chat extends Remote {
	
    public String getClients() throws RemoteException;
    public String getHelp() throws RemoteException;
    public boolean setNick(String name) throws RemoteException;
    public void sendMessage(String message, String user) throws RemoteException;


    
    /* Called by clients to register for server callbacks
     */
    public boolean registerForChat(Receivable n) throws RemoteException;
    public void deRegisterChat(Receivable n) throws RemoteException;
}