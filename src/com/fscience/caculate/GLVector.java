package com.fscience.caculate;

public class GLVector {
	
	double x, y, z, w;
	
	public GLVector(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public GLVector(MPoint point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
		this.w = 1.0;
	}
}
