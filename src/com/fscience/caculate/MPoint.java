package com.fscience.caculate;

public class MPoint {
	private double scale = 1;
	
	public double x;
	public double y;
	public double z;
	
	
	public MPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public MPoint(String x, String y, String z) {
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
//		this.z = 0;
		this.z = Double.parseDouble(z);
	}
	public double distanceForXY (MPoint point) {
		return Math.sqrt((point.x - x) * (point.x - x) + (point.y - y) * (point.y - y));
	}
	public void fixPosition(MBounds bounds) {
//		this.x -= (bounds.minX - 100);
//		this.y -= (bounds.minY - 100);
		this.x -= bounds.minX;
		this.y -= bounds.minY;
		
		if (scale == 1) {
			double w = bounds.maxX - bounds.minX;
			double h = bounds.maxY - bounds.minY;
			
			double maxL = (w > h) ? w : h;
			
			scale = 500.0 / maxL;
		}
		this.x *= scale;
		this.y *= scale;
		
//		this.x += 100;
//		this.y += 100;
	}
}
