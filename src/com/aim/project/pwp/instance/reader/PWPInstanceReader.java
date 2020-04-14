package com.aim.project.pwp.instance.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import com.aim.project.pwp.interfaces.PWPInstanceInterface;
import com.aim.project.pwp.interfaces.PWPInstanceReaderInterface;


public class PWPInstanceReader implements PWPInstanceReaderInterface {

	@Override
	public PWPInstanceInterface readPWPInstance(Path path, Random random) {

		BufferedReader bfr;
		try {
			bfr = Files.newBufferedReader(path);
			
			// TODO read in the PWP instance and create and return a new 'PWPInstance'
			
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
}
