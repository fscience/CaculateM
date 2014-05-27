package com.fscience.caculate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class MFiledSaver extends MPoint {
	String name = null;
	ArrayList<MPoint> points = null;
	public MFiledSaver(MPoint point) {
		super(point.x, point.y, point.z);
		
		points = new ArrayList<MPoint>(); 
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean add(MPoint point) {
		points.add(point);
		
		if (points.size() > 500) {
			this.writeToFile();
		}
		
		return true;
	}
	
	public void close() {
		if (points.size() > 0)
			this.writeToFile();
	}
	
	@Override
	public String toString() {
		if (name == null)
			return "Y: " + z;
		return name;
	}
	
	private void writeToFile() {
		try {
			File tmpFile = new File(".CalculateM" + File.separator + (int)z + ".mc");
//			File tmpFile = File.createTempFile("" + (int)z, "mc");
			if (!tmpFile.exists()) {
				tmpFile.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(tmpFile, true);
			OutputStreamWriter ow = new OutputStreamWriter(stream);
			BufferedWriter bw = new BufferedWriter(ow);
			
			for (MPoint point : points) {
				bw.write(point.z + "\t" + point.x + "\t" + point.y + "\n");
			}
			
			bw.close();
			ow.close();
			stream.close();
			
			points.clear();
		} catch (IOException e) {
		}
	}

	public MBounds loadFromFile() {
		points.clear();
		
		MBounds bounds = new MBounds();
		try {
			FileInputStream stream = new FileInputStream(".CalculateM" + File.separator + (int)z + ".mc");
			InputStreamReader read = new InputStreamReader(stream);
			BufferedReader bufRead = new BufferedReader(read);
			
			String tmpString = null;
			while ((tmpString = bufRead.readLine()) != null) {
				String coord[] = tmpString.split("\t");
				if (coord.length == 3) {
					// y, z, x
					MPoint point = new MPoint(coord[1], coord[2], "0");
					points.add(point);
					bounds.addPoint(point);
				}
			}
			
			bufRead.close();
			read.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bounds;
	}
}
