package com.aim.project.pwp;


import java.util.*;

public class tester {


    public static void main(String[] args) {
//        int[] p1city_id = {0, 1, 2, 3, 4, 5};
//        int[] p2city_id = {5, 3, 2, 1, 0, 4};
//        int length = p1city_id.length;
//        int[] backup_cityID_p1 = new int[length], backup_cityID_p2 = new int[length];
//        Random r = new Random();
//        int[] p2pair;
//        int valueIndex = 0, flagIndex = 1;
//        int startID, nextID, cycleIndex;
        String s = "-1.171894715 52.964538";
        String[] aoSt;
        aoSt = s.split("\\t");
        if(aoSt.length!=2){
            aoSt = s.split(" ");
        }
//        Double d = Double.parseDouble(aoSt[2]);
        System.out.print(aoSt[0]);




    }
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java