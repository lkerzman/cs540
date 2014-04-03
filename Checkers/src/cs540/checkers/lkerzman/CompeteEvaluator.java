package cs540.checkers.lkerzman;
import static cs540.checkers.CheckersConsts.*;

import java.util.*;

import cs540.checkers.Evaluator;

/**
 * This simplistic static board evaluator assigns points for material.  Each 
 * pawn remaining on the board contributes one point, and each remaining king 
 * remaining on the board contributes two points. 
 */
public class CompeteEvaluator implements Evaluator{
	public int evaluate(int[] bs)

	{	int score = Integer.MIN_VALUE; 

	int totalPieces = 0;//counting
	int redPawns = 0; 
	int blackPawns = 0;
	int redKings = 0;
	int blackKings = 0;

	int pawnPlace = 0;//location on board
	int kingPlace = 0;
	
	int redEdge = 0 ;
	int blackEdge = 0;





	for (int i = 0; i < H * W; i++)
	{
		int v = bs[i];
		switch(v)
		{
		case RED_PAWN:
			redPawns++;
			totalPieces++;
			if(isEdge(i))
				redEdge++;
			pawnPlace += location(RED_PAWN,i);
			break;
		case BLK_PAWN:
			blackPawns++;
			totalPieces++;
			if(isEdge(i))
				blackEdge++;
			pawnPlace -= location(BLK_PAWN,i);
			break;
		case RED_KING:
			redKings++;
			totalPieces++;
			if(isEdge(i))
				redEdge++;
			kingPlace += location(RED_KING,i);
			break;
		case BLK_KING:
			blackKings++;
			totalPieces++;
			if(isEdge(i))
				blackEdge++;
			kingPlace -= location(BLK_KING,i);
			break;
		default:
			break;
		}

	}
	//halfway through pieces more valuable 

	if(totalPieces <= 4)
		score = ((redPawns + 2*redKings) - (blackPawns + 2*redKings));

	if(totalPieces > 12)
		score = ((redPawns + redKings) - (blackPawns + blackKings)) * 100;
	else
		score = ((redPawns + redKings) - (blackPawns + blackKings))
		* (((redPawns + redKings) - (blackPawns + blackKings)) * 50);


	
	score += kingPlace;
	score += pawnPlace;
	score -= 2 * (redEdge - blackEdge);	
	return score;
	}




	public boolean isEdge(int i){

		if(i<8||i>55||i%8==0||i%8==7)
			return true;
		return false;
	}
	//locations I liked/didn't like
	public int location(int type, int pos ){
		int locVal=0;
		if (type==BLK_KING||type==RED_KING){
			if ((pos>=17&&pos<=23)||pos>=40&&pos<=46){
				locVal=2;
			}
			if (pos>=24&&pos<=39){
				locVal=3;
			}
			switch(pos){
			case 1:
			case 8:
			case 62:
			case 55:
				locVal=-1;
				break;
			case 7:
			case 56:
				locVal=-2;
				break;
			}
		}

		if (type==RED_PAWN){
			if (pos<=7){
				locVal=5;
			}
			if (pos>=10&&pos<=14){
				locVal=3;
			}
			if (pos>=24&&pos<=39){
				locVal=3;
			}
			if (pos>39){
				locVal=4;
			}

		}
		if (type==BLK_PAWN){ 
			if (pos<=7){
				locVal=4;
			}
			if (pos>=10&&pos<=14){
				locVal=3;
			}
			if (pos>=24&&pos<=39){
				locVal=3;
			}
			if (pos>39){
				locVal=5;
			}
		}
		return locVal;
	}

}



