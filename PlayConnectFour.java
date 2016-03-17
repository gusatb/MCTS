
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
public class PlayConnectFour {

    ConnectFour cf;
    MCTS ai;
    int status;

    public static void main(String[] args) throws Exception {
        int play = 1;
        while (play != 0) {
            play = (new PlayConnectFour()).status;
        }
    }

    public PlayConnectFour() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        cf = new ConnectFour();
        ArrayList<Byte> board;
        status = 1;
        while (status == 1) {
            board = new ArrayList<>(43);
            for (Byte i : cf.board_state) {
                board.add(i);
            }
            boolean solved = false;
            int c = 0;
            ai = new MCTS(cf, 1, board);
            while((!solved)&&c<50000) {
                solved = ai.round();
                c++;
            }
            byte ai_move = ai.getMove();
            System.out.println("\nP1: " + ai_move);
            int stat = cf.simMove(ai_move, cf.board_state);
            if (stat == 1) {
                status = 2;
                System.out.println("P1 WINS");
                continue;
            } else if (stat == -1) {
                status = 2;
                System.out.println("ILLEGAL MOVE! P1 WINS");
                continue;
            }
            System.out.println("BOARD:\n");
            for(int i = 0; i <= 6; i++)System.out.print(i+" | ");
            System.out.print("\n");
            for(int j = cf.board_state.size()-7; j >= 1; j-=7){
                for(int i = 0; i <= 6; i++)System.out.print(cf.board_state.get(j+i)+" | ");
                System.out.print("\n");
            }
            System.out.print("\nP2: ");
            do {
                String s_move = bf.readLine();
                if (s_move.equals("quit")) {
                    status = 0;
                    continue;
                }else if (s_move.equals("break")) {
                    stat = -1;
                    continue;
                }
                byte my_move = (Byte.parseByte(s_move));
                //System.out.println( + my_move);
                stat = cf.simMove(my_move, cf.board_state);
                if (stat == 2) {
                    status = 2;
                    System.out.println("P2 WINS");
                }
            } while (stat == -1);
            
        }
    }
}
