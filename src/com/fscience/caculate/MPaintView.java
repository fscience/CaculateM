package com.fscience.caculate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MPaintView extends JPanel {
	private static final long serialVersionUID = 1L;
	
	ArrayList<MPoint> orgPoints = null;
	MBounds bounds = null;
	double designTrackHeight = 0;
	double trackCenterX = 0;
	double orgTrackCenterX = 0;
	
	GLMatrix matrix = null;
	MouseProcess process = null;
	
	MCalculate calculate = null;
	
	
	boolean showOrgPoint = true;
	boolean showSortLine = true;
	boolean showFixdLine = true;
	boolean showOrgPointXY = true;
	
	public MPaintView() {
		matrix = new GLMatrix();
		process = new MouseProcess();
		
		this.addMouseListener(process);
		this.addMouseMotionListener(process);
		this.addMouseWheelListener(process);
	}
	
	public void updateData(ArrayList<MPoint> points, MBounds bounds, double trackHeight, double trackCenterX) {
		this.orgPoints = points;
		this.bounds = bounds;
		this.designTrackHeight = trackHeight;
		this.orgTrackCenterX = trackCenterX;
		{
			double centerX = (bounds.maxX + bounds.minX) / 2.0;
			centerX = (Math.abs(centerX - trackCenterX) < 0.5) ? trackCenterX : centerX;
			this.trackCenterX = centerX;
		}
		
		if (calculate == null)
			calculate = new MCalculate();
		calculate.calculate(orgPoints, bounds, trackHeight, trackCenterX);
		
		double scale = 500 / (bounds.maxX - bounds.minX);
		matrix.identity();
		matrix.translate(380, 380, 0);
		matrix.scale(scale, scale, 1);
		matrix.rotateX(Math.PI);
		matrix.translate(-bounds.center().x, -bounds.center().y, -bounds.center().z);
		this.repaint();
				
		process.scale = scale;
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			double curStemp = 0;
			double stemp = Math.PI / 80;
			
			@Override
			public void run() {
				matrix.identity();
				matrix.translate(380, 380, 0);
				matrix.scale(150, 150, 1);
				matrix.rotateX(curStemp);
				matrix.translate(-MPaintView.this.bounds.center().x, -MPaintView.this.bounds.center().y, -MPaintView.this.bounds.center().z);
				MPaintView.this.repaint();
				
				curStemp += stemp;
			}
			
		}, 1000, 100);*/
	}

	public void setType(int showType) {
		this.showOrgPoint = ((showType & 0x1) == 0x1);
		this.showSortLine = ((showType & 0x2) == 0x2);
		this.showFixdLine = ((showType & 0x4) == 0x4);
		this.showOrgPointXY = ((showType & 0x8) == 0x8);
		
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getBounds().width, this.getBounds().height);
		
		if (orgPoints == null) return;
		
		ArrayList<MPoint> fixdPoints = calculate.filtedPoints;
		ArrayList<MPoint> sortedPoints = calculate.sortedPoints;
		
		//拟合后的轨道切面
		if (showFixdLine){
			g.setColor(Color.BLUE);
			
			MPoint prePoint = matrix.multiplyPoint(fixdPoints.get(fixdPoints
					.size() - 1));
			for (int idx = 0; idx < fixdPoints.size(); idx++) {
				MPoint curPoint = matrix.multiplyPoint(fixdPoints.get(idx));

				g.drawLine((int) prePoint.x, (int) prePoint.y,
						(int) curPoint.x, (int) curPoint.y);

				prePoint = curPoint;
			}
		}
		
		//排序后的轨道切面
		if (showSortLine) {
			g.setColor(Color.red);
			
			MPoint prePoint = matrix
					.multiplyPoint(sortedPoints.get(sortedPoints.size() - 1));
			for (int idx = 0; idx < sortedPoints.size(); idx++) {
				MPoint curPoint = matrix.multiplyPoint(sortedPoints.get(idx));

				g.drawLine((int) prePoint.x, (int) prePoint.y,
						(int) curPoint.x, (int) curPoint.y);

				prePoint = curPoint;
			}
		}
		
		//原始点
		if (showOrgPoint) {
			g.setColor(Color.WHITE);
			
			for (int idx = 0; idx < orgPoints.size(); idx++) {
				MPoint curPoint = matrix.multiplyPoint(orgPoints.get(idx));

				g.drawArc((int) curPoint.x, (int) curPoint.y, 6, 6, 0, 360);
			}
		}
		
		if (showOrgPointXY) {
			g.setColor(Color.WHITE);
			
			for (int idx = 0; idx < orgPoints.size(); idx++) {
				MPoint curPoint = matrix.multiplyPoint(orgPoints.get(idx));

				g.drawString("" + idx + "[" + orgPoints.get(idx).x + ", "
						+ orgPoints.get(idx).y + "]", (int) curPoint.x,
						(int) curPoint.y);
			}
		}
		
		MPoint p1, p2;
		
		if (trackCenterX != orgTrackCenterX) {
			g.setColor(Color.RED);
			p1 = matrix.multiplyPoint(new MPoint(this.orgTrackCenterX, bounds.minY, 0));
			p2 = matrix.multiplyPoint(new MPoint(this.orgTrackCenterX, bounds.maxY, 0));
			g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		}
		g.setColor(Color.GREEN);
		//垂直线
		p1 = matrix.multiplyPoint(new MPoint(this.trackCenterX, bounds.minY, 0));
		p2 = matrix.multiplyPoint(new MPoint(this.trackCenterX, bounds.maxY, 0));
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		//轨道线
		p1 = matrix.multiplyPoint(new MPoint(bounds.minX, this.designTrackHeight, 0));
		p2 = matrix.multiplyPoint(new MPoint(bounds.maxX, this.designTrackHeight, 0));
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		//最低线
		p1 = matrix.multiplyPoint(new MPoint(trackCenterX - calculate.bottomLeft, this.designTrackHeight + 0.2, 0));
		p2 = matrix.multiplyPoint(new MPoint(trackCenterX + calculate.bottomRight, this.designTrackHeight + 0.2, 0));
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		//中心线
		p1 = matrix.multiplyPoint(new MPoint(trackCenterX - calculate.centerLeft, bounds.maxY - 2.6, 0));
		p2 = matrix.multiplyPoint(new MPoint(trackCenterX + calculate.centerRight, bounds.maxY - 2.6, 0));
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		//最高线
		p1 = matrix.multiplyPoint(new MPoint(trackCenterX - calculate.topLeft, this.designTrackHeight + 3.1, 0));
		p2 = matrix.multiplyPoint(new MPoint(trackCenterX + calculate.topRight, this.designTrackHeight + 3.1, 0));
		g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		
		g.setColor(Color.WHITE);
		//上
		g.drawString("上：[" + calculate.topLeft + ", " + calculate.hTopLeft + "]，[" + calculate.topRight + ", " + calculate.hTopRight + "]", 10, 15);
		//中
		g.drawString("中：[" + calculate.centerLeft + ", " + calculate.hCenterLeft + "]，[" + calculate.centerRight + ", " + calculate.hCenterRight + "]", 10, 30);
		//下
		g.drawString("下：[" + calculate.bottomLeft + ", " + calculate.hBottomLeft + "]，[" + calculate.bottomRight + ", " + calculate.hBottomRight + "]", 10, 45);
	}
	
	class MouseProcess implements MouseListener, MouseMotionListener, MouseWheelListener {
		public double scale = 0;
		Point startPoint = null;
		MPoint startMPoint = null;
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			if (bounds == null) return;
			
			matrix.identity();
			matrix.translate(arg0.getX(), arg0.getY(), 0);
			matrix.scale(scale, scale, 1);
			matrix.rotateX(Math.PI);
			matrix.translate(-startMPoint.x, -startMPoint.y, -bounds.center().z);
			MPaintView.this.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
//			GLMatrix invertMatrix = matrix.invert();
//			MPoint point = invertMatrix.multiplyPoint(new MPoint(e.getX(), e.getY(), 0));
//			System.out.println("X: " + point.x + " Y: " + point.y);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			startPoint = e.getPoint();
			
			startMPoint = matrix.invert().multiplyPoint(new MPoint(e.getX(), e.getY(), 0));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent arg0) {
			if (bounds == null) return;
			
			GLMatrix invertMatrix = matrix.invert();
			MPoint point = invertMatrix.multiplyPoint(new MPoint(arg0.getX(), arg0.getY(), 0));
			
			
			double tmpScale = scale * (1 + arg0.getWheelRotation() * 0.5);
			if (tmpScale > 1)
				scale = tmpScale;
			
			matrix.identity();
			matrix.translate(arg0.getX(), arg0.getY(), 0);
			matrix.scale(scale, scale, 1);
			matrix.rotateX(Math.PI);
			matrix.translate(-point.x, -point.y, -bounds.center().z);
			MPaintView.this.repaint();
		}
	}
}
