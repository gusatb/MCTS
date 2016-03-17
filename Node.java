
import java.util.ArrayList;

 public class Node {

        byte move;
        int wins;
        Node parent;
        ArrayList<Node> children;
        long sims;
        ArrayList<Byte> state;
        int finished = 0; //0 = not fininshed, otherwise equals winner

        public Node(Node parent, ArrayList<Byte> state, byte move) {
            this.parent = parent;
            this.state = state;
            this.move = move;
            children = new ArrayList<>();
        }
    }