package com.aim.project.pwp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class tester {


    public static void main(String[] args) {
        int[] permutation = {0,1,2,3,4,5,6,7,8,9,10};
//        String[] strings = new String[permutation.length+2];
//        strings[0] = "DEPOT";
//        strings[strings.length-1]= "HOME";
//        for(int i=1;i<=permutation.length;i++){
//            strings[i] = Integer.toString(permutation[i-1]);
//        }
//        System.out.println(String.join(" -> ",strings));
//        List<Integer> aloPermutation = new ArrayList<>();
//        for (int i : permutation) {
//            aloPermutation.add(i);
//        }
//        Random r = new Random();
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
//            int i = r.nextInt(permutation.length);
//            int j = i;
//            while (j == i) { //ensures i and j are two different indexes
//                j = r.nextInt(permutation.length);
//            }
//            System.out.printf("\n%d, %d\n",i,j);
//
//            Integer removed = aloPermutation.get(i);
//            aloPermutation.remove(i);
//            aloPermutation.add(j,removed);

//            if (i > j) { //i will always be smaller than j
//                int temp = i;
//                i = j;
//                j = temp;
//            }
//
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
//        for(int i=start; i<permutation.length+start ; i++){
//            System.out.println(permutation[i%permutation.length]);
//        }


    }
//
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java