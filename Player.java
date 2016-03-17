
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
public abstract class Player {
    Game cf;
    public abstract byte getMove(byte lastMove, ArrayList<Byte> board) throws Exception; //-1 is quit
    public abstract void restart();
    public void update(byte lastMove){}
}
