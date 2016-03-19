
import java.util.ArrayList;
import java.awt.Graphics;

public abstract class Game {
    
    public ArrayList<Byte> board_state; //index(0) = player to move;
    
    public abstract void restart();

    public abstract ArrayList<Byte> legalMoves(ArrayList<Byte> board);

    //public abstract int makeMove(int m); //returns 0 if game is not over, returns player who won, -1 if illegal move

    public abstract int simMove(byte m, ArrayList<Byte> board); //same as above except operates on board, returns victory state
    
    public abstract int getState(ArrayList<Byte> board); //-2 for draw, 0 for not over, else winning player
    
    public abstract void printState(ArrayList<Byte> board) throws Exception;
	
	public abstract void paintBoardState(Graphics g, int width, int height);
}
