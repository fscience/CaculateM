package com.fscience.caculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MCalculate {
	double topLeft, topRight, hTopLeft, hTopRight;
	double centerLeft, centerRight, hCenterLeft, hCenterRight;
	double bottomLeft, bottomRight, hBottomLeft, hBottomRight;
	
	ArrayList<MPoint> sortedPoints = null;
	ArrayList<MPoint> filtedPoints = null;
	
	private MBounds orgBounds = null;
	private MPoint calCenter = null;
	
	public void calculate(ArrayList<MPoint> points, MBounds bounds, double trackHeight, double trackCenterX) {
		orgBounds = bounds;
		
		calCenter = new MPoint(trackCenterX, (bounds.maxY + bounds.minY) / 2.0, 1);
		
		sortedPoints = sortPoints(points);
		filtedPoints = filterPoints(sortedPoints);
		
		calculateDis(trackHeight, trackCenterX);
	}
	
	private void calculateDis(double trackHeight, double trackCenterX) {
		double distance[] = new double[4];
//		System.out.println("最低线");
		getHorizontalDistance(trackHeight + 0.2, trackCenterX, filtedPoints, sortedPoints, distance);
		bottomLeft = distance[0]; bottomRight = distance[1]; hBottomLeft = distance[2]; hBottomRight = distance[3];
//		System.out.println("中心线");
		getHorizontalDistance(orgBounds.maxY - 2.6, trackCenterX, filtedPoints, sortedPoints, distance);
		centerLeft = distance[0]; centerRight = distance[1]; hCenterLeft = distance[2]; hCenterRight = distance[3];
//		System.out.println("最高线");
		getHorizontalDistance(trackHeight + 3.1, trackCenterX, filtedPoints, sortedPoints, distance);
		topLeft = distance[0]; topRight = distance[1]; hTopLeft = distance[2]; hTopRight = distance[3];
	}
	
	private ArrayList<MPoint> sortPoints(ArrayList<MPoint> array) {
		Collections.sort(array, new SortCircle(calCenter));
		return array;
	}
	
	private ArrayList<MPoint> filterPoints(ArrayList<MPoint> points) {
		ArrayList<MPoint> filtedPoints = new ArrayList<MPoint>();
		
		double radius = (orgBounds.maxX - orgBounds.minX) / 2.0;
		
		double angleStemp = Math.PI / 90;
		
		int startIndex = 0;
		double startAngle = Math.asin((points.get(0).y - calCenter.y) / points.get(0).distanceForXY(calCenter));
		for (int idx = 1; idx < points.size(); idx ++) {
			MPoint point = points.get(idx);
			
			if (Math.abs((radius - calCenter.distanceForXY(point))) > 0.5) {
				continue;
			}
			
			double angle = Math.asin((points.get(idx).y - calCenter.y) / points.get(idx).distanceForXY(calCenter));
			if ((Math.abs(angle - startAngle) > angleStemp) || ((idx + 1) == points.size())) {
				double x = 0, y = 0;
				for (int i = startIndex; i < idx; i ++) {
					x += points.get(i).x;
					y += points.get(i).y;
				}
				MPoint calPoint = new MPoint(x / (idx - startIndex), y / (idx - startIndex), point.z);
				filtedPoints.add(calPoint);
				
				startIndex = idx;
				startAngle = angle;
			}
			
//			System.out.println(Math.toDegrees(angle));
		}
		
		return filtedPoints;
	}
	
	class SortCircle implements Comparator<MPoint> {
		MPoint center = null;
		
		public SortCircle(MPoint center) {
			this.center = center;
		}
		
		@Override
		public int compare(MPoint o1, MPoint o2) {
			double distance1 = o1.distanceForXY(center);
			double distance2 = o2.distanceForXY(center);
			
			if ((o1.y >= center.y) && (o2.y >= center.y)) {
//				double val = ((o1.x - center.x) / distance1) - ((o2.x - center.x) / distance2);
				double val = ((o1.x - center.x) * distance2) - ((o2.x - center.x) * distance1);
				
				if (val > 0) return 1;
				else if (val < 0) return -1;
				return 0;
//				return (int)((((o1.x - center.x) / distance1) - ((o2.x - center.x) / distance2)) * 1000);
//				return (int)((o1.x - o2.x) * 100);
			} else if ((o1.y < center.y) && (o2.y < center.y)) {
//				double val = ((o2.x - center.x) / distance2) - ((o1.x - center.x) / distance1);
				double val = ((o2.x - center.x) * distance1) - ((o1.x - center.x) * distance2);
				
				if (val > 0) return 1;
				else if (val < 0) return -1;
				return 0;
//				return (int)((((o2.x - center.x) / distance2) - ((o1.x - center.x) / distance1)) * 1000);
//				return (int)((o2.x - o1.x) * 100);
			} else {
				double val = o1.y - o2.y;
				
				if (val > 0) return 1;
				else if (val < 0) return -1;
				return 0;
			}
		}
	}
	
	void getHorizontalDistance(double y, double trackCenterX, ArrayList<MPoint> points, ArrayList<MPoint> sortPoints, double distance[]) {
		double yP1, yP2;

		double leftDis = 0, rightDis = 0;
		yP1 = points.get(0).y - y;
		for (int idx = 1; idx <= points.size(); idx ++) {
			int tmpIdx = idx % (points.size() - 1);
			
			yP2 = y - points.get(tmpIdx).y;
			if (((yP1 <= 0) && (yP2 <= 0)) || ((yP1 >= 0) && (yP2 >= 0))) { //在二点之间
				if (points.get(tmpIdx).x < trackCenterX) { //左边
					double xVal = (points.get(idx - 1).x + (yP1 / yP2) * points.get(tmpIdx).x) / ((yP1 / yP2) + 1);
					xVal = Math.abs(xVal - trackCenterX);
					 
					if (leftDis < xVal)
						leftDis = xVal;
				} else { //右边
					double xVal = (points.get(idx - 1).x + (yP1 / yP2) * points.get(tmpIdx).x) / ((yP1 / yP2) + 1);
					xVal = Math.abs(xVal - trackCenterX);
					 
					if (rightDis < xVal)
						rightDis = xVal;
				}
			}
			yP1 = -yP2;
		}
		
		distance[0] = leftDis;
		distance[1] = rightDis;
		
		try {
			yP1 = sortPoints.get(0).y - y;
			for (int idx = 0; idx <= sortPoints.size(); idx++) {
				int tmpIdx = idx % (sortPoints.size() - 1);
				
				yP2 = y - sortPoints.get(tmpIdx).y;

				if (((yP1 <= 0) && (yP2 <= 0)) || ((yP1 >= 0) && (yP2 >= 0))) { // 在二点之间
					if (sortPoints.get(tmpIdx).x < trackCenterX) { // 左边
						MPoint cPoint = new MPoint(trackCenterX - leftDis, y, 0);

						int minIdx = findMinDisPoint(cPoint, sortPoints, tmpIdx);
						distance[2] = sortPoints.get(minIdx).y;
					} else { // 右边
						MPoint cPoint = new MPoint(trackCenterX + rightDis, y,
								0);

						int minIdx = findMinDisPoint(cPoint, sortPoints, tmpIdx);
						distance[3] = sortPoints.get(minIdx).y;
					}
				}
				yP1 = -yP2;
			}
		} catch (Exception e) {
		}
	}
	
	int findMinDisPoint(MPoint point, ArrayList<MPoint> sortPoints, int idx) {
//		System.out.println("X: " + point.x + " Y: " + point.y);
		double tmpDis = 100;
		int minIdx = idx;
		for (int i = -5; i < 10; i ++) {
			int index = (i + idx + (sortPoints.size() - 1)) % (sortPoints.size() - 1);
			double dis = sortPoints.get(index).distanceForXY(point);
			if (dis < tmpDis) {
				tmpDis = dis;
				minIdx = index;
			}
//			System.out.print("[" + sortPoints.get(index).x + ", " + sortPoints.get(index).y + "] ");
		}
//		System.out.println();
		return minIdx;
	}
}
