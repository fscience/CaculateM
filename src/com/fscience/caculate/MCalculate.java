package com.fscience.caculate;

import java.util.ArrayList;

public class MCalculate {
	double topLeft, topRight, hTopLeft, hTopRight;
	double centerLeft, centerRight, hCenterLeft, hCenterRight;
	double bottomLeft, bottomRight, hBottomLeft, hBottomRight;
	
	public void calculate(ArrayList<MPoint> sortPoints, ArrayList<MPoint> points, MBounds bounds, double trackHeight, double trackCenterX) {
		double distance[] = new double[4];
//		System.out.println("最低线");
		getHorizontalDistance(trackHeight + 0.2, trackCenterX, points, sortPoints, distance);
		bottomLeft = distance[0]; bottomRight = distance[1]; hBottomLeft = distance[2]; hBottomRight = distance[3];
//		System.out.println("中心线");
		getHorizontalDistance(bounds.maxY - 2.6, trackCenterX, points, sortPoints, distance);
		centerLeft = distance[0]; centerRight = distance[1]; hCenterLeft = distance[2]; hCenterRight = distance[3];
//		System.out.println("最高线");
		getHorizontalDistance(trackHeight + 3.1, trackCenterX, points, sortPoints, distance);
		topLeft = distance[0]; topRight = distance[1]; hTopLeft = distance[2]; hTopRight = distance[3];
	}
	
//	void getHorizontalDistance(double y, double trackCenterX, ArrayList<MPoint> points, ArrayList<MPoint> sortPoints, double distance[]) {
//		
//	}
	
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
