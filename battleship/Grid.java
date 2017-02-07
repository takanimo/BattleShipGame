package battleship;

import ships.Ship;


public class Grid {

	private Cell[][] spelplan;
	private GridGUI gridGUI;

	
	public Grid(int rows, int cols, GridGUI g) {
		gridGUI = g;

		spelplan = new Cell[rows][cols];

		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++) {
				spelplan[row][col] = new Cell();
			}
		for (int row = 0; row < rows; row++)
			for (int col = 0; col < cols; col++) {
				spelplan[row][col].addGUI(gridGUI.getCellGUI(row, col));
			}
	}

	
	public void setShip(Ship ship, int xStart, int yStart, int xEnd, int yEnd)
			throws BoatException {
		if (xStart == xEnd) {
			if (yEnd < spelplan.length) {
				for (int i = yStart; i <= yEnd; i++) {

					if (spelplan[xStart][i].hasShip())
						throw new BoatException("");
				}
				for (int i = yStart; i <= yEnd; i++) {
					spelplan[xStart][i].setShip(ship);
				}
			} else
				throw new BoatException("asdasd");
		}

		if (yStart == yEnd) {
			if (xEnd < spelplan.length) {
				for (int i = xStart; i <= xEnd; i++) {
					if (spelplan[i][yStart].hasShip())
						throw new BoatException("Busy");
				}
				for (int i = xStart; i <= xEnd; i++) {
					spelplan[i][yStart].setShip(ship);
				}
			} else
				throw new BoatException("Busy");
		}
		ship.setPlaced();
	}

	public boolean setBomb(int x, int y) {
		return spelplan[x][y].setBomb();
	}

	public Cell getCell(int x, int y) {
		return spelplan[x][y];
	}

	public boolean isBombed(int x, int y) {
		return spelplan[x][y].hasBomb();
	}

	public boolean hasShip(int x, int y) {
		if (spelplan[x][y].hasShip())
			return true;
		return false;
	}

	public void setHit(int x, int y) {
		spelplan[x][y].setShip();
		spelplan[x][y].setBomb();
	}

	public void setMiss(int x, int y) {
		setBomb(x, y);
	}

	public Ship getShip(int x, int y) {
		return spelplan[x][y].getShip();
	}

	public GridGUI getGridGUI() {
		return gridGUI;
	}
}
