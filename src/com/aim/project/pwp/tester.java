package com.aim.project.pwp;


import java.util.*;

public class tester {


    public static void main(String[] args) {
        int[] permutation = {70,80,95,115,140};
        int length = permutation.length;
        int lastIndex = length-1;
        Random r = new Random();
        int i,j;
        int oriCost = 150; //depot 20, home 170
        int newCost = 0;
        // test delta evaluation
        int[] prev = new int[length];
        System.arraycopy(permutation,0,prev,0,length);
        ArrayList<Integer> aloCity_ids = new ArrayList<>();
        for (int k : permutation)
            aloCity_ids.add(k);

        i = 3; j = 1;

        Integer removed = aloCity_ids.get(i);
        aloCity_ids.remove(i);
        aloCity_ids.add(j,removed);
        System.out.print(" 20");
        for(int k =0; k<length; k++ ) {
            permutation[k] = aloCity_ids.get(k);
            System.out.printf(" %d",permutation[k]);
        }
        System.out.print(" 170");


        newCost = oriCost
                - Math.abs(prev[i]-prev[i-1]) - Math.abs(prev[i]-prev[i+1])
                - Math.abs(prev[j]-prev[j-1])
                + Math.abs(permutation[j]-permutation[j-1]) + Math.abs(permutation[j]-permutation[j+1])
                + Math.abs(permutation[i+1]-permutation[i]);

        int check = 0;
        for(int k =0; k<length-1; k++){
            check += Math.abs(permutation[k]-permutation[k+1]);
        }
        check += fromDepot(permutation[0]) + toHome(permutation[lastIndex]);



//                toHome(permutation[i]);

        System.out.printf("\n%d   %d",newCost,check);
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
        private static int fromDepot(int x){
            return Math.abs(x-20);
        }
        private static int toHome(int x){
            return Math.abs(x-170);
        }
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java