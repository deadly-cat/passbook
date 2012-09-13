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
	
	/**
	 * Specify the row and column at the upper left of the component.
	 * The leftmost column has address gridx=0 and the top row has address gridy=0.
	 * Use GridBagConstraints.RELATIVE (the default value) to specify that the component be placed just to the right of (for gridx)
	 * or just below (for gridy) the component that was added to the container just before this component was added. 
	 * @param row
	 * @param col
	 * @return
	 */
	public GBH grid(int row, int col) {
		gridx = col;
		gridy = row;
		return this;
	}
	
	/**
	 * Specify the number of columns in the component's display area.
	 * These constraints specify the number of cells the component uses, not the number of pixels it uses.
	 * The default value is 1. Use GridBagConstraints.REMAINDER to specify that the component be the last
	 * one in its row .Use GridBagConstraints.RELATIVE to specify that the component be the next to last
	 * one in its row.
	 * @param width
	 * @return
	 */
	public GBH width(int width) {
		this.gridwidth = width;
		return this;
	}
	
	/**
	 * Specify the number of rows in the component's display area.
	 * These constraints specify the number of cells the component uses, not the number of pixels it uses.
	 * The default value is 1. Use GridBagConstraints.REMAINDER to specify that the component be the last
	 * one in its column. Use GridBagConstraints.RELATIVE to specify that the component be the next to last
	 * one in its column.
	 * @param height
	 * @return
	 */
	public GBH height(int height) {
		this.gridheight = height;
		return this;
	}
	
	/**
	 * Used when the component's display area is larger than the component's requested size to determine whether and how to resize the component.
	 * Valid values (defined as GridBagConstraints constants) include
	 * NONE (the default),
	 * HORIZONTAL (make the component wide enough to fill its display area horizontally, but do not change its height),
	 * VERTICAL (make the component tall enough to fill its display area vertically, but do not change its width),
	 * BOTH (make the component fill its display area entirely).
	 * @param fill
	 * @return
	 */
	public GBH fill(int fill) {
		this.fill = fill;
		return this;
	}
	
	/**
	 * Internal padding.
	 * Padding applies to both sides of the component
	 * @param x - padding value for left and right side
	 * @param y - padding value for top and bottom
	 * @return this
	 */
	public GBH padding(int x, int y) {
		this.ipadx = x;
		this.ipady = y;
		return this;
	}
	
	/**
	 * External padding
	 * @param top
	 * @param left
	 * @param bottom
	 * @param right
	 * @return
	 */
	public GBH insets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
		return this;
	}
	
	/**
	 * Used when the component is smaller than its display area to determine where (within the area) to place the component.
	 * Valid values (defined as GridBagConstraints constants) areCENTER (the default), PAGE_START, PAGE_END, LINE_START,
	 * LINE_END, FIRST_LINE_START, FIRST_LINE_END, LAST_LINE_END, and LAST_LINE_START.
	 * @param anchor
	 * @return
	 */
	public GBH anchor(int anchor) {
		this.anchor = anchor;
		return this;
	}
	
	/**
	 * Specify how much space get to cell for horizontal.
	 * Lager number - more space.
	 * Default value is 0.0. If all horizontal cell have 0.0 then space divide proportional
	 * @param weight 
	 * @return
	 */
	public GBH weightx(double weight) {
		this.weightx = weight;
		return this;
	}
	
	/**
	 * Specify how much space get to cell for vertical.
	 * Lager number - more space.
	 * Default value is 0.0. If all vertical cell have 0.0 then space divide proportional
	 * @param weight 
	 * @return
	 */
	public GBH weighty(double weight) {
		this.weighty = weight;
		return this;
	}
	
}
