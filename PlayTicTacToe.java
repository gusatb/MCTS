
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
public class PlayTicTacToe {

    TicTacToe cf;
    MCTS ai;
    int status;

    public static void main(String[] args) throws Exception {
        int play = 1;
        while (play != 0) {
            play = (new PlayTicTacToe()).status;
        }

    }

    public PlayTicTacToe() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        cf = new TicTacToe();
        ArrayList<Byte> board;
        status = 1;
        while (status == 1) {
            board = new ArrayList<>(10);
            for (Byte i : cf.board_state) {
                board.add(i);
            }
            boolean solved = false;
            int c = 0;
            ai = new MCTS(cf, 1, board);
            while (!solved && c < 400000) { //guarentee solve for <=400000
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
            } else if (stat == -2) {
                status = 2;
                System.out.println("DRAW!");
                continue;
            }
            System.out.println("BOARD:\n");
            //for(int i = 0; i <= 2; i++)System.out.print(i+" | ");
            //System.out.print("\n");
            for (int j = 1; j <= 7; j += 3) {
                for (int i = 0; i <= 2; i++) {
                    System.out.print(cf.board_state.get(j + i) + " | ");
                }
                System.out.print("\n_______________\n");
            }
            System.out.print("\nP2: ");
            do {
                String s_move = bf.readLine();
                if (s_move.equals("quit")) {
                    status = 0;
                    continue;
                }
                if (s_move.equals("break")) {
                    System.out.println("");
                    stat = -1;
                    continue;
                }
                byte my_move = (Byte.parseByte(s_move));
                //System.out.println( + my_move);
                stat = cf.simMove(my_move, cf.board_state);
                if (stat == 2) {
                    status = 2;
                    System.out.println("P2 WINS");
                } else if (stat == -2) {
                    status = 2;
                    System.out.println("DRAW!");
                    continue;
                }
            } while (stat == -1);

        }
        /*
         while(true){
         board = new ArrayList<>(10);
         for(Integer i: cf.board_state){
         board.add(i);
         }
         ai = new MCTS(cf, 1, board);
         int j = 0;
         boolean solved = false;
         while(!solved && j < 500000){
         solved = ai.round();
         j++;
         }
            
         int ai_move = ai.getMove();
         System.out.println("P1: " + ai_move);
         cf.makeMove(ai_move);
         int my_move = (Integer.parseInt(bf.readLine()));
         System.out.println("P2: " + my_move);
         cf.makeMove(my_move);
         }
         */
    }
}
