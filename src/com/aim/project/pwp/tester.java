package com.aim.project.pwp;


import com.aim.project.pwp.hyperheuristics.LateAcceptance;

import java.math.BigDecimal;
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
//        String s = "-1.171894715 52.964538";
//        String[] aoSt;
//        aoSt = s.split("\\t");
//        if(aoSt.length!=2){
//            aoSt = s.split(" ");
//        }
//        Double d = Double.parseDouble(aoSt[2]);
//        System.out.print(aoSt[0]);
//        int[] i = {1,2,3};
//        int[] j = i.clone();
//        j[0] = 2;
//        System.out.print(i[0]);

//        double delta = 0.1;
//        double temp = 5000*Math.pow(0.98,5000);
//        double temp = 0;
//        System.out.printf("temp=%f, %f",temp,Math.exp( -1* delta / temp));
//
//        Random r = new Random();
//        LateAcceptance la = new LateAcceptance(10,10,r);
//        for(int i=0; i<10; i++){
//            la.updateLateAcceptance(r.nextDouble()*10.0,i);
//            la.printArray();
//            System.out.println(la.getDelayedObjVal());
//        }
//        BigDecimal a = BigDecimal.ONE;
//        BigDecimal b = BigDecimal.TEN;
//        if(b.compareTo(b) == 0)
//            System.out.println('s');
//        roulleteWheel();
            long start = System.nanoTime();
            for(int i = 1; i%6 != 0; i++ );
            System.out.println(System.nanoTime()-start);

            long start2 = System.nanoTime();
            for(int i=1; i<10; i++){
                if(i == 6) break;
            }
            System.out.print(System.nanoTime()-start2);


    }
    private static int roulleteWheel(){
        Double[] aohScores = {0.1,0.2};
        Double sum = 0.3;
        Double r = new Random().nextDouble() * sum;
        Double cumulative = 0.0;
        for(int i =0; i<2; i++){
            cumulative+= aohScores[i];
            System.out.printf("%f , %f\n",r,cumulative);
            if(r<cumulative) {
                System.out.print(i);
                return i;
            }
        }
        return -1;
    }
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp
//  javac tester.java