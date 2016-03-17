
import java.util.ArrayList;

public class MCTS {

    final int branch = 81; //set to max
    final double epsilon = 1e-5;
    int playouts;
    Game game;
    int player;
    Node start;
    //ArrayList<Node> nodes;

    public MCTS(Game game, int player, ArrayList<Byte> board, int playouts){
        this.playouts = playouts;
        this.game = game;
        this.player = player;
        start = new Node(null, board, (byte)-1);
        //nodes = new ArrayList<>();
        //nodes.add(start);

    }

    public byte getMove() {
        byte move = start.children.get(0).move;
        long high = -2;
        for (int j = 0; j < start.children.size(); j++) {
            Node n = start.children.get(j);
            if (n.finished == player) {
                return n.move;
            } else if (n.finished == 0 && n.sims > high) {
                high = n.sims;
                move = n.move;
            } else if (n.finished == 3 && -1 > high) { //can draw
                high = -1;
                move = n.move;
            }
        }
        return move;
    }

    public boolean round() { //returns solved
        if (start.finished != 0) {
            System.out.print("(SOLVED FOR: " + start.finished+") ");
            return true;
        }
        Node n = selection();

        if (n == null) { //solved the game?
            //System.out.println("SOLVED");
            //return true;
            return false;
        }

        Node leaf = expansion(n);
        if (leaf == null) {
            leaf = n;
            //System.out.println("Reached Leaf");
        }

        for (int j = 0; j < playouts; j++) {
            int result = simulation(leaf);

            if (j==0 && leaf == n) {

                if (result == -2) {
                    result = 3;
                }
                if(n.parent != null) n.parent.finished = result;
                n.finished = result;
                j = playouts;
            //if(n.parent == start){
                //    System.out.println("QUICK-SOLVED");
                //}
            }

            Node p = leaf;
            while (p != null) {
                if (result == player) {
                    p.wins++;
                }
                p.sims++;
                p = p.parent;
            }
        }
        
        //save memory
        //byte temp = leaf.parent.state.get(0);
        //leaf.parent.state = new ArrayList<>(1); 
        //leaf.parent.state.add(temp);
        
                
        return false;
    }

    public Node selection() { //select a leaf node return index

        Node s = start;
        while (s.children.size() != 0) {
            int index = -1;
            double high = -2;
            int sumfinished = 0;
            int finishedtype = -1;
            for (int j = 0; j < s.children.size(); j++) {
                Node n = s.children.get(j);

                long wins = n.wins;
                if (s.state.get(0) != player) {
                    wins = n.sims - n.wins;//opponents move
                }
                if (n.finished == 0) {
                    double ucb = wins / (n.sims + epsilon) + Math.sqrt((Math.log(s.sims + 2) / (n.sims + epsilon)));
                    if (ucb > high) {
                        high = ucb;
                        index = j;
                    }
                } else {
                    if (finishedtype == -1) {
                        finishedtype = n.finished;
                        sumfinished += n.finished;
                        if (n.finished == s.state.get(0)) {
                            //index = j;
                            //high = Double.POSITIVE_INFINITY;
                            s.finished = n.finished;
                            return null;
                        } else if (n.finished == 3 && -1 > high) {
                            high = -1;
                            index = j;
                        }
                    } else if (finishedtype == n.finished) {
                        sumfinished += n.finished;

                    } else {
                        finishedtype = -2;
                        sumfinished = 0;
                        if (n.finished == s.state.get(0)) {
                            //index = j;
                            //high = Double.POSITIVE_INFINITY;
                            s.finished = n.finished;
                            return null;
                        } else if (n.finished == 3 && -1 > high) {
                            high = -1;
                            index = j;
                        }
                    }

                }
            }
            int opp = 2;
            if (s.state.get(0) == 2) {
                opp = 1;
            }
            if (finishedtype == opp && sumfinished == s.children.size() * opp) {
                //solved for opponent
                s.finished = opp;
                //if(s.parent!=null){
                //    s = s.parent;
                return null;
                //}else{
                //    return null;
                //}
            } else if (high == -1) { //best case draw
                s.finished = 3;
                return null;
            } else if (index != -1) {
                s = s.children.get(index);
            } else if (index == -1) {
                System.out.println("SOMETHINGS WRONG");
                return null;
            }
        }
        return s;
    }

    public Node expansion(Node n) { // choose child of leaf node

        ArrayList<Byte> moves = game.legalMoves(n.state);
        Node c = null;
        int index;
        while (!moves.isEmpty() && n.children.size() < branch) {
            index = (int) (Math.random() * moves.size());
            ArrayList<Byte> state = new ArrayList<>(n.state.size());
            for (Byte i : n.state) {
                state.add(i);
            }
            game.simMove(moves.get(index), state);
            c = new Node(n, state, moves.get(index));
            n.children.add(c);
            moves.remove(index);
            //nodes.add(c);
        }
        
        if (c == null) {
            return null;
        }
        return c;
    }

    public int simulation(Node n) { //random playout until end, returns player who won, -1 for draw
        int result = result = game.getState(n.state);
        if (result != 0) {
            return result;
        }
        ArrayList<Byte> board = new ArrayList<>(n.state.size());
        for (Byte i : n.state) {
            board.add(i);
        }
        ArrayList<Byte> moves;

        while (result == 0) {
            moves = game.legalMoves(board);
            if (moves.isEmpty()) {
                return -1;
            }
            result = game.simMove(moves.get((int) (Math.random() * moves.size())), board);
        }
        return result;

    }

}
