
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
public class PlayGame {

    //MCTS ai;
    int status;
    int mode; // 0 = play else multirun
    int p1wins = 0;
    int draws = 0;

    public static void main(String[] args) throws Exception {
        int play = 1;
        while (play != 0) {
            play = (new PlayGame()).status;
        }
    }

    public PlayGame() throws Exception {
        Game cf;
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Select mode: \n\t0: Regular\n\telse: Quick-run\n>");

        int mode = Integer.parseInt(bf.readLine());

        System.out.print("\nSelect game:\n\t1: Tic-Tac-Toe\n\t2: Connect Four\n\t3: Dots & Boxes\n\t4: Go 9x9\n>");

        int game = Integer.parseInt(bf.readLine());
        //game initialized again in gn loop
        if (game == 1) {
            cf = new TicTacToe();
        } else if (game == 2) {
            cf = new ConnectFour();
        } else if(game == 3) {
            cf = new DotsAndBoxes();
        } else{
			cf = new Go9();
		}

        Player[] players = new Player[2];
        for (int p = 0; p < 2; p++) {
            System.out.print("\nPlayer " + (p + 1) + ":\n\t0: Human\n\t1: MCTS\n\t-1: Random\n\t-2: Copy\n>");
            int p1choose = Integer.parseInt(bf.readLine());
            if (p1choose == 0) {
                players[p] = new HumanPlayer();
            } else if (p1choose == -1) {
                players[p] = new RandomPlayer();
                players[p].cf = cf;
            } else if (p1choose == -2) {
                players[p] = new CopyPlayer();
                players[p].cf = cf;
            } else if(p1choose == 1){ //if (p1choose > 0 && p1choose <= 10)
                System.out.print("\tTime per move (seconds/10): ");
                int secs = Integer.parseInt(bf.readLine());
                System.out.print("\tPlayouts: ");
                int plays = Integer.parseInt(bf.readLine());
                players[p] = new MCTSPlayer(secs, plays);
                players[p].cf = cf;
            }/*else {
             players[p] = new MCTSPlayer(p1choose);
             players[p].cf = cf;
             }*/

        }
        int games = mode;
        if (mode == 0) {
            games = 1;
        }
        for (int gn = 0; gn < games; gn++) {
            System.out.println("Game " + (gn + 1) + ":");
            cf.restart();
            players[0].restart();
            players[1].restart();
            ArrayList<Byte> board;
            status = 1;
            byte lastMove = -1;
            byte lastPlayer = -1;
            while (status == 1) {
                //for (int p = 0; p < 2 && status == 1; p++) {
                //if (cf.board_state.get(0) == p + 1) {
                int p = cf.board_state.get(0)-1;
                if(lastPlayer==p){
                   players[1-p].update(lastMove);
                   lastMove = -1;
                }
                lastPlayer = (byte)p;
                board = new ArrayList<>(cf.board_state.size());
                for (Byte i : cf.board_state) {
                    board.add(i);
                }
                int stat = 0;

                do {
                    if (mode == 0) {
                        cf.printState(board);
                        System.out.print("Player " + (p + 1) + ": ");
                    }else{
                        System.out.print("P" + (p + 1) + ": ");
                    }
                    byte pmove = players[p].getMove(lastMove, board);
                    lastMove = pmove;
                    //if (mode == 0) {
                        System.out.println(pmove);
                    //}
                    if (pmove == -1) {
                        status = 2;
                        break;
                    }
                    stat = cf.simMove(pmove, cf.board_state);
                    if (stat == -1 && players[p].cf != null) {
                        //if (mode == 0) {
                        cf.printState(cf.board_state);
                        System.out.println("Illegal move by Player " + (p + 1));
                        //}
                        status = 2;
                        break;
                    } else if (stat > 0) {
                        if (mode == 0) {
                            cf.printState(cf.board_state);
                            System.out.println("Player " + stat + " WINS");
                        } else if (stat == 1) {
                            p1wins++;
                        }
                        status = 2;
                        break;
                    } else if (stat == -2) {
                        if (mode == 0) {
                            cf.printState(cf.board_state);
                            System.out.println("DRAW!");
                        } else {
                            draws++;
                        }
                        status = 2;
                        break;
                    }
                } while (stat == -1);
            }
            //}
            //}
        }
        if (mode != 0) {
            System.out.println("Results:\n\tPlayer 1 wins: " + p1wins + "\n\tPlayer 2 wins: " + (mode - p1wins - draws) + "\n\tDraws: " + draws + "\n");
        }
    }
}

/*


 board = new ArrayList<>(cf.board_state.size());
 for (Integer i : cf.board_state) {
 board.add(i);
 }
 stat = 0;
            
 do {
 cf.printState(board);
 System.out.println("\nP2: ");
 int p2move = players[1].getMove(board);
                
 if (p2move==-1) {
 status = 0;
 continue;
 }else if (p2move==-2) {
 stat = -1;
 continue;
 }
 stat = cf.simMove(p2move, cf.board_state);
 if (stat == 2) {
 status = 2;
 System.out.println("P2 WINS");
 }else if(stat == 3){
 status = 2;
 System.out.println("DRAW!");
 }
 } while (stat == -1);


 */
