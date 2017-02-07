package battleship;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;


public class CellGUI extends JComponent {
	static final long serialVersionUID = 1L;
	
	private Color color;
	private Color water;
	private Color mouseColor;
	
	private MyMouseHandler mouseHandler;
	
	private int posX;
	private int posY;
	
	private boolean ship = false;
	
	private GridGUI gridGUI;
		

	public CellGUI(int x, int y, int a, int b, GridGUI g)
	{
		posX = a;
		posY = b;
		gridGUI = g;
		this.setPreferredSize(new Dimension(x, y));
		water = new Color(0,20,170);
		mouseColor = Color.green;
		color = water;
		
		mouseHandler = new MyMouseHandler();
		addMouseListener(mouseHandler);
	}
	
	
	public void setShip(){
		ship = true;
		if (gridGUI.isMyBoard()){
			water = new Color(0,100,120);
			color = water;
			repaint();
		}
	}
	
	
	public void setBomb(){
		water = new Color(0,150,220);
		color = water;
		if (ship){
			water = new Color(0,50,120);
			color = water;
		}
		repaint();
	}
	
	
	public void mouseClick()
	{		
		if (gridGUI.isMyBoard()){
			if (!gridGUI.getGame().bothPlayersReady())
				gridGUI.setShip(posX, posY);}
		else{
			if (gridGUI.getGame().bothPlayersReady()){
				if (!gridGUI.isBombed(posX, posY)){
					gridGUI.bomb(posX, posY);
					gridGUI.getGame().changeTurn();
				}
				else
					gridGUI.getGame().printToStatusField("Try again");
			}
		}
	}
	
	
	public void swapColor()
	{
		if (color == water)
			color = mouseColor;
		repaint();
	}

	
	public void resetColor()
	{
		if (color == mouseColor)
			color = water;
		repaint();
	}
	
	
	public void mouseOn()
	{
		int length = gridGUI.getShip(gridGUI.getShipIndex()).getLength();
		boolean vertical = gridGUI.isVertical();
		int xSize = gridGUI.getXSize();
		int ySize = gridGUI.getYSize();
		
		if (!vertical)
		{
			for (int i = 0; i < length; i++)
			{
				if (posY + i < ySize)
					gridGUI.getCellGUI(posX, posY + i).swapColor();
			}
		}
		else
		{
			for (int i = 0; i < length; i++)
			{
				if (posX + i < xSize)
					gridGUI.getCellGUI(posX + i, posY).swapColor();
			}
		}
	}

	public void mouseOff()
	{
		int length = gridGUI.getShip(gridGUI.getShipIndex()).getLength();
		boolean vertical = gridGUI.isVertical();
		int xSize = gridGUI.getXSize();
		int ySize = gridGUI.getYSize();
		
		if (!vertical)
		{
			for (int i = 0; i < length; i++)
			{
				if (posY + i < ySize)
					gridGUI.getCellGUI(posX, posY + i).resetColor();
			}
		}
		else
		{
			for (int i = 0; i < length; i++)
			{
				if (posX + i < xSize)
					gridGUI.getCellGUI(posX + i, posY).resetColor();
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Dimension d = getSize();
		
		g.setColor(color);
		g.fillRect(0, 0, d.width, d.height);
		
		g.setColor(Color.black);
		g.drawRect(0, 0, d.width, d.height);		
	
	}

	
	class MyMouseHandler extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e)
		{
			if (gridGUI.isMyBoard() && !gridGUI.getCurrentShip().isPlaced())
				mouseOn();
			else
				swapColor();
		}
		@Override
		public void mouseExited(MouseEvent e)
		{
			if (gridGUI.isMyBoard())
				mouseOff();
			else
				resetColor();
		}
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (SwingUtilities.isLeftMouseButton(e)){
				if (gridGUI.getGame().isMyTurn())
					mouseClick();
				else
					gridGUI.getGame().printToStatusField("It's not your turn");
			}

			else if (SwingUtilities.isRightMouseButton(e)){
				if (gridGUI.isMyBoard()){
					mouseOff();
					gridGUI.changeDir();
					mouseOn();}}
		}	
	}
}
