package com.fscience.caculate;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MWaittingDialog extends JDialog {
	static final long serialVersionUID = 1L;

	public MWaittingDialog(JFrame parent, String title, String message) {
		super(parent, title, true);
		
		JOptionPane panel = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
		setContentPane(panel);
	}
	
	public void close() {
		setVisible(false);
		dispose();
	}
}
