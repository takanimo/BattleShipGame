
package battleship;


import network.NetworkPlayer;
import ai.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFrame;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;



public class Game {	
	private GUI gui;
	
	private Grid playerBoard;
	private Grid opponentBoard;
	
	private int xBombed;
	private int yBombed;
	
	private boolean playerReady = false;
	private boolean opponentStart = false;
	
	private Player opponent;
	
	private boolean myTurn = true;
	private int port;
        static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics
        static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
        static Rectangle2D.Double splashTextArea;       // area where we draw the text
        static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
        static Font font;                               // used to draw our text

	//Constructor
	public Game(){
		int rows = 10;
		int cols = 10;
		port = 31415;
		gui = new GUI(rows, cols, this);
		
		playerBoard = gui.getGrid1().getGrid();
		opponentBoard = gui.getGrid2().getGrid();		
	}	
	private static void appInit()
    {
        for (int i = 1; i <= 10; i++)
        {   // pretend we have 10 things to do
            int pctDone = i * 10;       // this is about the only time I could calculate rather than guess progress
            splashText("Loading Game " + i);     // tell the user what initialization task is being done
            splashProgress(pctDone);            // give them an idea how much we have completed
            try
            {
                Thread.sleep(1000);             // wait a second
            }
            catch (InterruptedException ex)
            {
                break;
            }
        }
    }

    /**
     * Prepare the global variables for the other splash functions
     */
    private static void splashInit()
    {
        // the splash screen object is created by the JVM, if it is displaying a splash image
        
        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null

        if (mySplash != null)
        {
            // get the size of the image now being displayed
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(15., height*0.88, width * .45, 32.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height*.92, width*.4, 12 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 14);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
   
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }
    /**
     * Display a (very) basic progress bar
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLUE);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.RED);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }
	//Methods to use on the opponent.
		public void makeAI()
	{
		opponent = new AI(this);
	}
	
	public void createNetwork()
	{
		NetworkPlayer net = new NetworkPlayer(this);
		opponent = net;
		net.create(port);
	}
	
	public void connectNetwork(String address)
	{
		NetworkPlayer net = new NetworkPlayer(this);
		opponent = net;
		net.connect(address, port);
		myTurn = false;
		
	}
	
	public void sendMessage(String s)
	{
		opponent.sendMessage(s);
	}
	
	public void bombOpponent(int x, int y)
	{
		xBombed = x;
		yBombed = y;
		opponent.bomb(x,y);
	}
	
	public void setPlayerReady(){
		playerReady = true;
		if (opponent != null) {
			opponent.setOpponentReady();
		}
	}	
	
	
	
	public boolean placeBomb(int x, int y){
		return playerBoard.setBomb(x, y);
	}

	public void setHit(boolean hit)
	{
		if(hit)
			opponentBoard.setHit(xBombed, yBombed);
		else
			opponentBoard.setMiss(xBombed, yBombed);
	}
		
	
	public boolean bothPlayersReady(){
		return playerReady && opponentStart;
	}
	
	public boolean isOpponentStart() {
		return opponentStart;
	}

	public void setOpponentStart() {
		opponentStart = true;
	}

	
	public void printToStatusField(String s)
	{
		gui.updateStatus(s);
	}
	
	public void printToChat(String s)
	{
		gui.updateChat(s);
	}
	
	public Grid getOpponentBoard(){
		return opponentBoard;
	}

	public Grid getPlayerBoard(){
		return playerBoard;
	}

	public void changeTurn(){
		myTurn = !myTurn; 
		if(!myTurn){
			opponent.goAhead();
		}
	}
	
	public boolean isMyTurn(){
		return myTurn;
	}	
	
	public void close()
	{
		if (opponent != null) {
			opponent.exit();
		}
	}
	
	public static void main(String[] args) {
            splashInit();           // initialize splash overlay drawing parameters
            music();
        appInit();              // simulate what an application would do before starting
        if (mySplash != null)   // check if we really had a spash screen
            mySplash.close();   // we're done with it
        
       
    
		new Game();
                
	}
         public static void music(){
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;
        sun.audio.ContinuousAudioDataStream loop = null;
        try{
        BGM = new AudioStream(new FileInputStream("C:\\Users\\kishore\\Desktop\\java\\new\\Battleship\\Battleship\\src\\a5.wav"));
        MD = BGM.getData();
        loop = new sun.audio.ContinuousAudioDataStream(MD);
    }catch(IOException error){}
    MGP.start(loop);
    }
}
