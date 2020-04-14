package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

import com.aim.project.pwp.instance.Location;
import com.aim.project.pwp.instance.PWPInstance;
import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {

		BufferedReader bfr;
		PWPInstance instance = null;
        Location postalLoc = null, homeLoc = null;
        ArrayList<Location> aloLoc = new ArrayList<>();
        int numLoc = 0;
        String st,st2;
        String[] aoSt;

		try {

            bfr = Files.newBufferedReader(path);
            while ((st = bfr.readLine()) != null) {
                if (st.startsWith("POSTAL_OFFICE")) {
                    st2 = bfr.readLine();
                    aoSt = st2.split(" ");
                    numLoc++;
                    postalLoc = new Location(Double.parseDouble(aoSt[0]), Double.parseDouble(aoSt[1]));

                } else if (st.startsWith("WORKER_ADDRESS")) {
                    st2 = bfr.readLine();
                    aoSt = st2.split(" ");
                    homeLoc = new Location(Double.parseDouble(aoSt[0]), Double.parseDouble(aoSt[1]));
                    numLoc++;

                } else if (Character.isDigit(st.charAt(0))) {
                    aoSt = st.split(" ");
                    numLoc++;
                    aloLoc.add(new Location(Double.parseDouble(aoSt[0]), Double.parseDouble(aoSt[1]) ));

                }
            }//End while

			instance = new PWPInstance(numLoc,aloLoc.toArray(new Location[numLoc]),postalLoc,homeLoc,random);

		} catch (IOException e) {

			e.printStackTrace();

		}
		return instance;

	}
}
