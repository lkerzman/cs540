
/* Don't forget to change this line to cs540.checkers.<username> */
package cs540.checkers.lkerzman;

import cs540.checkers.*;
import static cs540.checkers.CheckersConsts.*;

import java.util.*;

/*
 * This is a skeleton for an alpha beta checkers player. Please copy this file
 * into your own directory, i.e. src/<username>/, and change the package 
 * declaration at the top to read
 *     package cs540.checkers.<username>;
 * , where <username> is your cs department login.
 */
/** This is a skeleton for an alpha beta checkers player. */
public class AlphaBetaPlayer extends CheckersPlayer implements GradedCheckersPlayer
{
	/** The number of pruned subtrees for the most recent deepening iteration. */
	protected int pruneCount;
	protected Evaluator sbe;
	int curr=0;
	int bestScore = Integer.MIN_VALUE;
	Move bestMove = null;
	int lastScore=Integer.MIN_VALUE;
	int numChildren=0;



	public AlphaBetaPlayer(String name, int side)
	{ 
		super(name, side);
		sbe = new SimpleEvaluator();
	}

	public void calculateMove(int[] bs)
	{
		bestScore = Integer.MIN_VALUE;
		bestMove = null;
		numChildren=0;
		pruneCount=0;

		for (int d=2;d< depthLimit; d++){
			curr=d;
			pruneCount=0;
			int score=Integer.MIN_VALUE;
			score=maxValue(bs, Integer.MIN_VALUE, Integer.MAX_VALUE, d);



			setMove(bestMove);
			if (score > bestScore){
				bestScore=score;
			}
			if(Utils.verbose == true){
				System.out.println("Best Move: " + bestMove + "final minimax value: " + bestScore);
				System.out.println("PruneCount: " + this.pruneCount + "minimax value of last pruned node: " + lastScore);
			}
		}


	}
	private int maxValue (int []s, int a, int B, int d){
		List<Move> possibleMoves = Utils.getAllPossibleMoves(s, side);
		numChildren+=possibleMoves.size();
		if(possibleMoves.size()==0 || d==0){
			int score = sbe.evaluate(s);
			if(side==BLK)
				score= -score;
			return score;
		}
		Move best =null;
		int old=Integer.MIN_VALUE;
		int count=0;
		for (Move move : possibleMoves){
			count++;
			Stack<Integer> rv = Utils.execute(s, move);
			old=a;
			a = Math.max(a, (minValue(s,a,B,(d-1))) );
			Utils.revert(s, rv);
			if (d==curr&&a>old){
				bestMove=move;
			}
			if (a>=B){
				pruneCount+= (possibleMoves.size()-count);
				lastScore=sbe.evaluate(s);
				if(side==BLK)
					lastScore= -lastScore;
				return a; //prune remaining children of Max
			}
		}
		return a;
	}

	private int minValue(int [] s, int a, int B, int d){
		List<Move> possibleMoves = Utils.getAllPossibleMoves(s, side);
		numChildren+=possibleMoves.size();
		if (possibleMoves.size()==0||d==0){
			int score = sbe.evaluate(s);
			if(side==BLK)
				score= -score;
			return score;
		}
		int count=0;
		for (Move move : possibleMoves){
			count++;
			Stack<Integer> rv = Utils.execute(s, move);
			B = Math.min(B, (maxValue(s, a, B,(d-1))) );
			Utils.revert(s, rv);
			if (a>=B){
				pruneCount+= (possibleMoves.size()-count);
				lastScore=sbe.evaluate(s);
				if(side==BLK)
					lastScore= -lastScore;
				return B; //prune remaining children of Min
			}

		}
		return B;
	}

	public int getPruneCount()
	{
		return pruneCount;
	}


	public int getLastPrunedNodeScore() {
		return lastScore;
	}


}
