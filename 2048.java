
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
public class 2048 extends Game{

     
    public 2048() {
        board_state = new ArrayList<>(10);
        board_state.add((byte)1);
        for(int j = 1; j <= 9; j++){
            board_state.add((byte)0);
        }
    }
     
    public void restart(){
        board_state = new ArrayList<>(10);
        board_state.add((byte)1);
        for(int j = 1; j <= 9; j++){
            board_state.add((byte)0);
        }
    }
    
    public ArrayList<Byte> legalMoves(ArrayList<Byte> board){
        ArrayList<Byte> moves = new ArrayList<>();
        if(getState(board)!=0)return moves;
        for(byte j = 1; j <= 9; j++){
            if(board.get(j)==0){
                moves.add(j);
            }
        }
        return moves;
    }
    
    public int simMove(byte m, ArrayList<Byte> board){ //same as above except operates on board, returns victory state
        if(board.get(m)==0){
            board.set(m, board.get(0));
            int ret = getState(board);
            if(board.get(0) == 1){
                board.set(0, (byte)2);
            }else{
                board.set(0, (byte)1);
            }
            return ret;
        }else{
            return -1;
        }
    }
    
    public int getState(ArrayList<Byte> board){
        int player;
        for(int j = 1; j <= 7; j+=3){ //hor
            player = board.get(j);
            if(board.get(j)==player && board.get(j+1)==player && board.get(j+2)==player){
                return player;
            }
        }
        for(int j = 1; j <= 3; j++){ //vert
            player = board.get(j);
            if(board.get(j)==player && board.get(j+3)==player && board.get(j+6)==player){
                return player;
            }
        }
        //diags
        player = board.get(5);
        if(board.get(5)==player && ((board.get(1)==player && board.get(9)==player) || (board.get(3)==player && board.get(7)==player))){
            return player;
        }
        for(int j = 1; j <= 9; j++){
            if(board.get(j)==0)
            return 0;
        }
        return -2;
    }
    
    public void printState(ArrayList<Byte> board){
        System.out.println("BOARD:\n");
            //for(int i = 0; i <= 2; i++)System.out.print(i+" | ");
            //System.out.print("\n");
            for (int j = 1; j <= 7; j += 3) {
                for (int i = 0; i <= 2; i++) {
                    System.out.print(board.get(j + i) + " | ");
                }
                System.out.print("\n_______________\n");
            }
    }
    
}
