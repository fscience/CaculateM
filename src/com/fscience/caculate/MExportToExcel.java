package com.fscience.caculate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class MExportToExcel {
	MParteSortedArray cutFaceArray;
	File saveFile = null;
	public MExportToExcel(MParteSortedArray array, File saveFile) {
		this.cutFaceArray = array;
		this.saveFile = saveFile;
		
		CalRunnable runnable = new CalRunnable();
		new Thread(runnable).start();
	}
	
	class CalRunnable implements Runnable {
		
		private void writeLine(WritableSheet sheet, int line, String[] content) throws RowsExceededException, WriteException {
			Label label;
			for (int i = 0; i < content.length; i++) {
				// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, line, content[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
		}
		
		@Override
		public void run() {
			String[] title1 = { "里程", "坐标", "", "线路中线至左侧墙横距", "", "", "", "", "", "线路中线至左侧墙横距", "", "", "", "" };
			String[] title2 = { "里程", "X", "Y", "位置", "实测宽度(m)", "实测标高(m)", "设计轨面高(m)", "调整后宽度(m)", "差值(m)", "位置", "实测宽度(m)", "实测标高(m)", "设计宽度(m)", "差值(m)" };
			try {
				OutputStream os = new FileOutputStream(saveFile); // 新建立一个jxl文件,即在e盘下生成testJXL.xls
				WritableWorkbook wwb = Workbook.createWorkbook(os); // 创建Excel工作薄
				WritableSheet sheet = wwb.createSheet("导出结果", 0); // 添加第一个工作表并设置第一个Sheet的名字
				
				sheet.mergeCells(0, 0, 0, 1);
				sheet.mergeCells(1, 0, 2, 0);
				sheet.mergeCells(3, 0, 8, 0);
				sheet.mergeCells(9, 0, 13, 0);
				
				writeLine(sheet, 0, title1);
				writeLine(sheet, 1, title2);
				
				MCalculate calculate = new MCalculate();
				for (MFiledSaver cutFace : cutFaceArray) {
					MBounds bounds = cutFace.loadFromFile();
					calculate.calculate(cutFace.points, bounds, cutFace.y, cutFace.x);
					
					String[] result = new String[14];
					result[0] = cutFace.name;
					result[1] = "" + cutFace.x;
					result[2] = "" + cutFace.z;
				}
				
				wwb.write();
				wwb.close();
				os.close();
			} catch (Exception e) {
			}
		}
		
	}
}
