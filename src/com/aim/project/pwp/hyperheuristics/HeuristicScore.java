package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

public class HeuristicScore {

    private int[] heuristicIndvScore;
    private int[][] heuristicPairScore;

    public HeuristicScore(int numHeuristic){
        heuristicIndvScore = new int[numHeuristic];
        heuristicPairScore = new int[numHeuristic][numHeuristic];
        Utilities.initialiseIntArray(heuristicIndvScore,0);
        Utilities.initialiseIntArray(heuristicPairScore,0);
    }

    private int getIndvHScore(int hIndex){
        return heuristicIndvScore[hIndex];
    }

    private int getPairHScore(int prevHIndex, int hIndex){
        return heuristicPairScore[prevHIndex][hIndex];
    }

    public void increaseScore(int prevHIndex, int hIndex){
        heuristicIndvScore[hIndex]++;
        if(prevHIndex != -1){
            heuristicPairScore[prevHIndex][hIndex]++;
        }
    }

    public void decreaseScore(int prevHIndex, int hIndex){
        heuristicIndvScore[hIndex]--;
        if(prevHIndex != -1){
            heuristicPairScore[prevHIndex][hIndex]--;
        }
    }

    public double getOverallHScore(int prevHIndex, int hIndex){
        if(prevHIndex == -1){
            prevHIndex = hIndex;
        }
        return ((getIndvHScore(hIndex)+getPairHScore(prevHIndex,hIndex))/2.0);
    }

}
