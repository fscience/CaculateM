package com.fscience.caculate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintView extends JPanel implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 1L;

	ArrayList<MPoint> points = null;
	MBounds bounds = null;
	int xPoints[], yPoints[];
	
	double scale = 1;
	
	public PaintView() {
		xPoints = null;
		yPoints = null;
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	double rotateX, rotateY;
	public void setPoints(ArrayList<MPoint> points, MBounds bounds) {
		this.points = points;
		this.bounds = bounds;
		points.add(points.get(0));
		xPoints = new int[points.size()];
		yPoints = new int[points.size()];
	}
	
	private void reCalculate(int thisHeight) {
//		GLMatrix matrix = new GLMatrix();
//		matrix.rotateY(rotateY);
//		matrix.rotateX(rotateX);
//		matrix.rotateZ(0.5);
//		matrix.translate(100, 100, 0);
//		matrix.scale(10, 10, 0);
		
		
		
		double w = bounds.maxX - bounds.minX;
		double h = bounds.maxY - bounds.minY;
		double maxL = (w > h) ? w : h;
		scale = ((double)(this.getHeight() - 10)) / maxL;
		
		for (int idx = 0; idx < points.size(); idx ++) {
			xPoints[idx] = (int)(points.get(idx).x * scale);
			yPoints[idx] = thisHeight - (int)(points.get(idx).y * scale);
			
//			MPoint point = matrix.multiplyPoint(points.get(idx));
//			xPoints[idx] = (int)point.x;
//			yPoints[idx] = (int)point.y;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if (xPoints == null || yPoints == null) return;
		
		int thisHeight = this.getHeight() - 5;
		
		this.reCalculate(thisHeight);
		g.setColor(Color.WHITE);
		g.clearRect(0, 0, this.getBounds().width, this.getBounds().height);
		g.setColor(Color.BLACK);
		g.drawPolyline(xPoints, yPoints, xPoints.length);
		
		g.setColor(Color.RED);
		for (int idx = 0; idx < points.size(); idx ++) {
			g.drawOval(xPoints[idx], yPoints[idx], 2, 2);
		}
//		for (MPoint point : points) {
//			g.drawOval((int)point.x, (int)point.y, 2, 2);
//		}
		
//		for (int idx = 0; idx < points.size(); idx ++) {
//			g.drawString("" + idx, xPoints[idx], yPoints[idx]);
//		}
		g.setColor(Color.BLACK);
//		double mHeight = 47.928;
		double mHeight = 50.643;
		
//		int centerX = (int)((bounds.maxX - bounds.minX) / 2.0 * scale);
		int centerX = (int)((49328.164 - bounds.minX) * scale);
//		int centerY = (int)((bounds.maxY - bounds.minY) / 2.0 * scale);
//		int centerY = (int)((86553.669 - bounds.minY) * scale);
		int maxX = (int)((bounds.maxX - bounds.minX) * scale);
		
		g.drawLine(centerX, thisHeight, centerX, thisHeight - (int)((bounds.maxY - bounds.minY) * scale));
		
		int y = thisHeight - (int)((mHeight - bounds.minY) * scale);
		g.drawLine(0, y, maxX, y);
		g.drawString("轨道高度", centerX - 25, y);
		
		y = thisHeight - (int)((mHeight + 0.2 - bounds.minY) * scale);
		g.drawLine(0, y, maxX, y);
		g.drawString("0.2", centerX - 7, y);
		
		y = thisHeight - (int)((mHeight + 3.1 - bounds.minY) * scale);
		g.drawLine(0, y, maxX, y);
		g.drawString("3.1", centerX - 7, y);
		
		y = thisHeight - (int)((bounds.maxY - 2.6 - bounds.minY) * scale);
		g.drawLine(0, y, maxX, y);
		g.drawString("中心线", centerX - 15, y);
		
		g.setColor(Color.BLUE);
		this.calculate(g);
	}
	
	void findY(double y, double centerX, Graphics g) {
		double yP1, yP2;
		
		int drawY = this.getHeight() - 5 - (int)(y * scale);
		double leftDis = 100, rightDis = 0;
		yP1 = points.get(0).y - y;
		for (int idx = 1; idx < points.size(); idx ++) {
			yP2 = y - points.get(idx).y;
			if (((yP1 <= 0) && (yP2 <= 0)) || 
				((yP1 >= 0) && (yP2 >= 0))) {
				if (points.get(idx).x < centerX) {
					//左
					double xVal = (points.get(idx - 1).x + (yP1 / yP2) * points.get(idx).x) / ((yP1 / yP2) + 1);
					System.out.println(Math.abs(xVal - centerX));
					 
					if (leftDis > xVal)
						leftDis = xVal;
				} else {
					//右
					double xVal = (points.get(idx - 1).x + (yP1 / yP2) * points.get(idx).x) / ((yP1 / yP2) + 1);
					System.out.println(Math.abs(xVal - centerX));
					 
					if (rightDis < xVal)
						rightDis = xVal;
				}
//				System.out.print(idx + ",");
			}
			yP1 = -yP2;
//			System.out.println("find " + idx +" [yP1: " + yP1 + " yP2: " + yP2 + "]");
		}
		
		g.drawString("" + Math.abs(leftDis - centerX), (int)100, drawY);
		g.drawString("" + Math.abs(rightDis - centerX), (int)400, drawY);
	}
	
	public void calculate(Graphics g) {
//		double mHeight = 47.928;
		double mHeight = 50.643;
		
//		double centerX = (bounds.maxX - bounds.minX) / 2.0;
		double centerX = (49328.164 - bounds.minX);
		
		double y = mHeight + 0.2;
		System.out.print("Find Height: " + y + "[");
		this.findY(y- bounds.minY, centerX, g);
		System.out.println("]");
		
		y = mHeight + 3.08;
		System.out.print("Find Height: " + y + "[");
		this.findY(y- bounds.minY, centerX, g);
		System.out.println("]");
		
//		y = (bounds.maxY + bounds.minY) / 2.0;
		y = bounds.maxY - 2.6;
		System.out.print("Find Height: " + y + "[");
		this.findY(y- bounds.minY, centerX, g);
		System.out.println("]");
	}
	

	
	boolean beginDrag = true;
	Point prevPoint = new Point(0, 0);

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (beginDrag) {
			beginDrag = false;
			prevPoint.setLocation(arg0.getX(), arg0.getY());
		} else {
			rotateX = ((double)(arg0.getX() - prevPoint.x) / 100.0);
			rotateY = ((double)(arg0.getY() - prevPoint.y) / 100.0);
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		beginDrag = true; 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
