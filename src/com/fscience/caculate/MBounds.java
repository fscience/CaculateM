package com.fscience.caculate;

public class MBounds {
	public double minX;
	public double minY;
	public double minZ;
	public double maxX;
	public double maxY;
	public double maxZ;
	
	public MBounds() {
		minX = minY = Double.MAX_VALUE;
		maxX = maxY = Double.MIN_VALUE;
	}
	public MBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public MPoint center() {
		MPoint point = new MPoint((maxX + minX) / 2.0,  (maxY + minY) / 2.0, (maxZ + minZ) / 2.0);
		return point;
	}
	
	public void addPoint(MPoint point) {
		if (minX > point.x) {
			minX = point.x;
		}
		if (maxX < point.x) {
			maxX = point.x;
		}
		
		if (minY > point.y) {
			minY = point.y;
		}
		if (maxY < point.y) {
			maxY = point.y;
		}
		
		if (minZ > point.z) {
			minZ = point.z;
		}
		if (maxZ < point.z) {
			maxZ = point.z;
		}
	}
}
