package com.aim.project.pwp;


import java.util.*;

public class tester {


    public static void main(String[] args) {
        int[] permA = {0,1,2,3,4,5,6,7,8,9};
        int[] oriA = {0,1,2,3,4,5,6,7,8,9};
        int[] permB = {3,7,5,9,0,1,4,2,6,8};
        int[] oriB = {3,7,5,9,0,1,4,2,6,8};
        int length = permA.length;
        Random r = new Random();
        int times =2;
        int i,j,temp,indexA,indexB;
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();
//        int start = r.nextInt(permutation.length);
//        System.out.println(start);
        // test delta evaluation
//        int[] prev = new int[permutation.length];
//        System.arraycopy(permutation,0,prev,0,permutation.length);
//
//        int lastIndex = prev.length-1;
//        int indexToSwap,adjacentIndex,temp;
//
//        for (int q = 0 ; q < 4 ; q++){
//            indexToSwap = r.nextInt(permutation.length);
//            adjacentIndex = (indexToSwap + 1) % permutation.length;
//            temp = permutation[indexToSwap];
//            permutation[indexToSwap] = permutation[adjacentIndex];
//            permutation[adjacentIndex] = temp;
//
//            System.out.println(" old");
//            for(int i : prev) {
//                System.out.printf("%d ",i);
//            }
//            System.arraycopy(permutation,0,prev,0,permutation.length);
//        }
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
        // invertion mutation & reinsertion
//        for (int k = 0; k < 2; k++) {
//
            //pick two indexes
//        for(int bigloop = 0; bigloop < times; bigloop++){
//            setA.clear();
//            setB.clear();
//            System.arraycopy(permA,0,oriA,0,length);
//            System.arraycopy(permB,0,oriB,0,length);
//            i = r.nextInt(permA.length);
//            j = i;
//            while (j == i) { //ensures i and j are two different indexes
//                j = r.nextInt(permA.length);
//            }
//            if (i > j) { //i will always be smaller than j
//                temp = i;
//                i = j;
//                j = temp;
//            }
//            System.out.printf("\n%d, %d\n",i,j);
//
//            for(int k=i+1; k<=j; k++ ){
//                setA.add(permA[k]);
//                setB.add(permB[k]);
//            }
//
//            indexA = j+1; indexB = j+1;
//            for(int index = j+1; index <= permA.length+j; index++) {
//                if (!setB.contains(oriA[index % permA.length])) {
//                    permB[indexB % permB.length] = oriA[index % permA.length];
//                    indexB++;
//                }
//                if (!setA.contains(oriB[index % permA.length])) {
//                    permA[indexA % permB.length] = oriB[index % permA.length];
//                    indexA++;
//                }
//            }//end for
//
//            for(int k: permA){
//                System.out.printf(" %d",k);
//            }
//            System.out.println(' ');
//            for(int k: permB){
//                System.out.printf(" %d",k);
//            }
        }

//            for (int h = i; h <= j; h++) {
//                aloPermutation.add(permutation[h]);
//            }
//            Collections.reverse(aloPermutation);
//            for (int h = 0; h < aloPermutation.size(); h++) {
//                permutation[i] = aloPermutation.get(h);
//                i++;
//            }
//            aloPermutation.clear();
//
//            for(int h=0 ; h<aloPermutation.size(); h++) {
//                permutation[h] = aloPermutation.get(h);
//                System.out.printf("%d ", permutation[h]);
//            }
//
//        }//end for
//            else {
//                System.out.printf("permB[%d]: %d is in setB thus not added to permS[%d]\n",
//                        index % 10, permA[index % 10], indexA % 10);
//            }
//        System.out.println("0,1,2,3,4,5,6,7,8,9");
//        System.out.println("3,7,5,9,0,1,4,2,6,8");

//        System.out.println(' ');
//        for(int k: setA){
//            System.out.printf(" %d",k);
//        }
//        System.out.println(' ');
//        for(int k: setB){
//            System.out.printf(" %d",k);
//        }
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java