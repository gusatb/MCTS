
import java.util.ArrayList;

public class Go9 extends Game {


//board state: 1 is top left point, goes right, 82 is number of consecutive passes, 83 is (0/else=no ko, move which is illegal), 84 is white stones captured by black, 85 is whites pts for capture


// moves 1-81 and 0 is pass

//Komi is 7.5, chinese rules
    public Go9() {
		restart();
    }

    public void restart() {
        board_state = new ArrayList<>(83);
        board_state.add((byte)1);
        for (int j = 1; j <= 81 + 4; j++) {
            board_state.add((byte)0);
        }
    }

    public ArrayList<Byte> legalMoves(ArrayList<Byte> board) {
        ArrayList<Byte> moves = new ArrayList<Byte>();
        if (getState(board) != 0) {
            return moves;
        }
		moves.add((byte)0);
		byte illegalMove = board.get(83);
		
		ArrayList<Byte> state = new ArrayList<>(board.size());
        for (Byte i : board) {
            state.add(i);
        }
		
        for (byte j = 1; j <= 81; j++) {
            if (board.get(j) != 0 || j == illegalMove) {
                continue;
            }
			int simReturn = simMove(j, state);
			for (int i = 0; i < board.size(); i++) {
                state.set(i, board.get(i));
            }
			if(simReturn == -1){
				continue;
			}
			moves.add((byte)j);
        }
        return moves;
    } 
	
//board state: 1 is top left point, goes right, 82 is number of consecutive passes, 83 is (0/else=no ko, move which is illegal), 84 is white stones captured by black, 85 is whites pts for capture
// moves 1-81 and 0 is pass
    public int simMove(byte m, ArrayList<Byte> board) {


		// if player passes
        if(m == 0){
			board.set(82, (byte)(board.get(82) + 1));
			if(board.get(82) == 2){
				// game over: 2 passes
				int state = getState(board);
				if (board.get(0) == 1) {
					board.set(0, (byte)2);
				} else {
					board.set(0, (byte)1);
				}
				return state;
			}else{
				if (board.get(0) == 1) {
					board.set(0, (byte)2);
				} else {
					board.set(0, (byte)1);
				}
				return 0;
			}
		}
		
		//Moving on a stone or illegal ko
		if(m < 0 || m > 81 || board.get(m) != 0 || m == board.get(83)){
			//System.out.println("Illegal: " + m + " = " + board.get(m));
			return -1;
		}
		
		byte turn = board.get(0);
		
		//place move check for captures
		board.set(m, turn);
		checkForCaptures(board, m);
		
		//check for suicide
		byte[] checkBoard = new byte[82];
		for(int j = 0; j < 82; j++){
			checkBoard[j] = 0;
		}
		if(!hasLiberties(board, m, checkBoard, (byte)(2 - turn))){
		//System.out.println("Suicide: " + m);
			board.set(m, (byte)0);
			return -1;
		}
		
		
		board.set(82, (byte)0);
		
        if (board.get(0) == 1) {
            board.set(0, (byte)2);
        } else {
            board.set(0, (byte)1);
        }
        return 0;
    }

    public int getState(ArrayList<Byte> board) {
		if(board.get(82) != 2){
			return 0;
		}
	
		int[] points = new int[2];
		
		//add points for captures
        points[0] = board.get(84);
		
		//white gets komi also
		points[1] = board.get(85) + 7;
		for(int j = 1; j <= 81; j++){
			byte spaceValue = board.get(j);
			if(spaceValue > 0){
				points[spaceValue - 1] += 1;
			}
		}
		
		
		//white wins tie (ie white gets a half pt)
		return (points[1] >= points[0] ? 2 : 1);
    }

    public void printState(ArrayList<Byte> board) {
        System.out.println("BOARD:\n");
        for (int j = 0; j < 9; j++) {
            for (int i = 0; i < 9; i++) {
                System.out.print(board.get(j*9 + i + 1) + " ");
            }
            System.out.print("\n");
        }
    }
	
	public class Point{
		byte x, y;
			
		public Point(int x, int y){
			this.x = (byte)x;
			this.y = (byte)y;
		}
	}

	
	final Point[] dirs = {new Point(-1, 0), new Point(0, -1), new Point(1, 0), new Point(0, 1)};
	
	
	// Go functions
	
	    int indexAndDir(int p, int j){
            int nextIndex = p + dirs[j].y * 9 + dirs[j].x;
            if((j == 0 && nextIndex % 9 == 0) || (j == 2 && nextIndex % 9 == 8) || (nextIndex <= 0) || ( nextIndex > 81)){
                return -1;
            }
            return nextIndex;
        }

        // p is the index in the board of a stone just played
        void checkForCaptures(ArrayList<Byte> board, int p){
			byte turn = (byte)(board.get(p) - 1);
            byte[] checkBoard = new byte[82];
            for(int j = 0; j < 4; j++){
                for(int i = 0; i < 82; i++){
                    checkBoard[i] = 0;
                }
                int nextIndex = indexAndDir(p, j);
                if(nextIndex != -1 && board.get(nextIndex) == 2-turn){
                    if(!hasLiberties(board, nextIndex, checkBoard, turn)){
                        captureGroup(board, nextIndex, turn);
                    }
                }
            }
        }

        void captureGroup(ArrayList<Byte> board, int p, byte turn){
            board.set(p, (byte)0);
            board.set(84 + turn, (byte)(board.get(84 + turn) + 1));
            for(int j = 0; j < 4; j++){
                int nextIndex = indexAndDir(p, j);
                if(nextIndex != -1 && board.get(nextIndex) == 2-turn){
                    captureGroup(board, nextIndex, turn);
                }
            }
        }

        // p is the index in the board of a stone in the group to count liberties for
        boolean hasLiberties(ArrayList<Byte> board, int p, byte[] checkBoard, byte turn){
		//System.out.println("hasLib: checking " + p);
            checkBoard[p] = 1;
            for(int j = 0; j < 4; j++){
                int nextIndex = indexAndDir(p, j);
                if(nextIndex != -1 && checkBoard[nextIndex] == 0){
                    if(board.get(nextIndex) == 0){
                        return true;
                    }else if(board.get(nextIndex) == 2-turn){
                        if(hasLiberties(board, nextIndex, checkBoard, turn)){
							return true;
						}
                    }
					
                }
            }
            return false;
        }
}
