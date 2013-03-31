package de.wsdevel.components.table;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * Created on 26.10.2005.
 * 
 * for project: tools
 * 
 * (c) 2005, Sebastian A. Weiss - All rights reserved.
 * 
 * This is a renderer for tables whose <code>TableModel</code> implement the
 * interface <code>SortedTableModel</code>. It displays an icon showing the
 * sorting of the column sorted.
 * 
 * The <code>Component</code> used to paint the cell is taken from the
 * <code>Table</code> passed to the constructor. This permits some customisation
 * while allowing this feature to be added.
 * 
 * @author <a href="mailto:post@sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweissTFH $ -- $Revision: 1.1 $ -- $Date: 2005/10/26
 *          16:56:23 $ <br>
 */
public class SortedTableHeaderRenderer implements TableCellRenderer {

	/**
	 * {@link ImageIcon}.
	 */
	private static final ImageIcon UP = new ImageIcon(
			ClassLoader.getSystemResource("images/up.gif"));

	/**
	 * {@link ImageIcon}.
	 */
	private static final ImageIcon DOWN = new ImageIcon(
			ClassLoader.getSystemResource("images/down.gif"));

	/**
	 * {@link DefaultTableCellRenderer}.
	 */
	private DefaultTableCellRenderer renderer;

	/**
	 * {@link SortedTableModel}.
	 */
	private SortedTableModel model;

	/**
	 * COMMENT.
	 * 
	 * @param tableVal
	 *            {@link JTable}
	 */
	public SortedTableHeaderRenderer(final JTable tableVal) {
		try {
			this.renderer = (DefaultTableCellRenderer) tableVal
					.getTableHeader().getDefaultRenderer();
			this.model = (SortedTableModel) tableVal.getModel();
		} catch (ClassCastException cce) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"TableModel has to be a SortedTableModel and DefaultRenderer has to be a DefaultTableCellRenderer!");
			iae.initCause(cce);
			throw iae;
		}
	}

	/**
	 * Use this constructor in case the model that you want to use is wrapped.
	 * 
	 * @param tableVal
	 *            {@link JTable}
	 * @param modelVal
	 *            {@link SortedTableModel}
	 */
	public SortedTableHeaderRenderer(final JTable tableVal,
			final SortedTableModel modelVal) {
		this.model = modelVal;
		try {
			this.renderer = (DefaultTableCellRenderer) tableVal
					.getTableHeader().getDefaultRenderer();
		} catch (ClassCastException cce) {
			IllegalArgumentException iae = new IllegalArgumentException(
					"DefaultRenderer has to be a DefaultTableCellRenderer!");
			iae.initCause(cce);
			throw iae;
		}
	}

	/**
	 * @param table
	 *            {@link JTable}
	 * @param value
	 *            {@link Object}
	 * @param isSelected
	 *            <code>boolean</code>
	 * @param hasFocus
	 *            <code>boolean</code>
	 * @param row
	 *            <code>int</code>
	 * @param column
	 *            <code>int</code>
	 * @return {@link Component}
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
	 *      java.lang.Object, boolean, boolean, int, int)
	 */
	public final Component getTableCellRendererComponent(final JTable table,
			final Object value, final boolean isSelected,
			final boolean hasFocus, final int row, final int column) {
		// The table is sorted by this row
		if ((this.model.getSortedColumn() >= 0)
				&& (this.model.getSortedColumn() == table
						.convertColumnIndexToModel(column))) {
			if (this.model.getAscending()) {
				this.renderer.setIcon(UP);
			} else {
				this.renderer.setIcon(DOWN);
			}
		} else {
			this.renderer.setIcon(null);
		}
		return this.renderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
	}
}
/*
 * $Log: SortedTableHeaderRenderer.java,v $ Revision 1.1 2006-05-02 16:06:00
 * sweissTFH cleaned up tools and moved everything to appropriate new packages
 * 
 * Revision 1.4 2006/04/05 18:19:34 sweissTFH cleaned up checkstyle errors
 * Revision 1.3 2005/12/27 16:06:01 sweissTFH moved to java 5 and very big clean
 * up!
 * 
 * Revision 1.2 2005/10/26 16:56:23 mschneiderTFH start of very big clean up and
 * commenting! (sw)
 */
