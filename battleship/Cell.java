package battleship;
import ships.Ship;


public class Cell {
	private boolean bomb = false;
	private boolean skepp = false;
	private Ship ship;
	private CellGUI cellGUI;
	
	//metoder
	public boolean setBomb(){
		bomb = true;
		cellGUI.setBomb();
		if (skepp)
		{
			if (ship!= null){
				ship.hit();
			}
			return true;
		}
		return false;
	}
	
	public void setShip(Ship ship){
		this.ship = ship;
		skepp = true;
		cellGUI.setShip();
	}
	
	public void setShip(){
		skepp = true;
		cellGUI.setShip();
	}
	
	public void addGUI(CellGUI c){
		cellGUI = c;
	}
	
	public boolean hasShip(){
		return skepp;
	}
	
	public boolean hasBomb(){
		return bomb;
	}
	
	public Ship getShip(){
		return ship;
	}
	
	public CellGUI getGUI(){
		return cellGUI;
	}
}
