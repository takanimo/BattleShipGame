package ai;

import java.util.*;

import ships.*;

import battleship.*;


public class AI implements Player{

	private Random rand;
	private Game game;
	private int boardSizeX = 10;
	private int boardSizeY = 10;

	private Grid board;
	
	private Fleet aiFleet;

	public AI(Game game)
	{
		rand = new Random();
		this.game = game;
		board = game.getOpponentBoard();

		aiFleet = board.getGridGUI().getFleet();
		for (int index = 0; index < 5; index++){
			placeBoat(aiFleet.getShip(index));
		}
		game.setOpponentStart();
	}

	
	public void bomb(int x, int y)
	{
		board.setBomb(x, y);
		if (!board.hasShip(x, y))
		{
			game.printToStatusField("Miss!");
		}
		else
	
		{
			game.printToStatusField("Hit!");
			
			if (board.getShip(x, y).isSunk())
			{
				game.printToStatusField("You sunk my " + board.getShip(x, y).getName());
			}
		}
	}

	
	public void randBomb()
	{
		int x;
		int y;
		do
		{
			x = rand.nextInt(boardSizeX);
			y = rand.nextInt(boardSizeY);
		}
		while (board.isBombed(x, y)); 		
		game.placeBomb(x, y);
		
	}

	public void placeBoat(Ship boat)
	{
		int yStart = 0;
		int yEnd = 0;
		int xStart = 0;
		int xEnd = 0;
		int direction = rand.nextInt(2);

		
		if (direction == 0)
		{
			yStart = rand.nextInt(boardSizeY); 
			xStart = rand.nextInt(boardSizeX - boat.getLength());
			xEnd = xStart + boat.getLength();
			
			boolean free = true;
			for (int i = xStart; i < xEnd; i++)
			{
				if (free)
					free = !board.hasShip(i, yStart);
			}
			if (free)
			{
				boat.setHorizontal();
				try {
					board.setShip(boat, xStart, yStart, xStart+boat.getLength()-1, yStart);
				} catch (BoatException e) {
					e.printStackTrace();
				}
			}
			else
				placeBoat(boat);
		}
		else
		{
			xStart = rand.nextInt(boardSizeX);
			yStart = rand.nextInt(boardSizeY - boat.getLength());
			yEnd = yStart + boat.getLength();

			boolean free = true;
			for (int i = yStart; i < yEnd; i++)
			{
				if (free)
					free = !board.hasShip(xStart, i);
			}

			if (free)
			{
				boat.setVertical();
				try {
					board.setShip(boat, xStart, yStart, xStart, yStart+boat.getLength()-1);
				} catch (BoatException e) {
					e.printStackTrace();
				}
			}
			else
				placeBoat(boat);
		}
	}
	
	public void goAhead()
	{
		randBomb();
		game.changeTurn();
	}

	public void sendMessage(String s)
	{
		game.printToChat("I can't talk to you.");
	}
	public void setOpponentReady() {}
	
	public void exit(){}
	
}
