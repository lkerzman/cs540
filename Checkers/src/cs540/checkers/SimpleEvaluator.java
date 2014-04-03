package cs540.checkers;
import static cs540.checkers.CheckersConsts.*;

import java.util.*;

/**
 * This simplistic static board evaluator assigns points for material.  Each 
 * pawn remaining on the board contributes one point, and each remaining king 
 * remaining on the board contributes two points. 
 */
public class SimpleEvaluator implements Evaluator
{
    public int evaluate(int[] bs)
    {
        int[] pawns = new int[2],
            kings = new int[2] ;

        for (int i = 0; i < H * W; i++)
        {
            int v = bs[i];
            //System.out.println("bs " + i);
            switch(v)
            {
                case RED_PAWN:
                case BLK_PAWN:
                    pawns[v % 4] += 1;
                   // System.out.println("pawns [" + v%4 + "] = " + pawns[v%4]);
                    //System.out.println("pawns [0]: " + pawns[0]+ " pawns[1]: " + pawns[1]);
                    break;
                case RED_KING:
                case BLK_KING:
                    kings[v % 4] += 1;
                    //System.out.println("kings [" + v%4 + "] = " + kings[v%4]);
                    //System.out.println("kings [0]: " + kings[0]+ " kings[1]: " + kings[1]);
                    break;
            }
        }

        return 1 * (pawns[RED] - pawns[BLK]) + 
               3 * (kings[RED] - kings[BLK]);
    }
}
