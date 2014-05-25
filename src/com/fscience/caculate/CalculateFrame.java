package com.fscience.caculate;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CalculateFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	int showType = 4;
	
	JTextField centerField = null;
	JTextField trackField = null;
	MPaintView view = null;
	JList listView = null;
	
	MParteSortedArray dumpArray = null;
	
	public CalculateFrame() {
		this.setSize(300, 200);
		this.setTitle("计算");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.initUI();
	}
	
	private void initUI() {
		this.getContentPane().setLayout(new BorderLayout());
		
		view = new MPaintView();
		this.add(view, BorderLayout.CENTER);
		
		{	
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(3, 2));
			
			JLabel centerLabel = new JLabel("切面中心：");
			panel.add(centerLabel);
			centerField = new JTextField("49331.046");//("49328.164");
			panel.add(centerField);
			
			JLabel trackLabel = new JLabel("轨道高：");
			panel.add(trackLabel);
			trackField = new JTextField("48.895");//("50.643");
			panel.add(trackField);
			
			//
			JPanel panel1 = new JPanel();
			panel1.setLayout(new GridLayout(4, 1));
			panel1.setBorder(new TitledBorder("控制属性"));
			
			JCheckBox orgBox = new JCheckBox("显示原始点", false);
			orgBox.addActionListener(this);
			panel1.add(orgBox);
			
			JCheckBox sortedBox = new JCheckBox("显示排序后的线", false);
			sortedBox.addActionListener(this);
			panel1.add(sortedBox);
			
			JCheckBox fixedBox = new JCheckBox("显示过滤后的线", true);
			fixedBox.addActionListener(this);
			panel1.add(fixedBox);
			
			JCheckBox orgXYBox = new JCheckBox("显示原始点XY值", false);
			orgXYBox.addActionListener(this);
			panel1.add(orgXYBox);
			//
			
			JButton button1 = new JButton("加载文件");
			button1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						double trackCenterX = Double.parseDouble(centerField
								.getText());
						double trackHeight = Double.parseDouble(trackField
								.getText());

						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						chooser.setCurrentDirectory(new File(
								"/Users/xiaorong/Desktop/提供资料"));
						if (chooser.showOpenDialog(CalculateFrame.this) == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();
							CalculateFrame.this.dumpData(file, trackCenterX, trackHeight);
//							CalculateFrame.this.dumpAllData(file);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(CalculateFrame.this,
								"请输入正确的数字。");
					}
				}
			});
			panel.add(button1);
			
			//
			listView = new JList();
			listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			listView.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (listView.getSelectedIndex() >= 0) {
						if (e.getClickCount() == 2) {
							MFiledSaver fs = dumpArray.get(listView.getSelectedIndex());
							File tmpFile = new File(".CalculateM" + File.separator + (int)fs.z + ".mc");
							CalculateFrame.this.dumpData(tmpFile, fs.x, fs.y);
						}
					}
				}
			});
			//
			JButton loadFileButton = new JButton("加载文件");
			loadFileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setCurrentDirectory(new File(
							"/Users/xiaorong/Desktop/提供资料"));
					if (chooser.showOpenDialog(CalculateFrame.this) == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						CalculateFrame.this.dumpAllData(file);
					}
				}
			});
			//
			JButton exportFileButton = new JButton("导出结果");
			exportFileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setCurrentDirectory(new File(
							"/Users/xiaorong/Desktop/提供资料"));
					if (chooser.showSaveDialog(CalculateFrame.this) == JFileChooser.APPROVE_OPTION) {
						CalculateFrame.this.exportToExcel(chooser.getSelectedFile());
					}
				}
			});
			//
			
			JPanel parentPanel = new JPanel();
			
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
			leftPanel.add(loadFileButton);
			leftPanel.add(exportFileButton);
			leftPanel.add(panel1);
//			leftPanel.add(panel);
			leftPanel.add(listView);
			parentPanel.add(leftPanel);
			
			this.add(parentPanel, BorderLayout.WEST);
			
			view.setType(showType);
		}
	}
	
	private void dumpAllData(File file) {
		MParteSortedArray array = new MParteSortedArray();
		
//		MWaittingDialog dialog = new MWaittingDialog(this, "提示", "正在加载文件，请稍等...");
//		dialog.pack();
//		dialog.setVisible(true);
		
		this.initDirectory();
		this.dumpForInit(array, file);
		this.dumpDataFile(array, file);
		
		dumpArray = array;
		
//		dialog.close();
	}
	
	private void initDirectory() {
		try {
			File mainDir = new File(".CalculateM");
			if (mainDir.exists()) {
				File files[] = mainDir.listFiles();
				for (File mcFile : files) {
					if (mcFile.isFile())
						mcFile.delete();
				}
			} else {
				mainDir.mkdirs();
			}
		} catch (Exception e) {
		}
	}
	
	private void dumpForInit(MParteSortedArray array, File readFile) {
		String readPath = readFile.getAbsolutePath();
		readPath = readPath.substring(0, readPath.lastIndexOf(".")) + ".txt";
		
		try {
			FileInputStream stream = new FileInputStream(readPath);
			InputStreamReader read = new InputStreamReader(stream, "GB2312");
			BufferedReader bufRead = new BufferedReader(read);
			
			String tmpString = null;
			while ((tmpString = bufRead.readLine()) != null) {
				String coord[] = tmpString.split("\t");
				
				MPoint point = new MPoint(coord[2], coord[3], coord[1]);
				array.addForInit(point, coord[0]);
			}
			bufRead.close();
			read.close();
			stream.close();
		} catch (Exception e) {
		}
	}
	
	private void dumpDataFile(MParteSortedArray array, File readFile) {
		try {
			FileInputStream stream = new FileInputStream(readFile);
			InputStreamReader read = new InputStreamReader(stream);
			BufferedReader bufRead = new BufferedReader(read);
			
			String tmpString = null;
			while ((tmpString = bufRead.readLine()) != null) {
				String coord[] = tmpString.split("\t");
				if (coord.length == 3) {
					// y, z, x
					MPoint point = new MPoint(coord[1], coord[2], coord[0]);
					array.add(point);
				}
			}
			
			bufRead.close();
			read.close();
			stream.close();
		} catch (Exception e) {
		} finally {
			for (MFiledSaver mFiledSaver : array) {
				mFiledSaver.close();
			}
			
			listView.setListData(array);
		}
	}
	
	private ArrayList<MPoint> dumpData(File file, double trackCenterX, double trackHeight) {
		ArrayList<MPoint> array = new ArrayList<MPoint>();
		try {
			FileInputStream stream = new FileInputStream(file);
			InputStreamReader read = new InputStreamReader(stream);
			BufferedReader bufRead = new BufferedReader(read);
			
			MBounds bounds = new MBounds();
			String tmpString = null;
			while ((tmpString = bufRead.readLine()) != null) {
				String coord[] = tmpString.split("\t");
				if (coord.length == 3) {
					// y, z, x
					MPoint point = new MPoint(coord[1], coord[2], "0");
//					MPoint point = new MPoint(coord[0], coord[2], coord[1]);
					array.add(point);
					bounds.addPoint(point);
				}
			}

			view.updateData(array, bounds, trackHeight, trackCenterX);
			view.repaint();
			
			bufRead.close();
			read.close();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return array;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("显示原始点")) {
			JCheckBox box = (JCheckBox)e.getSource();
			if (box.isSelected()) {
				showType |= 0x1;
			} else {
				showType &= 0xFFFE;
			}
		} else if (e.getActionCommand().equals("显示排序后的线")) {
			JCheckBox box = (JCheckBox)e.getSource();
			if (box.isSelected()) {
				showType |= 0x2;
			} else {
				showType &= 0xFFFD;
			}
		} else if (e.getActionCommand().equals("显示过滤后的线")) {
			JCheckBox box = (JCheckBox)e.getSource();
			if (box.isSelected()) {
				showType |= 0x4;
			} else {
				showType &= 0xFFFB;
			}
		} else if (e.getActionCommand().equals("显示原始点XY值")) {
			JCheckBox box = (JCheckBox)e.getSource();
			if (box.isSelected()) {
				showType |= 0x8;
			} else {
				showType &= 0xFFF7;
			}
		}
		
		view.setType(showType);
	}

	public void exportToExcel(File saveFile) {
		String[] title = { "编号", "产品名称", "产品价格", "产品数量", "生产日期", "产地", "是否出口" };
		try {
			OutputStream os = new FileOutputStream(saveFile); // 新建立一个jxl文件,即在e盘下生成testJXL.xls
			WritableWorkbook wwb = Workbook.createWorkbook(os); // 创建Excel工作薄
			WritableSheet sheet = wwb.createSheet("导出结果", 0); // 添加第一个工作表并设置第一个Sheet的名字
			Label label;
			for (int i = 0; i < title.length; i++) {
				// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, 0, title[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}
			wwb.write();
			wwb.close();
			os.close();
		} catch (Exception e) {
		}
	}
}
