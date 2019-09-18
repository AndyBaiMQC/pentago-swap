package student_player;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;



public class MyTools {
    public final static int WHITE = 1;
    public final static int BLACK = 2;
    public final static int BLANK = 0;
    private final static int WIN = 0;
    private final static int ACTIVE4 = 1;
    private final static int INACTIVE4 = 2;
    private final static int ACTIVE3 = 3;
    private final static int INACTIVE3 = 4;
    private final static int ACTIVE2 = 5;
    private final static int INACTIVE2 = 6;
    private final static int OL1 = 7;
    private final static int NONE = 8;
    private final static String[] WIN_WHITE = {"11111"};//in order to easy present,0 means blank,1 means white,2 means black;
    private final static String[] WIN_BLACK = {"22222"};
    private final static String[] ACTIVE4_WHITE = {"011110"};
    private final static String[] ACTIVE4_BLACK = {"022220"};
    private final static String[] INACTIVE4_WHITE = {"011112", "211110", "10111", "11011", "11101"};
    private final static String[] INACTIVE4_BLACK = {"022221", "122220", "20222", "22022", "22202"};
    private final static String[] ACTIVE3_WHITE = {"001110", "011100", "010110", "011010"};
    private final static String[] ACTIVE3_BLACK = {"002220", "022200", "020220", "022020"};
    private final static String[] INACTIVE3_WHITE = {"001112", "010112", "011012", "011102", "211100", "211010", "210110", "201110", "00111", "10011", "10101", "10110", "01011", "10011", "11001", "11010", "01101", "10101", "11001", "11100",};
    private final static String[] INACTIVE3_BLACK = {"002221", "020221", "022021", "022201", "122200", "122020", "120220", "102220", "00222", "20022", "20202", "20220", "02022", "20022", "22002", "22020", "02202", "20202", "22002", "22200",};
    private final static String[] ACTIVE2_WHITE = {"000110", "001010", "001100", "001100", "010100", "011000", "000110", "010010", "010100", "001010", "010010", "011000",};
    private final static String[] ACTIVE2_BLACK = {"000220", "002020", "002200", "002200", "020200", "022000", "000220", "020020", "020200", "002020", "020020", "022000",};
    private final static String[] INACTIVE2_WHITE = {"000112", "001012", "010012", "10001", "2010102", "2011002", "211000", "210100", "210010", "2001102"};
    private final static String[] INACTIVE2_BLACK = {"000221", "002021", "020021", "20002", "1020201", "1022001", "122000", "120200", "120020", "1002201"};
    private final static String[] OL1_WHITE = {"1"};
    private final static String[] OL1_BLACK = {"2"};
    private final static String[] NONE_ = {""};
    private final static int BOARD_SIZE = 6;
    int score;
    /*
     * alpha-beta prunning
     */
    public static int alphabeta(PentagoBoardState tmp,int player,long endtime, int alpha ,int beta,int depth) {
   	 int [][] board = new int[6][6];
   	  board = getBoard(tmp);
   	 for(int i = 0; i < BOARD_SIZE; i++) {
         for (int j = 0; j < BOARD_SIZE; j++) {
             if (board[i][j] != BLANK) {
                 List<String> list = getString(i,j,board);
                 if (CheckPieceStatus(WHITE, list, WIN)) {
                     return  100000000;
                 }
                 if (CheckPieceStatus(BLACK, list, WIN)) {
                     return -100000000;
                 }
             }
         }
	}//check game over;
       if (depth == 0) {
           return getScore(board);
       }
        if(player == 0) {
        	if (System.currentTimeMillis() > endtime) {
				return alpha;
			}
    	   ArrayList<PentagoMove> move = tmp.getAllLegalMoves();
       	for(int i=0;i<move.size();i++)
       	{
       		PentagoBoardState tmp2 =  (PentagoBoardState)tmp.clone();
       		tmp2.processMove(move.get(i));
       		int val = alphabeta(tmp2,1-player, endtime, alpha,beta,depth-1);
       		alpha = Math.max(alpha,val);
       		if(beta<=alpha) break; //cut
       	}
       	return alpha;
       	}
       else {
    	   ArrayList<PentagoMove> move = tmp.getAllLegalMoves();
       	for( int i=0;i<move.size();i++)
       	{
       		if (System.currentTimeMillis() > endtime) {
				return beta;
			}
       		PentagoBoardState tmp2 =  (PentagoBoardState)tmp.clone();
       		tmp2.processMove(move.get(i));
       		int val = alphabeta(tmp2,1-player,endtime, alpha,beta,depth-1);      		
       		beta = Math.min(beta, val);
       		if (alpha >=beta) break;//cut
       	}
       	return beta;
       }
   }
    public static int[][] getBoard(PentagoBoardState boardState)
    {
    	int[][] board  = new int[6][6];
    	 for(int j=0;j<6;j++)
		 {
			 for(int k=0;k<6;k++)
			 {
				 Piece p = boardState.getPieceAt(j,k);
				 if(p==Piece.EMPTY)
				 {
					 board[j][k] = 0;
				 }
				 if(p==Piece.WHITE)
				 {
					 board[j][k] = 1;
				 }
				 if(p==Piece.BLACK)
				 {
					 board[j][k] = 2;
				 }
			 }
		 }
    	return board;
    	
    }
    //my evaluate function.
    public static int getScore(int[][] board) {
        int score = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != BLANK) {
                    List<String> list = getString(i,j,board);
                    if (CheckPieceStatus(WHITE, list, WIN)) {
                        score += 100000000;
                        return score;
                    }
                    if (CheckPieceStatus(BLACK, list, WIN)) {
                        score += -100000000;
                        return score;
                    }
                    if (CheckPieceStatus(WHITE, list, ACTIVE4)) {
                        score += 10000000;
                    }
                    if (CheckPieceStatus(BLACK, list, ACTIVE4)) {
                        score += -10000000;
                    }
                    if (CheckPieceStatus(WHITE, list, INACTIVE4)) {
                        score += 1000000;
                    }
                    if (CheckPieceStatus(BLACK, list, INACTIVE4)) {
                        score += -1000000;
                    }
                    if (CheckPieceStatus(WHITE, list, ACTIVE3)) {
                        score += 100000;
                    }
                    if (CheckPieceStatus(BLACK, list, ACTIVE3)) {
                        score += -100000;
                    }
                    if (CheckPieceStatus(WHITE, list, INACTIVE3)) {
                        score += 10000;
                    }
                    if (CheckPieceStatus(BLACK, list, INACTIVE3)) {
                        score += -10000;
                    }
                    if (CheckPieceStatus(WHITE, list, ACTIVE2)) {
                        score += 1000;
                    }
                    if (CheckPieceStatus(BLACK, list, ACTIVE2)) {
                        score += -1000;
                    }
                    if (CheckPieceStatus(WHITE, list, INACTIVE2)) {
                        score += 100;
                    }
                    if (CheckPieceStatus(BLACK, list, INACTIVE2)) {
                        score += -100;
                    }
                    if (CheckPieceStatus(WHITE, list, OL1)) {
                        score += 10;
                        if(i%3==1&&j%3==1)
                        {score += 20;}
                    }
                    if (CheckPieceStatus(BLACK, list, OL1)) {
                        score += -10;
                        if(i%3==1&&j%3==1)
                        {	
                        	score += -20;
                        }
                    }
                    if (CheckPieceStatus(WHITE, list, NONE)) {
                        score += 1;
                        
                    }
                    if (CheckPieceStatus(BLACK, list, NONE)) {
                        score += -1;
                        
                    }
                }
            }
        }
        return score;
    }
    //check piece status;
    public static boolean CheckPieceStatus(int p, List<String> list, int type) {
        switch (type) {
            case WIN:
                if (p == WHITE) { // five-in-line
                    if (checkString(list, WIN_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // five-in-line
                    if (checkString(list, WIN_BLACK)) {
                        return true;
                    }
                }
                return false;
            case ACTIVE4:
                if (p == WHITE) { // four-in-line
                    if (checkString(list, ACTIVE4_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // four-in-line
                    if (checkString(list, ACTIVE4_BLACK)) {
                        return true;
                    }
                }
                return false;
            case INACTIVE4:
                if (p == WHITE) { // four-in-line
                    if (checkString(list, INACTIVE4_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // four-in-line
                    if (checkString(list, INACTIVE4_BLACK)) {
                        return true;
                    }
                }
                return false;
            case ACTIVE3:
                if (p == WHITE) { // three-in-line
                    if (checkString(list, ACTIVE3_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // three-in-line
                    if (checkString(list, ACTIVE3_BLACK)) {
                        return true;
                    }
                }
                return false;
            case INACTIVE3:
                if (p == WHITE) { // three-in-line
                    if (checkString(list, INACTIVE3_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // three-in-line
                    if (checkString(list, INACTIVE3_BLACK)) {
                        return true;
                    }
                }
                return false;
            case ACTIVE2:
                if (p == WHITE) { // two-in-line
                    if (checkString(list, ACTIVE2_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // two-in-line
                    if (checkString(list, ACTIVE2_BLACK)) {
                        return true;
                    }
                }
                return false;
            case INACTIVE2:
                if (p == WHITE) { // two-in-line
                    if (checkString(list, INACTIVE2_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) { // two-in-line
                    if (checkString(list, INACTIVE2_BLACK)) {
                        return true;
                    }
                }
                return false;
            case OL1:
                if (p == WHITE) {
                    if (checkString(list, OL1_WHITE)) {
                        return true;
                    }
                } else if (p == BLACK) {
                    if (checkString(list, OL1_BLACK)) {
                        return true;
                    }
                }
                return false;
            case NONE:
                if (p == WHITE) {
                    if (checkString(list, NONE_)) {
                        return true;
                    }
                } else if (p == BLACK) {
                    if (checkString(list, NONE_)) {
                        return true;
                    }
                }
                return false;
        }
        return true;
    }

    /**
     * check pieces string
     */
    public static boolean checkString(List<String> list, String[] situation) {
        for (String str : list) {
            for (int i = 0; i < situation.length; i++) {
                if (str.contains(situation[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * x, y direction (|-- / \)
 
     */
    public static List<String> getString(int x, int y,int [][] board) {
        List<String> strings = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, j = y; i < BOARD_SIZE; i++) { // direction:--
            sb.append(board[i][j]);
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        for (int i = x, j = 0; j < BOARD_SIZE; j++) { //direction:|
            sb.append(board[i][j]);
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        // direction:/
        if (x + y < BOARD_SIZE) { 
            for (int i = 0, j = x + y; i < BOARD_SIZE && j >= 0; i++, j--) {
                sb.append(board[i][j]);
            }
        } else { //
            for (int i = x + y - 3, j = BOARD_SIZE - 1; i < BOARD_SIZE && j >= 0; i++, j--) {
                sb.append(board[i][j]);
            }
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        // direction:\
        if (x <= y) {
            for (int i = 0, j = y - x; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {  // 
                sb.append(board[i][j]);
            }
        } else {
            for (int i = x - y, j = 0; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {  //
                sb.append(board[i][j]);
            }
        }
        strings.add(sb.toString());
        sb.delete(0, sb.length());
        return strings;
    }

}