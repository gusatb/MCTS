
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
public class MCTSPlayer extends Player {

    MCTS ai;
    int seconds;
    int playouts;
    //int rounds;

    public MCTSPlayer(int seconds, int playouts) {
        this.seconds = seconds;
        this.playouts = playouts;
        ai = null;
    }

    public void restart() {
        //System.out.println("Reset");
        ai = null;
    }

    @Override
    public void update(byte lastMove) {
        if (ai != null) {
            boolean changed = false;
            for (Node n : ai.start.children) {
                if (lastMove == n.move) {
                    //System.out.println("Changed");
                    ai.start = n;
                    ai.start.parent = null;
                    changed = true;
                    break;
                }
            }
            if (!changed) {
                restart();
                //System.out.println("Resetting");
            }
        }
    }

    public byte getMove(byte lastMove, ArrayList<Byte> board) throws Exception {
        //boolean first = false;

        if (ai == null) {
            //System.out.println("START");
            ai = new MCTS(cf, board.get(0), board, playouts);
            //  first = true;
        } else if (lastMove != -1) {
            boolean changed = false;
            for (Node n : ai.start.children) {
                if (lastMove == n.move) {
                    //System.out.println("Changed");
                    ai.start = n;
                    ai.start.parent = null;
                    changed = true;
                    break;
                }
            }
            if (!changed) {
                //System.out.println("Reset2");
                ai = new MCTS(cf, board.get(0), board, playouts);
            }

        }

        if (!equalLists(ai.start.state, board)) { //implies the game is Dots&Boxes and a player went twice
            System.out.println("PROBLEM");
         //ai = new MCTS(cf, board.get(0), board);
            //ai.playouts = playouts;
        }
        //System.out.println(ai.start.sims);
        //int c = 0;
        boolean solved = false;
        long start = System.currentTimeMillis();
        while ((!solved) && (System.currentTimeMillis() - start) <= seconds * 100) {
            solved = ai.round();
            //c++;
            //if (c % 10000 == 0) System.out.println(c);

        }
        //System.out.println("Rounds:"+c);
        //System.out.println("Sims:"+ai.start.sims);
        byte move = ai.getMove();
        //boolean changed = false;
        for (Node n : ai.start.children) {
            if (move == n.move) {
                ai.start = n;
                ai.start.parent = null;
                //changed = true;
                break;
            }
        }
            //if(!changed){
        //System.out.println("NC2");
        //ai = new MCTS(cf, board.get(0), board);
        //}

        //System.out.println(move);
        return move;
    }

    public boolean equalLists(ArrayList<Byte> l1, ArrayList<Byte> l2) {
        for (int j = 0; j < l2.size(); j++) {
            if (l1.get(j) != l2.get(j)) {
                return false;
            }
        }
        return true;
    }

    public boolean equalMoves(ArrayList<Node> l1, ArrayList<Byte> l2) {
        for (int j = 0; j < l1.size(); j++) {
            if (l2.get(l1.get(j).move) != 0) {
                return false;
            }
        }
        return true;
    }

}
