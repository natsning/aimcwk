package com.aim.project.pwp.hyperheuristics;

import com.aim.project.pwp.Utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HeuristicScore {

    private int numHeuristic;
    private BigDecimal[] indvScore;
    private BigDecimal[] timeScore;
    private BigDecimal indvWeight = BigDecimal.ONE;
    private BigDecimal timeWeight = BigDecimal.ONE;
    private BigDecimal phi = BigDecimal.valueOf(0.99);
    private BigDecimal upperBound;
    private BigDecimal lowerBound;

    public HeuristicScore(int numHeuristic,double bounds){
        this.numHeuristic = numHeuristic;
        indvScore = new BigDecimal[numHeuristic];
        timeScore = new BigDecimal[numHeuristic];

        Utilities.initialiseBigDecimalArray(indvScore,BigDecimal.ZERO);
        Utilities.initialiseBigDecimalArray(timeScore,BigDecimal.valueOf(5));
        upperBound = BigDecimal.valueOf(bounds*1.2);
        lowerBound = BigDecimal.valueOf(bounds*-1.2);

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

    public void updateTimeScore(int chosenHIndex){
        for(int i = 0; i<numHeuristic; i++){
            if(i == chosenHIndex){
                timeScore[chosenHIndex] = BigDecimal.ONE;
            }else{
                timeScore[i] = timeScore[i].add( timeWeight);

            }
        }

    }

    public void increaseScore(int hIndex, BigDecimal change){

//        indvScore[hIndex] = indvScore[hIndex].add(indvWeight);
        indvScore[hIndex] = indvScore[hIndex].add(change);
//        indvScore[hIndex] = indvScore[hIndex].multiply(BigDecimal.valueOf(1.2));
        if(indvScore[hIndex].compareTo(upperBound) == 1){
            indvScore[hIndex] = upperBound;
//        } else if(indvScore[hIndex].compareTo(BigDecimal.ZERO) == -1){
//            indvScore[hIndex] = BigDecimal.ZERO;
        }
        phi = BigDecimal.valueOf(0.99);
        updateTimeScore(hIndex);
    }

    public void decreaseScore(int hIndex, BigDecimal change){
//        indvScore[hIndex] = indvScore[hIndex].subtract(indvWeight);
        indvScore[hIndex] = indvScore[hIndex].subtract(change);
//        indvScore[hIndex] = indvScore[hIndex].multiply(BigDecimal.valueOf(0.8));
        if(indvScore[hIndex].compareTo(lowerBound) == -1){
            indvScore[hIndex] = lowerBound;
        }
        if(phi.compareTo(BigDecimal.valueOf(0.01)) == 1){
            phi = phi.subtract(BigDecimal.valueOf(0.01));
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
        return (getIndvScore(hIndex).multiply(phi).add((BigDecimal.valueOf(1).subtract(phi)).multiply(getTimeScore(hIndex))));
//        return (getIndvScore(hIndex).add(getTimeScore(hIndex)));
    }

    public void printHScore(){
        for(int i=0; i<numHeuristic; i++){
            System.out.printf("%d: %.5f %.5f %.5f\n",i,indvScore[i],timeScore[i],getOverallHScore(i));
        }
    }

}
