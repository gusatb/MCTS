import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gus
 */
public class RandomPlayer extends Player{
    
    public void restart(){
        
    }
    
    public byte getMove(byte lastMove, ArrayList<Byte> board) throws Exception{
        ArrayList<Byte> moves = cf.legalMoves(board);
        return moves.get((int)(Math.random()*moves.size()));
    }
}
