
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
public class HumanPlayer extends Player{
    
    public void restart(){
        
    }
    
    public byte getMove(byte lastMove, ArrayList<Byte> board) throws Exception{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String s = bf.readLine();
        if(s.equals("quit")){
            return -1;
        }
        return Byte.parseByte(s);
    }
}
