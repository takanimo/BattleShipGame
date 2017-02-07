package battleship;
import java.util.*;

import ships.Ship;

public class Fleet {

	private ArrayList<Ship> ships;
	private Game game;
	
	
	public Fleet(Game game){
		this.game = game;
		ships = new ArrayList<Ship>();
		ships.add(new Ship(5, "Aircraft Carrier", this));
		ships.add(new Ship(4, "Battleship", this));
		ships.add(new Ship(3, "Destroyer", this));
		ships.add(new Ship(3, "Submarine", this));
		ships.add(new Ship(2, "Patrol Boat", this));						
		
	}
	
	
	public boolean shipLeft(int index){
		
		return !ships.get(index).isPlaced();
	}
	
	
	public boolean isPlaced(){
		boolean isPlaced = true;
		for(Ship s:ships)
		{
			if (isPlaced){
				isPlaced = s.isPlaced();
			}
		}
		return isPlaced;
	}
	
	public Ship getShip(int index) {
		return ships.get(index);
	}

	public void isSunk(){
		boolean isSunk = true;
		for (Ship s: ships){
			if (isSunk){
				isSunk = s.isSunk();
			}
		}
		if (isSunk)
			game.printToStatusField("You win!");		
	}
}
