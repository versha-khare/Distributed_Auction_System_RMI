/*****************************************************
 *Buyer.java                                         *
 *                                                   *
 *Provide bidders with a way to bid for (or)         *
 *view active AuctionItems                           *
 *****************************************************/

import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.*;

public class Buyer{
	final static String host = "localhost";
	final static String serverName = "AuctionServer";

	public static void main(String[] args){
		
		try{
			//Inspect RMI registry in the host	
			AuctionInterface server = connectServer();
		    System.out.println("Connected Server \n");

		    ClientImplement z = new ClientImplement();
		    // Create the remote Object
		    ClientInterface client = (ClientInterface) UnicastRemoteObject.exportObject(z,0);
		    // Call displayMenu Method which shows Buyers option for Auction
		    displayMenu(server, client);

		}catch(MalformedURLException m){
			System.out.println(m);
		}catch(ConnectException e) {
			System.out.println("Connection to server refused. Check that server is up. Stopping Client");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*
	* Connects to remote server
	* Returns reference to remote object auction
	*/
	protected static AuctionInterface connectServer() throws MalformedURLException, RemoteException, NotBoundException {
		return (AuctionInterface) Naming.lookup("rmi://"+ host +"/"+ serverName);
	}
	
	/*
	* Display options to Buyers through which they can 
	* interact with the Auction.
	* @param AuctionInterface, ClientInterface
	*/
	public static void displayMenu(AuctionInterface a, ClientInterface c) throws MalformedURLException, RemoteException, NotBoundException{
		while(true){
			try {
				int choice = 0;
				System.out.println("[1] View all Auction Item(s).   [2] Bid for an item   [3] Exit");
				System.out.println("--------------------------------------------------------------");
				System.out.println("Choose an option:");
	
				Scanner scan = new Scanner(System.in);
				choice =scan.nextInt();
	
				switch(choice){
					case 1:
					    listAll(a);
					    break;
	
					case 2:
						bidItem(a, c);
						break;
	
					case 3:
						System.out.println("You have Exited.");
						System.exit(0);
						break;
	
					default:
						System.out.println("Invalid Option");
				}
			}catch(ConnectException e) {
				//if connection exception happens, try to reconnect to server in case the server restarted
				//this avoids the need to restart client program
				System.out.println("Lost connection to Server, trying to reconnect...");
				a=connectServer();
				System.out.println("Reconnected with server succesfully. Please re-enter command.");
			}
		}

	}

	/*
	* List all the active auction on server
	* @ AuctionInterface 
	*/
	public static void listAll(AuctionInterface a) throws ConnectException{
		try{
			System.out.println(a.listALLItems());
		}catch(ConnectException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
	    }
	}

	/*
	* Performs bidding for an AuctionItem by asking Auction_ID, bidderName, Email and BidValue
	* @param AuctionInterface, ClientInterface
	*/
	public static void bidItem(AuctionInterface a, ClientInterface c) throws ConnectException{
		try{
			System.out.println("Enter AuctionID of the Item you want to bid: ");
			Scanner scan = new Scanner(System.in);
			long x = scan.nextLong();

			System.out.println("Please enter your name: ");
			Scanner scan2 = new Scanner(System.in); 
			String name = scan2.nextLine();
			c.setName(name);

			System.out.println("Please enter your email: ");
			String email = scan2.nextLine();
			c.setEmail(email);

			System.out.println("Enter to bid: ");
			Scanner bidScan = new Scanner(System.in);
			double bidValue = Double.parseDouble(bidScan.nextLine());

			System.out.println(a.setBid(c , x, bidValue));
		}catch(NumberFormatException n){
		   	System.out.println("Please enter a valid (long Int) bid. ");
		}catch(ConnectException e){
			throw e;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}