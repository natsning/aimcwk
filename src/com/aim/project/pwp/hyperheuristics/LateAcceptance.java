package com.aim.project.pwp.hyperheuristics;

import java.util.Arrays;
import java.util.Random;

public class LateAcceptance {
    private double[] aoObjectiveValues;
    private int num;
    private int pointIndex;
    private int updateIndex = 0;

    public LateAcceptance(int degreeOfLateness,double defaultValue, Random rng){
        num = degreeOfLateness;
        pointIndex = rng.nextInt(num-1)+1; //does not start from 0

        aoObjectiveValues = new double[degreeOfLateness];
        for(int i=0; i<num; i++){
            aoObjectiveValues[i] = defaultValue;
        }

    }

    public void updateLateAcceptance(double objVal ){
        aoObjectiveValues[updateIndex++%num] = objVal;
    }

    public double getAverage( ){
        return Arrays.stream(aoObjectiveValues).sum()/(double)num;
    }

    public double getDelayedObjVal(){
        return aoObjectiveValues[pointIndex++%num];
    }

    public void printArray(){
        for(int i =0; i<num; i++){
            System.out.printf("%.3f ",aoObjectiveValues[i]);
        }
    }
}
