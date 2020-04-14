package com.aim.project.pwp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class tester {
    private static String[] instanceFiles = {
            "square", "libraries-15", "carparks-40", "tramstops-85", "trafficsignals-446", "streetlights-35714"
    };

    public static void main(String[] args){

        String SEP = FileSystems.getDefault().getSeparator();
        String instanceName = "D:"+SEP+"Y2S2-AIM"+SEP+"cw"+SEP+"cwk"+SEP+"instances" + SEP + "pwp" + SEP + instanceFiles[0] + ".pwp";
        Path path = Paths.get(instanceName);
        BufferedReader bfr;
        String st="st",st2="sr2";
        String[] aoSt;
        ArrayList<Double> aloDb = new ArrayList<>();
        int numberOfLocations=0;


        try {
//            bfr = Files.newBufferedReader(path);
//            while ((st = bfr.readLine()) != null) {
//                if (st.startsWith("POSTAL_OFFICE")){
//                    st2 = bfr.readLine();
//                    aoSt = st2.split(" ");
//                    numberOfLocations++;
//                    System.out.printf("POSTAL OFFICE: %f %f\n",Double.parseDouble(aoSt[0]),Double.parseDouble(aoSt[1]));
//                }
//                else  if(st.startsWith("WORKER_ADDRESS")){
//                    st2 = bfr.readLine();
//                    aoSt = st2.split(" ");
//                    numberOfLocations++;
//                    System.out.printf("WORKER: %f %f\n",Double.parseDouble(aoSt[0]),Double.parseDouble(aoSt[1]));
//                }
//                else if (Character.isDigit(st.charAt(0))) {
//                    aoSt = st.split(" ");
//                    numberOfLocations++;
////                    System.out.printf("Normal: %f %f\n",Double.parseDouble(aoSt[0]),Double.parseDouble(aoSt[1]));
//                    aloDb.add(Double.parseDouble(aoSt[0]));
//
//                }
//            }
            aloDb.add(1.0);
            aloDb.add(2.0);
            aloDb.add(3.0);

            Double[] aod = aloDb.toArray(new Double[3]);
            for (Object i:aod) {
                System.out.print(i);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

//        System.out.printf("Total:%d\n",numberOfLocations);
//        for (Object d:aloDb.toArray()) {
//            System.out.print(aloDb.toArray());
//        }

    }
}
    //cd Y2S2-AIM/cw/cwk/src/com/aim/project/pwp