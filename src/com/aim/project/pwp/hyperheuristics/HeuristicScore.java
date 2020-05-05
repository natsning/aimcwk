package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HeuristicScore {

    private int numHeuristic;
    private BigDecimal[] indvScore;
//    private double[][] pairScore;
    private BigDecimal[] timeScore;
    private BigDecimal indvWeight = BigDecimal.ONE;
//    private double pairWeight = 1.0;
    private BigDecimal timeWeight = BigDecimal.ONE;
    private double phi = 0.99;

    public HeuristicScore(int numHeuristic){
        this.numHeuristic = numHeuristic;
        indvScore = new BigDecimal[numHeuristic];
//        pairScore = new double[numHeuristic][numHeuristic];
        timeScore = new BigDecimal[numHeuristic];

        Utilities.initialiseBigDecimalArray(indvScore,BigDecimal.TEN);
        Utilities.initialiseBigDecimalArray(timeScore,BigDecimal.TEN);
    }

    public BigDecimal getIndvScore(int hIndex){
        return indvScore[hIndex].divide(timeScore[hIndex],10, RoundingMode.CEILING);
    }

//    public double getPairScore(int prevHIndex, int hIndex){
//        return pairScore[prevHIndex][hIndex];
//    }

    public BigDecimal getTimeScore(int hIndex){
        return timeScore[hIndex];
    }

    private void updateTimeScore(int chosenHIndex){
        for(int i = 0; i<numHeuristic; i++){
            if(i == chosenHIndex){
                timeScore[chosenHIndex] = BigDecimal.TEN;
            }else{
                timeScore[i] = timeScore[i].add( timeWeight);

            }
        }
    }

    public void increaseScore(int hIndex, BigDecimal change){

//        indvScore[hIndex] = indvScore[hIndex].add(indvWeight);
//        indvScore[hIndex] = indvScore[hIndex].add(change);
        indvScore[hIndex] = BigDecimal.ONE;
        phi = 0.99;
        updateTimeScore(hIndex);
    }

    public void decreaseScore(int hIndex, BigDecimal change){
//        indvScore[hIndex] = indvScore[hIndex].subtract(indvWeight);
//        indvScore[hIndex] = indvScore[hIndex].subtract(change);

        indvScore[hIndex] = BigDecimal.valueOf(-1);
        if(phi!=0.01){
            phi-=0.01;
        }
        updateTimeScore(hIndex);
    }

//    public void boostScore(int hIndex){
//        indvScore[hIndex]+= indvWeight;
//    }

    public void resetScore(){
        Utilities.initialiseBigDecimalArray(indvScore,BigDecimal.ZERO);
    }

    public BigDecimal getOverallHScore(int hIndex){
        return (getIndvScore(hIndex).multiply(BigDecimal.valueOf(phi)).add(BigDecimal.valueOf(1-phi).multiply(getTimeScore(hIndex))));
    }

}
