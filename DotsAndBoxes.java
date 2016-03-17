
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
public class DotsAndBoxes extends Game {

    // board 5x5 dots... each line conecting 2 dots is a potential move. complete a box to recieve a point and go again. Last 2 is p1 and p2 score
    // indexes 1-40 are 3 for counted line, 1 for p1 line, 2 for p2 line
    public DotsAndBoxes() {
        board_state = new ArrayList<>(43);
        board_state.add((byte)1);
        for (int j = 1; j <= 42; j++) {
            board_state.add((byte)0);
        }
    }
    
    public void restart(){
        board_state = new ArrayList<>(43);
        board_state.add((byte)1);
        for (int j = 1; j <= 42; j++) {
            board_state.add((byte)0);
        }
    }

    public ArrayList<Byte> legalMoves(ArrayList<Byte> board) {
        ArrayList<Byte> moves = new ArrayList<>();
        if (getState(board) != 0) {
            return moves;
        }
        for (byte j = 1; j <= 40; j++) {
            if (board.get(j) == 0) {
                moves.add(j);
            }
        }
        return moves;
    }

    public int simMove(byte m, ArrayList<Byte> board) {
        if (board.get(m) == 0) {
            board.set(m, board.get(0));
            boolean updated = updateScore(board);
            int ret = getState(board);
            //MUST CHECK IF LINE COMPLETED BOX, IF SO GO AGAIN
            if (!updated) {
                if (board.get(0) == 1) {
                    board.set(0, (byte)2);
                } else {
                    board.set(0, (byte)1);
                }
            }
            return ret;
        } else {
            return -1;
        }
    }

    public int getState(ArrayList<Byte> board) {
        if(board.get(41)>=9){
            return 1;
        }else if(board.get(42)>=9){
            return 2;
        }
        for (int j = 1; j <= 40; j++) {
            if (board.get(j) == 0) {
                return 0;
            }
        }
        return getWinner(board);
    }

    public int getWinner(ArrayList<Byte> board) {
        int diff = board.get(42) - board.get(41);
        if (diff < 0) {
            return 1;
        } else if (diff > 0) {
            return 2;
        } else {
            return -2;
        }
    }

    public boolean updateScore(ArrayList<Byte> board) {
        int p1score = board.get(41);
        int p2score = board.get(42);
        //board.set(41, 0);
        //board.set(42, 0);
        for (int j = 0; j < 16; j++) { //for each box
            int y = (j - (j % 4)) / 4;
            int x = (j % 4) + 1;
            int color = board.get((y * 9) + x); //top
            if (color != 0 && //top
                    board.get((y * 9) + 4 + x) != 0 && //left (y*9)+5+(x-1)     
                    board.get((y * 9) + 5 + x) != 0 && //left (y*9)+5+(x-1)   
                    board.get(((y + 1) * 9) + x) != 0) {
                if (color == 3) {
                    if (board.get((y * 9) + 4 + x) != 3) {
                        color = board.get((y * 9) + 4 + x);
                        board.set((y * 9) + 4 + x, (byte)3);
                    } else if (board.get((y * 9) + 5 + x) != 3) {
                        color = board.get((y * 9) + 5 + x);
                        board.set((y * 9) + 5 + x, (byte)3);
                    } else if (board.get(((y + 1) * 9) + x) != 3) {
                        color = board.get(((y + 1) * 9) + x);
                        board.set(((y + 1) * 9) + x, (byte)3);
                    }
                }
                //board.set(color + 40, board.get(color + 40) + 1);  //increment score
                if (color != 3) {
                    board.set(color + 40, (byte)(board.get(color + 40) + 1)); 
                }
            }
        }
        for(int j = 1; j <= 40; j++){
            if(board.get(j)!=0&&board.get(j)!=3){
                board.set(j, (byte)3);
            }
        }
        if (p1score < board.get(41) || p2score < board.get(42)) {
            return true;
        }

        return false;
    }

    public void printState(ArrayList<Byte> board) throws Exception {
        System.out.println("BOARD:\n");
        //for(int i = 0; i <= 2; i++)System.out.print(i+" | ");
        //System.out.print("\n");
        for (int i = 0; i <= 40; i += 9) {
            for (int j = 1; j <= 5; j++) {
                System.out.print("O");
                if (j < 5 && board.get(i + j) != 0) {
                    System.out.print("--");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println("");
            for (int k = 1; k <= 6 && i < 36; k++) {
                if (k < 6 && board.get(i + k + 4) != 0) {
                    System.out.print("|  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("");

        }
        System.out.println("Score:\n\tPlayer 1: " + board.get(41) + "\n\tPlayer 2: " + board.get(42));
    }
}

// ((j-(j%4))/4*9)+(j%4)+1
