package home.ingvar.passbook.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBH extends GridBagConstraints {

	private static final long serialVersionUID = 1L;
	
	public static GBH get() {
		return new GBH();
	}
	
	public static GBH get(int top, int left, int bottom, int right) {
		return new GBH(top, left, bottom, right);
	}
	
	public GBH() {
		this(5, 5, 5, 5);
	}
	
	public GBH(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
		anchor = LINE_START;
	}
	
	public GBH grid(int row, int col) {
		gridx = col;
		gridy = row;
		
		return this;
	}
	
	public GBH setAnchor(int anchor) {
		this.anchor = anchor;
		
		return this;
	}
	
	public GBH setWidth(int width) {
		this.gridwidth = width;
		
		return this;
	}
	
	public GBH setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
		
		return this;
	}
	
}
