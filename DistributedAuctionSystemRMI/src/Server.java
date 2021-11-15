/****************************************************
 * Server.java                                      *
 *                                                  *
 * This is the Server that host the Auction
 *
 *
 ****************************************************/
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.net.MalformedURLException;


public class Server{

	public Server(){
		try{
			//if backup file exists, load the file and use it to load the auction object
			File objectFile = new File("auctionBackup.txt");
			Auction auction;
			if (objectFile.exists()) {
				FileInputStream fi = new FileInputStream(objectFile);
				ObjectInputStream oi = new ObjectInputStream(fi);
				
				auction = new Auction((ConcurrentHashMap) oi.readObject());
				
				oi.close();
				fi.close();
			}
			else {
				//if backup doesn't exists, start with empty auction
				auction = new Auction();
			}
			//Create the remote Object
			AuctionInterface a = (AuctionInterface) UnicastRemoteObject.exportObject(auction, 0);
			//Register the object to RMI registry
			Naming.rebind("rmi://localhost/AuctionServer",a);
			System.out.println("Server is Active now!!!");
		}catch(MalformedURLException m){
			System.out.println(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		// Creates new Server for Auction
		new Server();
	}
}