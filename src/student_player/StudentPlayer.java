package student_player;

import java.util.ArrayList;
import java.util.List;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoBoardState.Piece;
import pentago_swap.PentagoMove;
/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260562421");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public int [][] board = new int[6][6];
    public int max_score=0;
    public int score;
    public int id = 0;
    public static int WHITE = 1;
    public static int BLACK = 2;
    public static int BLANK = 0;
    public static int BOARD_SIZE = 6;
    private static final int INFINITY = 1000000000;
    
    public int bestMove(PentagoBoardState tmp,long starttime,long endtime)
    {
    	board = MyTools.getBoard(tmp);
    	max_score=0;
    	id = 0;
    	int bestscore = 0; 
    	 if(tmp.getOpponent()==1)
    		 max_score = -INFINITY;
    	 if(tmp.getOpponent()==0)
    		 max_score = INFINITY;
    	 if(tmp.getOpponent()==1)
    		 bestscore = -INFINITY;
    	 if(tmp.getOpponent()==0)
    		 bestscore = INFINITY;
    	PentagoBoardState tmp2 =(PentagoBoardState) tmp.clone();
    	
        ArrayList<PentagoMove> moves_tmp = tmp.getAllLegalMoves();
        for(int i=0;i<moves_tmp.size();i++)
         {
        	long current = System.currentTimeMillis();
        	if (current > endtime) {
        		System.out.println("time limit 2s.");
				return id;
			}
			
        	 tmp2 =(PentagoBoardState) tmp.clone();
        	 PentagoMove testMove = moves_tmp.get(i);
        	 tmp2.processMove(testMove);
        	 score = MyTools.alphabeta(tmp2, 1-testMove.getPlayerID(),endtime, -INFINITY, INFINITY, 1);
        		 if(tmp.getOpponent()==1)
        		 {
        			 // System.out.println(i+":score:"+score);
        			 if(score>max_score)
        			 {
        				 max_score=score;
        				 //System.out.println(i+":score:"+score);
        				 id = i;
        			 }
        			 if(score == max_score) {
        				 if(bestscore < MyTools.getScore(MyTools.getBoard(tmp2)))
        				 {
        					 bestscore =  MyTools.getScore(MyTools.getBoard(tmp2));
        					 id = i;
        					// System.out.println("  test:"+bestscore);
        				 }
        			 }
        		 }
        		 if(tmp.getOpponent()==0)
        		 {
        			 //System.out.println(i+":score:"+score);
        			 if(score<max_score)
        			 {
        				 max_score = score;
        				 //System.out.println(i+":score:"+score);
        				 id = i;
        			 }
        			 if(score == max_score) {
        				 if(bestscore > MyTools.getScore(MyTools.getBoard(tmp2)))
        				 {
        					 bestscore =  MyTools.getScore(MyTools.getBoard(tmp2));
        					 id = i;
        					// System.out.print("  test:"+bestscore);
        				 }
        			 }
        		 }
         }
        return id ;
    }
    public Move chooseMove(PentagoBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        //MyTools.getSomething();
        ArrayList<PentagoMove> moves = boardState.getAllLegalMoves();
        PentagoBoardState tmp =(PentagoBoardState) boardState.clone();
        long starttime = System.currentTimeMillis();
    	long endtime = starttime + 1900;
        int bestChoose;
        bestChoose = bestMove(tmp,starttime,endtime);
        
        Move myMove = moves.get(bestChoose);
        //tmp.processMove(moves.get(bestChoose));
        //tmp.printBoard();
        //System.out.println("player_student:"+bestChoose+"  "+myMove.getPlayerID());
        return myMove;
    }
}