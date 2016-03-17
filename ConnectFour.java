
import java.util.ArrayList;

public class ConnectFour extends Game {
//board state: 0 is bottom row left space, increasing going right. 7 is second row, 14 is third row.... 0 is empty space, 1 is p1, 2 is p2

    public ConnectFour() {
        board_state = new ArrayList<>(43);
        board_state.add((byte)1);
        for (int j = 1; j < 43; j++) {
            board_state.add((byte)0);
        }
    }

    public void restart() {
        board_state = new ArrayList<>(43);
        board_state.add((byte)1);
        for (int j = 1; j < 43; j++) {
            board_state.add((byte)0);
        }
    }

    public ArrayList<Byte> legalMoves(ArrayList<Byte> board) {
        ArrayList<Byte> moves = new ArrayList<Byte>();
        if (getState(board) != 0) {
            return moves;
        }
        for (int j = 36; j < 43; j++) {
            if (board.get(j) == 0) {
                moves.add((byte)(j - 36));
            }
        }
        return moves;
    }

    public int simMove(byte m, ArrayList<Byte> board) { // moves 0-6 corresponding to column left to right, index 0 is player who just moved
        int j;
        for (j = 6; j >= 1 && board.get(7 * (j - 1) + m + 1) == 0; j--) {
        }
        if (j == 6) {
            return -1;
        }
        board.set(7 * (j) + m + 1, board.get(0));

        int state = getState(board);
        if (board.get(0) == 1) {
            board.set(0, (byte)2);
        } else {
            board.set(0, (byte)1);
        }
        return state;
    }

    public int getState(ArrayList<Byte> board) {
        for (int j = 1; j < 43; j++) {
            if (board.get(j) != 0) {

                int color = board.get(j);
                int i;
                int row;
//check hor 
                if (j % 7 == 4) { //huge optimization
                    row = 1;
                    for (i = j; (i - 1) % 7 != 0 && board.get(i - 1) == color && i > 1; i--) {
                    }
                    while (((i + 1) % 7) != 1 && board.get(i + 1) == color) {
                        i++;
                        row++;
                        if (row == 4) {
                            return color;
                        }
                    }
                }
//check vert
                if ((j - (j % 7))/7 == 2) { //huge optimization
                    row = 1;
                    for (i = j; (i - 7) > 0 && board.get(i - 7) == color; i -= 7) {
                    }
                    while ((i + 7) <= 42 && board.get(i + 7) == color) {
                        i += 7;
                        row++;
                        if (row == 4) {
                            return color;
                        }
                    }

//check down-left diag
                    row = 1;
                    for (i = j; (i - 8) > 0 && (i - 8) % 7 != 0 && board.get(i - 8) == color; i -= 8) {
                    }
                    while ((i + 8) <= 42 && (i + 8) % 7 != 1 && board.get(i + 8) == color) {
                        i += 8;
                        row++;
                        if (row == 4) {
                            return color;
                        }
                    }

//check down-right diag
                    row = 1;
                    for (i = j; (i - 6) > 0 && (i - 6) % 7 != 1 && board.get(i - 6) == color; i -= 6) {
                    }
                    while ((i + 6) <= 42 && (i + 6) % 7 != 0 && board.get(i + 6) == color) {
                        i += 6;
                        row++;
                        if (row == 4) {
                            return color;
                        }
                    }
                }

            }
        }
        //changed
        for (int j = 36; j < 43; j++) {
            if (board.get(j) == 0) {
                return 0;
            }
        }
        return -2;
    }

    public void printState(ArrayList<Byte> board) {
        System.out.println("BOARD:\n");
        for (int i = 0; i <= 6; i++) {
            System.out.print(i + " | ");
        }
        System.out.print("\n");
        for (int j = board.size() - 7; j >= 1; j -= 7) {
            for (int i = 0; i <= 6; i++) {
                System.out.print(board.get(j + i) + " | ");
            }
            System.out.print("\n");
        }
    }
}
