package com.fscience.caculate;

import java.util.ArrayList;

public class MSortedArray extends ArrayList<MPoint> {
	private static final long serialVersionUID = 1L;

	@Override
	public void add(int index, MPoint element) {
		throw new NullPointerException("xxx");
	}

	@Override
	public boolean add(MPoint e) {
		int sortIndex = 0;
		if (this.size() > 0) {
			sortIndex = this.getSuiteIndex(e);
		}
		
		super.add(sortIndex, e);
		
		return true;
	}
	
	private int getSuiteIndex(MPoint e) {
		int center = this.size() / 2;
		
//		while (true) {
//			MPoint point = get(center);
//		}
		return center;
	}
}
