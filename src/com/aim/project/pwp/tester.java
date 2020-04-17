package com.aim.project.pwp;


import java.util.Random;

public class tester {


    public static void main(String[] args){
//        List<Integer> aloPermutation = new ArrayList<>();
//        for (int i : permutation){
//            aloPermutation.add(i);
     // test delta evaluation
//        int[] permutation = {70,80,94,95};
//        int[] prev = {70,80,94,95};
//        int lastIndex = prev.length-1;
//        int indexToSwap = new Random().nextInt(permutation.length);
//        int adjacentIndex = (indexToSwap + 1) % permutation.length;
//        int temp = permutation[indexToSwap];
//        permutation[indexToSwap] = permutation[adjacentIndex];
//        permutation[adjacentIndex] = temp;
//        int oriCost = (70-20) + (80-70) + (94-80) + (95-94) + (120-95); //depot 20, home 120
//        int newCost = 0;
//        for(int i : permutation) {
//            System.out.printf("%d ",i);
//        }
//        if(indexToSwap == lastIndex){
//            newCost = oriCost
//                    - Math.abs(120-prev[lastIndex]) - Math.abs(prev[0]-20)
//                    - Math.abs(prev[1]-prev[0]) -Math.abs(prev[lastIndex]-prev[lastIndex-1])
//                    + Math.abs(120-permutation[lastIndex]) + Math.abs(permutation[0]-20)
//                    + Math.abs(permutation[1]-permutation[0]) + Math.abs(permutation[lastIndex]-permutation[lastIndex-1]);
//        }
//        else if(indexToSwap == 0){
//            newCost = oriCost - Math.abs(prev[0]-20) - Math.abs(prev[1]-prev[2])
//                    + Math.abs(permutation[0]-20) + Math.abs(permutation[1]-permutation[2]);
//        }
//        else if(indexToSwap == lastIndex-1){
//            newCost = oriCost - Math.abs(120-prev[lastIndex]) - Math.abs(prev[lastIndex-1]-prev[lastIndex-2])
//                    + (120-permutation[lastIndex]) + Math.abs(permutation[lastIndex-1]-permutation[lastIndex-2]);
//        }
//        else{
//            newCost = oriCost - Math.abs(prev[indexToSwap]-prev[indexToSwap-1]) - Math.abs(prev[adjacentIndex]-prev[adjacentIndex+1])
//                    + Math.abs(permutation[indexToSwap]-permutation[indexToSwap-1]) + Math.abs(permutation[adjacentIndex]-permutation[adjacentIndex+1]);
//        }
//        System.out.printf(": %d",newCost);
    }

//
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java