package network;


import java.net.*;
import java.io.*;

import battleship.*; 

public class NetworkPlayer implements Player{

	private ServerSocket serverSocket = null;
	private Socket socket = null;

	private DataOutputStream out;

	private Game game;
	private Receiver receiver;

	public NetworkPlayer(Game g)
	{
		game = g;
	}

	
	public void create(int port)
	{
		try
                    
		{
                    
                    InetAddress ip = InetAddress.getLocalHost();
                        
			serverSocket = new ServerSocket(port, 50, ip);
			
                        
                        socket = serverSocket.accept();
			out = new DataOutputStream(socket.getOutputStream());
			receiver = new Receiver(this,socket, game);
			receiver.start();
		}
		catch (Exception e)
		{
			System.err.println("unable to create: " + e.getMessage());
		}
	}

	
	public void connect(String address, int port)
	{
		try
		{
			socket = new Socket(address, port);
			out = new DataOutputStream(socket.getOutputStream());
			receiver = new Receiver(this,socket, game);
			receiver.start();
		}
		catch (Exception e)
		{
			System.err.println("unable to connect: " + e.getMessage());
		}
	}


	public void bomb(int x, int y)
	{
		send("c:" + x +"," + y);
	}

	
	public void send(String s)
	{
		try
		{
			out.writeUTF(s);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void setOpponentReady()
	{
		send("a");
	}
	
	public void goAhead()
	{	}
	
	public void sendMessage(String s)
	{
		send("m:"+s);
	}

	public void exit()
	{
		send("x");
		close();
	}
	
	public void close()
	{
		try
		{
			receiver.stop();
			if (serverSocket != null) {
				if (!serverSocket.isClosed())
					serverSocket.close();
			}
			socket.close();
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
