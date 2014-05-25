package com.fscience.caculate;

import java.util.Vector;

public class MParteSortedArray extends Vector<MFiledSaver> {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void add(int index, MFiledSaver element) {
		throw new NullPointerException("xxx");
	}

	@Override
	public boolean add(MFiledSaver e) {
		throw new NullPointerException("xxx");
	}
	
	public void add(MPoint point) {
		int sortIndex = 0;
		if (this.size() > 0) {
			sortIndex = this.getSuiteIndex(point);
		} else {
			super.add(new MFiledSaver(point));
		}
		
		get(sortIndex).add(point);
	}
	
	public void addForInit(MPoint point, String name) {
		int sortIndex = 0;
		if (this.size() > 0) {
			sortIndex = this.getSuiteIndex(point);
		} else {
			super.add(new MFiledSaver(point));
		}
		get(sortIndex).setName(name);
	}
	
	int getSuiteIndex(MPoint point) {
		int min = 0, max = size() - 1;
		int suiteIndex = -1;
		
		while ((min <= max) && (max <= size() - 1)) {
			int center = (max + min) >> 1;
			MPoint tmpPoint = get(center);
			double offset = tmpPoint.z - point.z;
			if (offset < -1) { // 右
				min = center + 1;
				if (min > max) {
					super.add(min, new MFiledSaver(point));
					suiteIndex = min;
					break;
				}
			} else if (offset > 1) { // 左
				max = center - 1;
				if (min > max) {
					super.add(min, new MFiledSaver(point));
					suiteIndex = min;
					break;
				}
			} else {
				suiteIndex = center;
				break;
			}
		}
		
//		if (min == max) {
//			MPoint tmpPoint = get(min);
//			
//			double offset = tmpPoint.z - point.z;
//			if (offset > 1) { // 右
//				super.add(min + 1, new MFiledSaver(point));
//				suiteIndex = min + 1;
//			} else if (offset < -1) { // 左
//				super.add(min, new MFiledSaver(point));
//				suiteIndex = min;
//			} else {
//				suiteIndex = min;
//			}
//		}
		
		return suiteIndex;
	}
}
