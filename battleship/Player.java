package battleship;

public interface Player {
	void bomb(int x, int y);
	void goAhead();
	void setOpponentReady();
	void sendMessage(String s);
	void exit();
}
