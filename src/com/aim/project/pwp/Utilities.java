package com.aim.project.pwp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

public class Utilities {

    public static void shuffle(int[] arr, Random r){
        for (int i = 0; i < arr.length; i++) {
            int randomIndexToSwap = r.nextInt(arr.length);
            int temp = arr[randomIndexToSwap];
            arr[randomIndexToSwap] = arr[i];
            arr[i] = temp;
        }
    }

    public static int[] convertArrayListToArray(ArrayList<Integer> al){
        int[] array = new int[al.size()];
        for(int i=0;i<al.size();i++){
            array[i] = al.get(i);
        }
        return array;
    }

    public static void initialiseBigDecimalArray(BigDecimal[] arr, BigDecimal element){
        for (int i = 0; i< arr.length; i++){
            arr[i] = element;
        }
    }

//    public static void initialiseDoubleArray(double[][] arr, int element){
//        for( int i=0; i< arr.length; i++){
//            initialiseDoubleArray(arr[i],element);
//        }
//    }
}
