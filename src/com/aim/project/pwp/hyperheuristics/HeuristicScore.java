package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

public class HeuristicScore {

    private int numHeuristic;
    private double[] indvScore;
    private double[][] pairScore;
    private double[] timeScore;
    private double indvWeight = 0.5;
    private double pairWeight = 1.0;
    private double timeWeight = 2;

    public HeuristicScore(int numHeuristic){
        this.numHeuristic = numHeuristic;
        indvScore = new double[numHeuristic];
        pairScore = new double[numHeuristic][numHeuristic];
        timeScore = new double[numHeuristic];
        Utilities.initialiseDoubleArray(indvScore,0);
        Utilities.initialiseDoubleArray(pairScore,0);
        Utilities.initialiseDoubleArray(timeScore,0);
    }

    public double getIndvScore(int hIndex){
        return indvScore[hIndex];
    }

    public double getPairScore(int prevHIndex, int hIndex){
        return pairScore[prevHIndex][hIndex];
    }

    public double getTimeScore(int hIndex){
        return timeScore[hIndex];
    }

    private void updateTimeScore(int chosenHIndex){
        for(int i = 0; i<numHeuristic; i++){
            if(i == chosenHIndex){
                timeScore[chosenHIndex] = 0;
            }else{
                timeScore[i] += timeWeight;
            }
        }
    }

    public void increaseScore(int prevHIndex, int hIndex){
        indvScore[hIndex]+= indvWeight;
        if(prevHIndex != -1){
            pairScore[prevHIndex][hIndex]+= pairWeight;
        }
        updateTimeScore(hIndex);
    }

    public void decreaseScore(int prevHIndex, int hIndex){
        indvScore[hIndex]-= indvWeight;
        if(prevHIndex != -1){
            pairScore[prevHIndex][hIndex]-= pairWeight;
        }
        updateTimeScore(hIndex);
    }

    public void boostScore(int hIndex){
        indvScore[hIndex]+= indvWeight;
    }

    public void resetScore(){
        Utilities.initialiseDoubleArray(indvScore,0);
        Utilities.initialiseDoubleArray(pairScore,0);
    }

    public double getOverallHScore(int prevHIndex, int hIndex){
        if(prevHIndex == -1){
            prevHIndex = hIndex;
        }
        return (getIndvScore(hIndex)+getPairScore(prevHIndex,hIndex)+getTimeScore(hIndex));
    }

}
