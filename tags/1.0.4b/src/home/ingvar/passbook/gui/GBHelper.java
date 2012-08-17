package home.ingvar.passbook.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBHelper extends GridBagConstraints {

	private static final long serialVersionUID = 1L;
	
	public GBHelper() {
		insets = new Insets(5, 5, 5, 5);
		anchor = LINE_START;
	}
	
	public GBHelper grid(int row, int col) {
		gridx = col;
		gridy = row;
		
		return this;
	}
	
	public GBHelper setAnchor(int anchor) {
		this.anchor = anchor;
		
		return this;
	}
	
	public GBHelper setWidth(int width) {
		this.gridwidth = width;
		
		return this;
	}
	
	public GBHelper setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
		
		return this;
	}
	
}
